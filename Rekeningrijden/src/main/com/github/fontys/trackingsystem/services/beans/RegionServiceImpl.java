package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.RateDAO;
import com.github.fontys.trackingsystem.dao.interfaces.RegionDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.interfaces.RegionService;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RegionServiceImpl implements RegionService {

    @Inject
    private RegionDAO regionDAO;

    @Inject
    private RateDAO rateDAO;

    @Inject
    private UserDAO userDAO;

    @Override
    public Region create(String name, List<BorderLocation> borderPoints) {
        return create(new Region(name, borderPoints));
    }

    @Override
    public Region create(Region region) {
        if (regionDAO.exists(region.getName())) {
            throw new ClientErrorException(Response.Status.CONFLICT);
        }

        regionDAO.create(region);
        return region;
    }

    @Override
    public Region edit(Long id, String name, List<BorderLocation> borderPoints) {
        return edit(id, new Region(name, borderPoints));
    }

    @Override
    public Region edit(Long id, Region region) {
        if (!regionDAO.exists(id)) {
            throw new NotFoundException("Region with given id doesn't exist");
        }
        if (regionDAO.exists(region.getName())) {
            throw new ClientErrorException(Response.Status.CONFLICT);
        }

        Region toMerge = regionDAO.find(id);
        toMerge.setName(region.getName());
        toMerge.setBorderPoints(region.getBorderPoints());

        regionDAO.edit(region);
        return region;
    }

    @Override
    public Region remove(Long id) {
        if (!regionDAO.exists(id)) {
            throw new NotFoundException("Region with given id doesn't exist");
        }

        Region region = regionDAO.find(id);
        regionDAO.remove(region);
        return region;
    }

    @Override
    public Region remove(Region region) {
        if(region == null){
            throw new NotAcceptableException("Region is null");
        }

        return remove(region.getId());
    }

    @Override
    public Rate createRate(Long region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime, Long authorizer) {
        Region foundRegion = null;
        if (regionDAO.exists(region)) {
             foundRegion = regionDAO.find(region);
        }

        if(!userDAO.exists(authorizer)){
            throw new NotFoundException("given user doesn't exist");
        }

        if(startTime == null || endTime == null){
            throw new NotAcceptableException("Start and enddate can't be null for a rate");
        }

        User foundAuthorizer = userDAO.find(authorizer);

        Rate rate = new Rate(foundRegion, kilometerPrice, energyLabel, startTime, endTime, GregorianCalendar.getInstance(), foundAuthorizer);
        rateDAO.create(rate);

        return rate;
    }

    @Override
    public Rate createRate(Rate rate) {

        if(rate == null || rate.getAuthorizer() == null){
            throw new NotAcceptableException("Rate and Authorizer can't be null while creating a rate!");
        }

        return createRate(rate.getRegion().getId(), rate.getKilometerPrice(), rate.getEnergyLabel(), rate.getStartTime(), rate.getEndTime(), rate.getAuthorizer().getId());
    }

    @Override
    public Rate removeRate(Rate rate) {
        if(rate == null){
            throw new NotAcceptableException("Can't remove a null Rate!");
        }
        return removeRate(rate.getId());
    }

    @Override
    public Rate removeRate(Long id) {
        if(!rateDAO.exists(id)){
            throw new NotFoundException("Rate couldn't be found!");
        }

        Rate toRemove = rateDAO.find(id);
        rateDAO.remove(toRemove);
        return toRemove;
    }

    @Override
    public List<Rate> getRegionRates(Region region) {
        return rateDAO.findRates(region);
    }

    @Override
    public Rate getRate(Location location) {
        return null;
    }

    @Override
    public List<Region> getWithinRegions(Location location) {
        return getWithinRegions(location.getLat(), location.getLon());
    }

    @Override
    public List<Region> getWithinRegions(double lat, double lon) {
        List<Region> regions = regionDAO.getAllRegions();
        List<Region> inRegions = new ArrayList<>();
        for (Region r : regions) {
            if (r.isWithinRegion(lat, lon)) {
                inRegions.add(r);
            }
        }
        return inRegions;
    }

    @Override
    public Region getWithinRegion(Location location) {
        return getWithinRegion(location.getLat(), location.getLon());
    }

    @Override
    public Region getWithinRegion(double lat, double lon) {
        List<Region> within = getWithinRegions(lat, lon);
        if (within.size() <= 0) {
            return null;
        }
        Region mostRecentlyAdded = within.get(0);
        for (Region region : within) {
            if (!region.getId().equals(mostRecentlyAdded.getId())) {
                if (region.getAddedDate().after(mostRecentlyAdded.getAddedDate())) {
                    mostRecentlyAdded = region;
                }
            }
        }
        return mostRecentlyAdded;
    }
}
