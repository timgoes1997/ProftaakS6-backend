package com.github.fontys.entities.transfer;

public enum TransferStatus {
    WaitingForResponseNewOwner,
    AcceptedNewOwner,
    DeclinedNewOwner,
    AcceptedCurrentOwner,
    DeclinedCurrentOwner,
    ConfirmedOwnership,
    Completed,
}
