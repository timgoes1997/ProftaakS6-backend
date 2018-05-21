package com.github.fontys.trackingsystem.beans;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.logging.Logger;

@Path("/ping")
public class ExamplePingBean {

    @Inject
    Logger logger;

    @GET
    @Path("")
    public void ping() {
        logger.info("ping");
    }
}
