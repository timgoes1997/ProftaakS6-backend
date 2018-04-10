package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.user.Account;

import java.util.List;

public interface BillDAO {
    void create(Bill bill);

    void edit(Bill bill);

    void remove(Bill bill);

    Bill find(int id);

    List<Bill> findByOwnerId(int ownerId);

    List<Bill> findByStatus(String status);

    List<Bill> findByVehicleId(int vehicleId);

    List<Bill> getAll();
}
