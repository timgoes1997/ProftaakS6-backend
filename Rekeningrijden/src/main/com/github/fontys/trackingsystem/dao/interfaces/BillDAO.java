package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.ForeignBill;

import java.util.List;

public interface BillDAO {
    void create(Bill bill);

    void edit(Bill bill);

    void remove(Bill bill);

    boolean exists(long id);

    Bill find(long id);

    List<Bill> findByOwnerId(Long ownerId);

    List<Bill> findByStatus(String status);

    List<Bill> findByVehicleId(long vehicleId);

    List<Bill> getAll();

    List<ForeignBill> getAllForeignBill();

    List<Bill> getAllCountryBill();
}
