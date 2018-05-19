package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

public class TradeServiceImpl implements TradeService {

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
        return null;
    }

    @Override
    public List<Transfer> getTransfersFromCurrentUser() {
        return null;
    }

    @Override
    public List<Transfer> getTransfersToCurrentUser() {
        return null;
    }

    @Override
    public List<Transfer> getTransfersFromUser(long id) {
        return null;
    }

    @Override
    public List<Transfer> getTransfersToUser(long id) {
        return null;
    }

    @Override
    public List<Transfer> getTransfersVehicle(long id) {
        return null;
    }
}
