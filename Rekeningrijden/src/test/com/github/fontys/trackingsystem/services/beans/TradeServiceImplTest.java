package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.helper.PersistenceHelper;
import com.github.fontys.entities.transfer.Transfer;
import com.github.fontys.entities.transfer.TransferStatus;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

public class TradeServiceImplTest {

    private EntityManager em;

    private String transferFrom = "email9@gmail.com";
    private String transferTo = "email1@gmail.com";

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        PersistenceHelper.cleanDataBase();
    }

    @Test
    public void createTransfer() {
//        PersistenceHelper.generateDummyData();
//        Account acc = PersistenceHelper.getAccountDAO().findByEmail(transferFrom);
//        Account acc2 = PersistenceHelper.getAccountDAO().findByEmail(transferTo);
//        User user = PersistenceHelper.getUserDAO().findByAccount(acc);
//        User user2 = PersistenceHelper.getUserDAO().findByAccount(acc2);
//        List<RegisteredVehicle> registeredVehicles = PersistenceHelper.getRegisteredVehicleDAO().findByUser(user.getId());
//        assertTrue(registeredVehicles.size() > 0);
//        RegisteredVehicle registeredVehicle = registeredVehicles.get(0);
//
//        TradeServiceImpl tradeService = PersistenceHelper.getTradeService();
//        tradeService.setCurrentUser(user);
//
//        //User createsTransfer
//        String licensePlate = registeredVehicle.getLicensePlate();
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        Transfer transfer = tradeService.createTransfer(registeredVehicle.getLicensePlate(),acc.getEmail());
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        //Transfer is still waiting for response new owner after the user has accepted it's token.
//        assertNotNull(transfer);
//        assertEquals(transfer.getStatus(), TransferStatus.WaitingForResponseNewOwner);
//
//        //The user which received the email accepts the transfer with the token.
//        tradeService.setCurrentUser(user2);
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        tradeService.accept(user2, transfer.getTransferToken());
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        assertEquals(transfer.getOwnerToTransferTo().getId(), user2.getId());
//
//        //The user which has accepted the transfer by owner now confirms it to the other user.
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        tradeService.acceptTransferNewOwner(transfer.getId());
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        //The new user has accepted the transfer status and the transfer status has been set to AcceptedNewOwner
//        assertEquals(transfer.getStatus(), TransferStatus.AcceptedNewOwner);
//
//        //Switch back to user and accept the trade on it's end.
//        tradeService.setCurrentUser(user);
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        tradeService.acceptTransferCurrentOwner(transfer.getId());
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        //Status has been set to AcceptedCurrentOwner
//        assertEquals(transfer.getStatus(), TransferStatus.AcceptedCurrentOwner);
//
//        //Switch back to NewOwner to confirm ownership of transfer and upload a file.
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        tradeService.setCurrentUser(user2);
//        ClassLoader classLoader = getClass().getClassLoader();
//        InputStream input = classLoader.getResourceAsStream("test.png");
//        try {
//            FormDataContentDisposition formDataContentDisposition = new FormDataContentDisposition("form-data; name=\"file\"; filename=\"test.png\"");
//            tradeService.confirmOwnership(transfer.getId(), input, formDataContentDisposition);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        assertEquals(transfer.getStatus(), TransferStatus.ConfirmedOwnership);
//
//        //Switch back to user to complete the transfer
//        tradeService.setCurrentUser(user);
//        PersistenceHelper.getEntityManager().getTransaction().begin();
//        tradeService.completeTransfer(transfer.getId());
//        PersistenceHelper.getEntityManager().getTransaction().commit();
//
//        assertEquals(transfer.getStatus(), TransferStatus.Completed);
    }
}