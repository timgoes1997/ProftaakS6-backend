package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.entities.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.services.interfaces.FileService;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.github.fontys.entities.user.Role;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.FuelType;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.github.fontys.entities.vehicle.Vehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class VehicleServiceImpl implements VehicleService {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private FileService fileService;

    @Override
    public List<RegisteredVehicle> getVehiclesFromUser() {
        try {
            User user = (User) currentUser;
            if (user == null) {
                throw new NotAuthorizedException("User is not logged in");
            }
            return registeredVehicleDAO.findByUser(user.getId());
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't find any vehicles by user");
        }
    }

    @Override
    public Vehicle getVehicle(int id) {
        try {
            return vehicleDAO.find(id);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find a vehicle for given id");
        }
    }

    @Override
    public List<String> getBrands() {
        return vehicleDAO.getBrands();
    }

    @Override
    public List<Vehicle> getVehiclesByBrand(String brand) {
        return vehicleDAO.findByBrand(brand);
    }

    @Override
    public List<String> getVehicleModelsByBrand(String brand) {
        return vehicleDAO.findModelsByBrand(brand);
    }

    @Override
    public List<String> getEditionsByModelAndBrand(String brand, String model) {
        return vehicleDAO.findEditionsByModelAndBrand(brand, model);
    }

    @Override
    public List<String> getFuelTypesByModelBrandAndEdition(String brand, String model, String edition) {
        return vehicleDAO.findFuelTypesByModelBrandAndEdition(brand, model, edition);
    }

    @Override
    public List<String> getEnergyLabelsByModelBrandAndEdition(String brand, String model, String edition) {
        return vehicleDAO.findEnergyLabelsByModelBrandAndEdition(brand, model, edition);
    }

    @Override
    public List<String> getEnergyLabelsByModelBrandEditionAndFuelType(String brand, String model, String edition, FuelType fuelType) {
        return vehicleDAO.findEnergyLabelsByModelBrandEditionAndFuelType(brand, model, edition, fuelType);
    }

    @Override
    public List<RegisteredVehicle> getVehicles() {
        List<RegisteredVehicle> vehicles;

        // RETURN OWN VEHICLES FOR;
        // CUSTOMERS
        if (currentUser.getPrivilege() == Role.CUSTOMER) {
            vehicles = registeredVehicleDAO.findByUser(((User) currentUser).getId());
        } else {
            // FOR ANY OTHER ROLE, RETURN ALL
            vehicles = registeredVehicleDAO.getAll();
        }
        return vehicles;
    }

    @Override
    public Vehicle registerVehicle(String brand, String date, String modelName, String edition, String fuelTypeString, String energyLabelString) {
        FuelType fuelType = FuelType.valueOf(fuelTypeString);
        EnergyLabel energyLabel = EnergyLabel.valueOf(energyLabelString);

        Date inDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            inDate = sdf.parse(date);
            Vehicle v = vehicleDAO.find(modelName, edition, fuelType, energyLabel);
            if (v != null) {
                throw new NotAllowedException("Vehicle already exists");
            }
        } catch (EJBException e) { //Expects a NoResultException but that is hidden in EJBException
            vehicleDAO.create(new Vehicle(brand, inDate, modelName, edition, fuelType, energyLabel));
            Vehicle v = vehicleDAO.find(modelName, edition, fuelType, energyLabel);
            return v;
        } catch (ParseException e) {
            throw new NotAcceptableException("You have given a invalid date");
        }
        throw new InternalServerErrorException("Didn't expect this to happen when registering a vehicle");
    }

    @Override
    public RegisteredVehicle newVehicle(long vehicleID, long userID, String license, InputStream uploadedInputStream, FormDataContentDisposition fileDetails) {
        Vehicle v;
        try {
            v = vehicleDAO.find(vehicleID);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given vehicle");
        }

        User u;
        try {
            u = userDAO.find(userID);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }

        String location = fileService.writeToFile(uploadedInputStream, fileDetails);

        if (v == null || u == null) {
            throw new NotFoundException("Vehicle and user are invalid");
        }

        RegisteredVehicle cv = new RegisteredVehicle(u, license, v, location);
        registeredVehicleDAO.create(cv);
        return cv;
    }

    @Override
    public RegisteredVehicle newVehicle(String brand, String date, String modelName, String edition, String fuelTypeString, String energyLabelString, long userID, String license, InputStream uploadedInputStream, FormDataContentDisposition fileDetails) {
        FuelType fuelType = FuelType.valueOf(fuelTypeString);
        EnergyLabel energyLabel = EnergyLabel.valueOf(energyLabelString);

        Date inDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Vehicle v = null;
        try {
            inDate = sdf.parse(date);
            v = vehicleDAO.find(brand, inDate, modelName, edition, fuelType, energyLabel);
        } catch (EJBException e) { //Expects a NoResultException but that is hidden in EJBException
            v = new Vehicle(brand, inDate, modelName, edition, fuelType, energyLabel);
        }catch (ParseException e) {
            throw new NotAcceptableException("You have given a invalid date");
        }

        User u;
        try {
            u = userDAO.find(userID);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }

        String location = fileService.writeToFile(uploadedInputStream, fileDetails);

        if (v == null || u == null) {
            throw new NotFoundException("Vehicle and user are invalid");
        }

        RegisteredVehicle cv = new RegisteredVehicle(u, license, v, location);
        registeredVehicleDAO.create(cv);
        return cv;
    }

    @Override
    public RegisteredVehicle destroyVehicle(String license) {
        RegisteredVehicle registeredVehicle = getRegisteredVehicle(license);

        if (!(currentUser.getPrivilege() == Role.GOVERNMENT_EMPLOYEE || currentUser.getPrivilege() == Role.BILL_ADMINISTRATOR)) {
            throw new NotAuthorizedException("You are not authorized to destroy this vehicle");
        }

        registeredVehicle.destroy();
        registeredVehicle.setCustomer(null);
        registeredVehicleDAO.edit(registeredVehicle);
        return registeredVehicle;
    }

    @Override
    public File getProofOfOwnership(String license) {
        RegisteredVehicle registeredVehicle = getRegisteredVehicle(license);

        //TODO: Permissions for Bill Administrators etc to get ownership
        if(!Objects.equals(registeredVehicle.getCustomer().getId(), ((User) currentUser).getId())){
            throw new NotAuthorizedException("You are not authorized to destroy this vehicle");
        }

        return new File(registeredVehicle.getProofOfOwnership());
    }

    @Override
    public RegisteredVehicle getRegisteredVehicle(String license) {
        RegisteredVehicle registeredVehicle;
        try{
            registeredVehicle = registeredVehicleDAO.findByLicense(license);
            if(registeredVehicle == null){
                throw new NotFoundException("Couldn't find given vehicle");
            }
        }catch (Exception e){
            throw new NotFoundException("Couldn't find given vehicle");
        }
        return registeredVehicle;
    }

    public void setCurrentUser(ESUser currentUser) {
        this.currentUser = currentUser;
    }

    public void setVehicleDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setRegisteredVehicleDAO(RegisteredVehicleDAO registeredVehicleDAO) {
        this.registeredVehicleDAO = registeredVehicleDAO;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
