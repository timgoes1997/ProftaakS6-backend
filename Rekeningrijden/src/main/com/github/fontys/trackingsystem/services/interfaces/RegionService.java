package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.EnergyLabel;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public interface RegionService {
    Region create(String name, List<BorderLocation> borderPoints);
    Region create(Region region);

    Region edit(Long id, String name, List<BorderLocation> borderPoints);
    Region edit(Long id, Region region);

    Region remove(Long id);
    Region remove(Region region);

    Rate createRate(Long region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime, Calendar addedDate, Long authorizer);
    Rate createRate(Rate rate);

    Rate editRate(Long id, Long region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime, Calendar addedDate, Long authorizer);
    Rate editRate(Long id, Rate rate);

    Rate removeRate(Rate rate);
    Rate removeRate(Long id);

    List<Rate> getRegionRates(Region region);
    Rate getRate(Location location);

    List<Region> getWithinRegions(Location location);
    List<Region> getWithinRegions(double x, double y);
    Region getWithinRegion(Location location);
    Region getWithinRegion(double x, double y);
}
