package com.github.fontys.trackingsystem.dao.interfaces;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.Region;

import java.util.List;

public interface RateDAO {
    void create(Rate regionRate);
    void edit(Rate regionRate);
    void remove(Rate regionRate);

    boolean exists(long id);
    Rate find(long id);
    List<Rate> findRates(Region region);
    List<Rate> findDefaultRates();
}
