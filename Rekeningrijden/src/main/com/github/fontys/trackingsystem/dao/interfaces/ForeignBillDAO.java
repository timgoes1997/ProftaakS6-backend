package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.payment.ForeignBill;

import java.util.List;

public interface ForeignBillDAO {
    void create(ForeignBill bill);

    void edit(ForeignBill bill);

    void remove(ForeignBill bill);

    ForeignBill find(int id);

    List<ForeignBill> findByOwnerId(Long ownerId);

    List<ForeignBill> findByStatus(String status);

    List<ForeignBill> findByVehicleId(int vehicleId);

    List<ForeignBill> getAll();
}
