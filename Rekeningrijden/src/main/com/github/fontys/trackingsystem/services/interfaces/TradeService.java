package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.transfer.Transfer;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface TradeService {
    Transfer getTransfer(long id);
    Transfer getTransfer(String token);
    Transfer createTransfer(String license, String email);
    Account acceptTokenAlreadyLoggedIn(String token);
    Account acceptTokenLogin(String token, String email, String password, HttpServletRequest req);
    Account acceptTokenRegister(String token, String name, String address, String residency, String email, String username, String password, HttpServletRequest req);
    Account accept(User user, String token);

    Transfer acceptTransfer(long id);
    Transfer declineTransfer(long id);

    Transfer acceptTransferNewOwner(long id);
    Transfer acceptTransferNewOwner(Transfer transfer);
    Transfer declineTransferNewOwner(long id);
    Transfer declineTransferNewOwner(Transfer transfer);

    Transfer acceptTransferCurrentOwner(long id);
    Transfer acceptTransferCurrentOwner(Transfer transfer);
    Transfer declineTransferCurrentOwner(long id);
    Transfer declineTransferCurrentOwner(Transfer transfer);

    Transfer confirmOwnership(long id, InputStream uploadedInputStream, FormDataContentDisposition fileDetails);
    Transfer completeTransfer(long id);

    List<Transfer> getTransfersFromCurrentUser();
    List<Transfer> getTransfersToCurrentUser();
    List<Transfer> getTransfersFromUser(long id);
    List<Transfer> getTransfersToUser(long id);
    List<Transfer> getTransfersVehicle(long id);

    String generateTradeToken();

    File getProofOfOwnership(long id);
}
