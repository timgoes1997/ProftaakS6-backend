package com.github.fontys.trackingsystem.transfer;

import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.RegisteredVehicle;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "TRANSFER")
public class Transfer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REGISTERED_VEHICLE_ID")
    private RegisteredVehicle vehicleToTransfer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CURRENT_OWNER_ID")
    private User currentOwner;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TRANSFER_OWNER_ID")
    private User ownerToTransferTo;

    @Column(name="TOKEN")
    private String transferToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TransferStatus status;

    public Transfer(){

    }

    public Transfer(RegisteredVehicle vehicleToTransfer, String transferToken){
        this.vehicleToTransfer = vehicleToTransfer;
        this.transferToken = transferToken;
        this.status = TransferStatus.WaitingForResponseNewOwner;
        this.currentOwner = vehicleToTransfer.getCustomer();
    }

    public void setNewOwner(User newOwner){
        this.ownerToTransferTo = newOwner;
    }

    public void acceptedNewOwner(){
        this.status = TransferStatus.AcceptedNewOwner;
    }

    public void declineNewOwner(){
        this.status = TransferStatus.DeclinedNewOwner;
    }

    public void acceptedTransferOwner(){
        this.status = TransferStatus.AcceptedCurrentOwner;
    }

    public void declineCurrentOwner(){
        this.status = TransferStatus.DeclinedCurrentOwner;
    }

    public void confirmOwnerShip(){
        this.status = TransferStatus.ConfirmedOwnership;
    }

    public void completed(){
        this.status = TransferStatus.Completed;
    }

    public Long getId() {
        return id;
    }

    public RegisteredVehicle getVehicleToTransfer() {
        return vehicleToTransfer;
    }

    public void setVehicleToTransfer(RegisteredVehicle vehicleToTransfer) {
        this.vehicleToTransfer = vehicleToTransfer;
    }

    public User getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(User currentOwner) {
        this.currentOwner = currentOwner;
    }

    public User getOwnerToTransferTo() {
        return ownerToTransferTo;
    }

    public void setOwnerToTransferTo(User ownerToTransferTo) {
        this.ownerToTransferTo = ownerToTransferTo;
    }

    public String getTransferToken() {
        return transferToken;
    }

    public void setTransferToken(String transferToken) {
        this.transferToken = transferToken;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
