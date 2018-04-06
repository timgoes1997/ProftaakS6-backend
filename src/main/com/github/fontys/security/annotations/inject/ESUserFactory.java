/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fontys.security.annotations.inject;

import com.github.fontys.security.base.ESUser;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rowan
 */
public class ESUserFactory 
{
    @Inject
    private HttpServletRequest context;
    
    @Dependent
    @Produces
    @CurrentESUser
    public ESUser createESUser() {
        return (ESUser)context.getSession().getAttribute("ESUser");
    }
}
