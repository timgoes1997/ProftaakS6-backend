package com.github.fontys.trackingsystem.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.trackingsystem.dao.interfaces.RateDAO;
import com.github.fontys.trackingsystem.services.interfaces.RegionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Stateless
@Path("/region")
public class RegionBean {

    @Inject
    private RegionService regionService;

    @GET
    @Path("/rates")
    public List<Rate> getDefaultRates(){
        return regionService.getDefaultRates();
    }

    @GET
    @Path("/{name}/rates")
    public List<Rate> getRegionRates(@PathParam("name") String regionName){
        return regionService.getRegionRates(regionName);
    }

}
