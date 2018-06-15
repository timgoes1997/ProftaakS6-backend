package com.github.fontys.trackingsystem.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.security.base.ESUser;
import com.github.fontys.entities.user.Role;
import com.github.fontys.entities.user.User;
import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.dao.interfaces.RateDAO;
import com.github.fontys.trackingsystem.services.interfaces.RegionService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@Path("/region")
public class RegionBean {

    @Inject
    private RegionService regionService;

    @Inject
    @CurrentESUser
    private ESUser user;

    @Inject
    private Logger logger;

    @GET
    @Path("/rates")
    public List<Rate> getDefaultRates(){
        return regionService.getDefaultRates();
    }

    @GET
    @Path("/rates/{name}")
    public List<Rate> getRegionRates(@PathParam("name") String regionName){
        return regionService.getRegionRates(regionName);
    }

    @GET
    @Path("/name/{name}")
    public Region getRegion(@PathParam("name") String regionName){
        return regionService.getRegion(regionName);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Region create(Region region){
        return regionService.create(region);
    }

    @POST
    //@EasySecurity(requiresUser = true, grantedRoles = {"KILOMETER_TRACKER"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create/rates/{name}")
    public List<Rate> createRegionRates(@PathParam("name") String regionName, List<Rate> rates){
        return regionService.createRate(regionName, rates, (User)user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update/borders/{name}")
    public List<BorderLocation> updateBorders(@PathParam("name") String regionName, List<BorderLocation> locations){
        return regionService.updateBorders(regionName, locations);
    }

    @DELETE
    @Path("/delete/rate/{id}")
    public Rate deleteRate(@PathParam("id") Long id){
        return regionService.removeRate(id);
    }

    @DELETE
    @Path("/delete/region/{id}")
    public Region deleteRegion(@PathParam("id") Long id){
        return regionService.remove(id);
    }

}
