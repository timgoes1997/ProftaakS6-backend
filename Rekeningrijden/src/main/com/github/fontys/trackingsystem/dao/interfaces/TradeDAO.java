package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.transfer.Transfer;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.RegisteredVehicle;

import java.util.List;

public interface TradeDAO {
    void create(Transfer transfer);
    void edit(Transfer transfer);
    void remove(Transfer transfer);
    boolean tokenExists(String token);
    Transfer find(long id);
    Transfer findByToken(String token);
    List<Transfer> findFromUser(User user);
    List<Transfer> findToUser(User user);
    List<Transfer> findForVehicle(RegisteredVehicle vehicle);
}
