package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.RegisteredVehicleDAO;
import com.github.fontys.trackingsystem.dao.interfaces.TradeDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.email.EmailTradeServiceImpl;
import com.github.fontys.trackingsystem.services.email.interfaces.EmailTradeService;
import com.github.fontys.trackingsystem.services.interfaces.AuthService;
import com.github.fontys.trackingsystem.services.interfaces.FileService;
import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.services.interfaces.UserService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.transfer.TransferStatus;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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
    private UserService userService;

    @Inject
    private FileService fileService;

    @Inject
    private EmailTradeService emailTradeService;

    @Inject
    private AuthService authService;

    @Inject
    private Logger logger;

    @Override
    public Transfer createTransfer(String license, String email) {
        RegisteredVehicle registeredVehicle;
        try {
            registeredVehicle = registeredVehicleDAO.findByLicense(license);
            if (registeredVehicle == null) {
                throw new NotFoundException("There are no vehicles for given id");
            }
        } catch (Exception e) {
            throw new NotFoundException("There are no vehicles for given id");
        }

        if (registeredVehicle.getCustomer().getId() != ((User) currentUser).getId()) {
            throw new NotAuthorizedException("You don't own the given vehicle");
        }

        //Check for transfers that are still in progress
        List<Transfer> transfers = tradeDAO.findForVehicle(registeredVehicle);
        for (Transfer t : transfers) {
            if (t.getStatus() == TransferStatus.WaitingForResponseNewOwner ||
                    t.getStatus() == TransferStatus.AcceptedCurrentOwner ||
                    t.getStatus() == TransferStatus.AcceptedNewOwner ||
                    t.getStatus() == TransferStatus.ConfirmedOwnership) {
                throw new BadRequestException("There are still transfers in progress please complete them before requesting a new one");
            }
        }


        User user = userDAO.find(((User) currentUser).getId());
        Transfer transfer = new Transfer(user, registeredVehicle, generateTradeToken());
        tradeDAO.create(transfer);
        try {
            emailTradeService.sendTransferMail(transfer, email);
            return transfer;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new InternalServerErrorException("Either couldn't create transfer or send a e-mail to given email adress");
        }
    }

    @Override
    public Transfer acceptTokenAlreadyLoggedIn(String token) {
        return accept((User) currentUser, token);
    }

    @Override
    public Transfer acceptTokenLogin(String token, String email, String password, HttpServletRequest req) {
        Account account = authService.logon(email, password, req);
        return accept(account.getUser(), token);
    }

    @Override
    public Transfer acceptTokenRegister(String token, String name, String address, String residency, String email, String username, String password, HttpServletRequest req) {
        return accept(userService.createCustomer(name, address, residency, email, username, password), token);
    }

    @Override
    public Transfer accept(User user, String token) {
        Transfer transfer = tradeDAO.findByToken(token);
        if (transfer == null) {
            throw new NotFoundException("Couldn't find a transfer with the given token");
        }
        if (transfer.getOwnerToTransferTo() != null) {
            throw new NotAllowedException("Token has already been used by a user");
        }
        User persistent = userDAO.find(user.getId());
        transfer.setOwnerToTransferTo(persistent);
        tradeDAO.edit(transfer);
        return transfer;
    }

    @Override
    public Transfer acceptTransferNewOwner(long id) {
        Transfer transfer = getTransfer(id);
        if (transfer.getStatus() == TransferStatus.WaitingForResponseNewOwner) {
            transfer.acceptedNewOwner();
            tradeDAO.edit(transfer);
            return transfer;
        } else {
            throw new NotAllowedException("You are not allowed to accept this transfer");
        }
    }

    @Override
    public Transfer declineTransferNewOwner(long id) {
        Transfer transfer = getTransfer(id);
        if (transfer.getStatus() == TransferStatus.WaitingForResponseNewOwner ||
                transfer.getStatus() == TransferStatus.AcceptedCurrentOwner ||
                transfer.getStatus() == TransferStatus.AcceptedNewOwner) {
            transfer.declineNewOwner();
            tradeDAO.edit(transfer);
            return transfer;
        } else {
            throw new NotAllowedException("You are not allowed to accept this transfer");
        }
    }

    @Override
    public Transfer acceptTransferCurrentOwner(long id) {
        Transfer transfer = getTransfer(id);
        if (transfer.getStatus() == TransferStatus.AcceptedNewOwner) {
            transfer.acceptedCurrentOwner();
            tradeDAO.edit(transfer);
            return transfer;
        } else {
            throw new NotAllowedException("You are not allowed to accept this transfer");
        }
    }

    @Override
    public Transfer declineTransferCurrentOwner(long id) {
        Transfer transfer = getTransfer(id);
        if (transfer.getStatus() == TransferStatus.WaitingForResponseNewOwner ||
                transfer.getStatus() == TransferStatus.AcceptedCurrentOwner ||
                transfer.getStatus() == TransferStatus.AcceptedNewOwner ||
                transfer.getStatus() == TransferStatus.ConfirmedOwnership) {
            transfer.declineCurrentOwner();
            tradeDAO.edit(transfer);
            return transfer;
        } else {
            throw new NotAllowedException("You are not allowed to accept this transfer");
        }
    }

    @Override
    public Transfer confirmOwnership(long id, InputStream uploadedInputStream, FormDataContentDisposition fileDetails) {
        Transfer transfer = getTransfer(id);
        if (transfer.getStatus() == TransferStatus.AcceptedCurrentOwner) {
            transfer.confirmOwnerShip(fileService.writeToFile(uploadedInputStream, fileDetails));
            tradeDAO.edit(transfer);
            return transfer;
        } else {
            throw new NotAllowedException("You are not allowed to confirm this transfer");
        }
    }

    @Override
    public Transfer completeTransfer(long id) {
        Transfer transfer = getTransfer(id);
        RegisteredVehicle registeredVehicle = transfer.getVehicleToTransfer();
        if (registeredVehicle == null) {
            throw new NotFoundException("There are no vehicles for transfer with given id");
        }

        if (transfer.getStatus() == TransferStatus.ConfirmedOwnership) {
            registeredVehicle.setCustomer(transfer.getOwnerToTransferTo());
            registeredVehicle.setProofOfOwnership(transfer.getProofOfOwnership());
            registeredVehicleDAO.edit(registeredVehicle);
            transfer.completed();
            tradeDAO.edit(transfer);
        } else {
            throw new NotAllowedException("You are not allowed to complete the transaction yet!");
        }

        return transfer;
    }

    @Override
    public Transfer getTransfer(long id) {
        Transfer transfer;
        try {
            transfer = tradeDAO.find(id);
            if (transfer == null) {
                throw new NotFoundException("There are no transfers with given id");
            }
        } catch (Exception e) {
            throw new NotFoundException("There are no transfers with given id");
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

    public void setCurrentUser(ESUser currentUser) {
        this.currentUser = currentUser;
    }

    public void setTradeDAO(TradeDAO tradeDAO) {
        this.tradeDAO = tradeDAO;
    }

    public void setRegisteredVehicleDAO(RegisteredVehicleDAO registeredVehicleDAO) {
        this.registeredVehicleDAO = registeredVehicleDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setEmailTradeService(EmailTradeService emailTradeService) {
        this.emailTradeService = emailTradeService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setLogger() {
        this.logger = Logger.getLogger(TradeServiceImpl.class.getName());
    }
}
