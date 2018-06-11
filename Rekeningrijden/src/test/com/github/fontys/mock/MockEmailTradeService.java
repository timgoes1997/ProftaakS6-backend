package com.github.fontys.mock;

import com.github.fontys.trackingsystem.services.email.interfaces.EmailTradeService;
import com.github.fontys.entities.transfer.Transfer;

public class MockEmailTradeService implements EmailTradeService {

    @Override
    public void sendTransferMail(Transfer transfer, String email) {

    }

    @Override
    public void sendStatusUpdate(Transfer transfer) {

    }
}
