package com.github.fontys.trackingsystem.transfer;

public enum TransferStatus {
    WaitingForResponseNewOwner,
    AcceptedNewOwner,
    DeclinedNewOwner,
    AcceptedCurrentOwner,
    DeclinedCurrentOwner,
    ConfirmedOwnership,
    Completed,
}
