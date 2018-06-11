package com.github.fontys.entities.transfer;

import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.RegisteredVehicle;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@Entity(name = "TRANSFER")
@NamedQueries({
        @NamedQuery(name = Transfer.FIND_BYID,
                query = "SELECT t FROM TRANSFER t WHERE t.id=:id"),
        @NamedQuery(name = Transfer.FIND_BY_VEHICLE,
                query = "SELECT t FROM TRANSFER t WHERE t.vehicleToTransfer.id=:id"),
        @NamedQuery(name = Transfer.FIND_BY_TOKEN,
                query = "SELECT t FROM TRANSFER t WHERE t.transferToken=:token"),
        @NamedQuery(name = Transfer.FIND_BY_CURRENT_OWNER,
                query = "SELECT t FROM TRANSFER t WHERE t.currentOwner.id=:id"),
        @NamedQuery(name = Transfer.FIND_BY_NEW_OWNER,
                query = "SELECT t FROM TRANSFER t WHERE t.ownerToTransferTo.id=:id"),
})
public class Transfer implements Serializable{

    // ======================================
    // =             Queries              =
    // ======================================

    public static final String FIND_BYID = "Transfer.findByID";
    public static final String FIND_BY_VEHICLE = "Transfer.findByVehicle";
    public static final String FIND_BY_NEW_OWNER = "Transfer.findByNewOwner";
    public static final String FIND_BY_CURRENT_OWNER = "Transfer.findByCurrentOwner";
    public static final String FIND_BY_TOKEN = "Transfer.findByToken";

    // ======================================
    // =             Fields              =
    // ======================================

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

    @Column(name="PROOF_OF_OWNERSHIP")
    private String proofOfOwnership;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TransferStatus status;

    public Transfer(){

    }

    public Transfer(User currentOwner, RegisteredVehicle vehicleToTransfer, String transferToken){
        this.currentOwner = currentOwner;
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

    public void acceptedCurrentOwner(){
        this.status = TransferStatus.AcceptedCurrentOwner;
    }

    public void declineCurrentOwner(){
        this.status = TransferStatus.DeclinedCurrentOwner;
    }

    public void confirmOwnerShip(String proofOfOwnership){
        this.proofOfOwnership = proofOfOwnership;
        this.status = TransferStatus.ConfirmedOwnership;
    }

    public void completed(){
        this.status = TransferStatus.Completed;
    }

    @XmlAttribute
    public long getId() {
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

    public String getProofOfOwnership() {
        return proofOfOwnership;
    }

    public void setProofOfOwnership(String proofOfOwnership) {
        this.proofOfOwnership = proofOfOwnership;
    }
}
