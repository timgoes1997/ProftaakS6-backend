package com.github.fontys.trackingsystem.beans;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ping")
public class ExamplePingBean {

    @GET
    @Path("")
    public void ping() {
    }
}
