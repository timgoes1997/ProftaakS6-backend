package com.github.fontys.trackingsystem.beans;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

@Startup
@Singleton
public class IntegrationBean {

    @Inject
    private Logger logger;

    @PostConstruct
    public void init(){
        logger.info("Hello this is our integration!");
    }

}
