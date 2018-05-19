package com.github.fontys.trackingsystem.dao;

import com.github.fontys.trackingsystem.dao.interfaces.TradeDAO;

public class TradeDAOImpl implements TradeDAO {
    @Override
    public boolean tokenExists(String token) {
        return false;
    }
}
