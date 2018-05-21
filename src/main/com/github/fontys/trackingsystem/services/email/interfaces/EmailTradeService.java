package com.github.fontys.trackingsystem.services.email.interfaces;

import com.github.fontys.trackingsystem.transfer.Transfer;

public interface EmailTradeService {
    void sendTransferMail(Transfer transfer, String email);
    void sendStatusUpdate(Transfer transfer);
}
