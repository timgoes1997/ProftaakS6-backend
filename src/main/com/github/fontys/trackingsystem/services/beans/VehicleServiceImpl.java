package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.services.interfaces.VehicleService;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.FuelType;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import com.github.fontys.trackingsystem.vehicle.Vehicle;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    @Override
    public GenericEntity<List<RegisteredVehicle>> getVehiclesFromUser() {
        try {
            User user = (User) currentUser;
            if (user == null) {
                throw new NotAuthorizedException("User is not logged in");
            }
            List<RegisteredVehicle> registeredVehicles = registeredVehicleDAO.findByUser(user.getId());
            GenericEntity<List<RegisteredVehicle>> list = new GenericEntity<List<RegisteredVehicle>>(registeredVehicles) {
            };
            return list;
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
    public GenericEntity<List<String>> getBrands() {
        List<String> brands = vehicleDAO.getBrands();
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(brands) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<Vehicle>> getVehiclesByBrand(String brand) {
        List<Vehicle> vehicles = vehicleDAO.findByBrand(brand);
        GenericEntity<List<Vehicle>> list = new GenericEntity<List<Vehicle>>(vehicles) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<String>> getVehicleModelsByBrand(String brand) {
        List<String> models = vehicleDAO.findModelsByBrand(brand);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(models) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<String>> getEditionsByModelAndBrand(String brand, String model) {
        List<String> models = vehicleDAO.findEditionsByModelAndBrand(brand, model);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(models) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<String>> getFuelTypesByModelBrandAndEdition(String brand, String model, String edition) {
        List<String> fuelTypes = vehicleDAO.findFuelTypesByModelBrandAndEdition(brand, model, edition);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<String>> getEnergyLabelsByModelBrandAndEdition(String brand, String model, String edition) {
        List<String> fuelTypes = vehicleDAO.findEnergyLabelsByModelBrandAndEdition(brand, model, edition);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<String>> getEnergyLabelsByModelBrandEditionAndFuelType(String brand, String model, String edition, FuelType fuelType) {
        List<String> fuelTypes = vehicleDAO.findEnergyLabelsByModelBrandEditionAndFuelType(brand, model, edition, fuelType);
        GenericEntity<List<String>> list = new GenericEntity<List<String>>(fuelTypes) {
        };
        return list;
    }

    @Override
    public GenericEntity<List<RegisteredVehicle>> getVehicles() {
        List<RegisteredVehicle> vehicles;

        // RETURN OWN VEHICLES FOR;
        // CUSTOMERS
        if (currentUser.getPrivilege() == Role.CUSTOMER) {
            vehicles = registeredVehicleDAO.findByUser(((User) currentUser).getId());
        } else {
            // FOR ANY OTHER ROLE, RETURN ALL
            vehicles = registeredVehicleDAO.getAll();
        }
        GenericEntity<List<RegisteredVehicle>> list = new GenericEntity<List<RegisteredVehicle>>(vehicles) {
        };
        return list;
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

        String uploadedFileLocation = System.getProperty("user.dir") + "//files//";

        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            throw new NotAcceptableException("No file given");
        }

        File f = getNewFile(uploadedFileLocation, extension);//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String location = uploadedFileLocation + f.getName();

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
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

        String uploadedFileLocation = System.getProperty("user.dir") + "//files//";

        String extension = FilenameUtils.getExtension(fileDetails.getFileName());

        if (extension.equals("")) {
            throw new NotAcceptableException("No file given");
        }

        File f = getNewFile(uploadedFileLocation, extension);//getNewFile(uploadedFileLocation, extension);
        // save it
        writeToFile(uploadedInputStream, f);

        String location = uploadedFileLocation + f.getName();

        if (v == null || u == null) {
            throw new NotFoundException("Vehicle and user are invalid");
        }

        RegisteredVehicle cv = new RegisteredVehicle(u, license, v, location);
        registeredVehicleDAO.create(cv);
        return cv;
    }

    private File getNewFile(String uploadFileLocation, String fileType) {
        Random random = new SecureRandom();
        File f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        while (f.exists()) {
            f = new File(uploadFileLocation + getRandomFileName(random, fileType));
        }
        return f;
    }

    private String getRandomFileName(Random random, String fileType) {
        return new BigInteger(130, random).toString(32) + "." + fileType;
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             File f) {
        try {
            OutputStream out;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(f);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


}
