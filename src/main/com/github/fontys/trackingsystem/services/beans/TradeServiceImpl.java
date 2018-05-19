package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TradeDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.services.email.EmailTradeService;
import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.transfer.TransferStatus;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TradeServiceImpl implements TradeService {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private TradeDAO tradeDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private EmailTradeService emailTradeService;

    @Override
    public Transfer createTransfer(String email) {
        return null;
    }

    @Override
    public Transfer acceptTokenAlreadyLoggedIn(String token) {
        return null;
    }

    @Override
    public Transfer acceptTokenLogin(String token, String email, String password) {
        return null;
    }

    @Override
    public Transfer acceptTokenRegister(String token, String name, String address, String residency, String email, String username, String password) {
        return null;
    }

    @Override
    public Transfer acceptTransferNewOwner(long id) {
        return null;
    }

    @Override
    public Transfer declineTransferNewOwner(long id) {
        return null;
    }

    @Override
    public Transfer acceptTransferCurrentOwner(long id) {
        return null;
    }

    @Override
    public Transfer declineTransferCurrentOwner(long id) {
        return null;
    }

    @Override
    public Transfer confirmOwnership(long id, InputStream uploadedInputStream, FormDataContentDisposition fileDetails) {
        return null;
    }

    @Override
    public Transfer completeTransfer(long id) {

        Transfer transfer = null;
        try {
            transfer = tradeDAO.find(id);
        } catch (Exception e) {
            throw new NotFoundException("There are no transfers with given id");
        }

        if (transfer == null) {
            throw new NotFoundException("There are no transfers with given id");
        }

        RegisteredVehicle registeredVehicle = transfer.getVehicleToTransfer();
        if(registeredVehicle == null){
            throw new NotFoundException("There are no vehicles for transfer with given id");
        }

        registeredVehicle.setCustomer(transfer.getOwnerToTransferTo());
        registeredVehicleDAO.edit(registeredVehicle);


        if (transfer.getStatus() == TransferStatus.ConfirmedOwnership) {
            transfer.completed();
            tradeDAO.edit(transfer);
        } else {
            throw new NotAllowedException("You are not allowed to complete the transaction yet!");
        }

        return transfer;
    }

    @Override
    public List<Transfer> getTransfersFromCurrentUser() {
        try {
            User user = userDAO.find(((User) currentUser).getId());
            return tradeDAO.findFromUser(user);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }
    }

    @Override
    public List<Transfer> getTransfersToCurrentUser() {
        try {
            User user = userDAO.find(((User) currentUser).getId());
            return tradeDAO.findToUser(user);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }
    }

    @Override
    public List<Transfer> getTransfersFromUser(long id) {
        try {
            User user = userDAO.find(id);
            return tradeDAO.findFromUser(user);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }
    }

    @Override
    public List<Transfer> getTransfersToUser(long id) {
        try {
            User user = userDAO.find(id);
            return tradeDAO.findToUser(user);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given user");
        }
    }

    @Override
    public List<Transfer> getTransfersVehicle(long id) {
        RegisteredVehicle vehicle = registeredVehicleDAO.find(id);
        if (vehicle == null) {
            throw new NotFoundException("Given vehicle doesn't exist");
        }

        return tradeDAO.findForVehicle(vehicle);
    }

    @Override
    public String generateTradeToken() {
        String token = UUID.randomUUID().toString();
        if (tradeDAO.tokenExists(token)) {
            return generateTradeToken();
        } else {
            return token;
        }
    }
}
