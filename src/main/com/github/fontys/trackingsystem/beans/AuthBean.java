package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.services.beans.AuthService;
import com.github.fontys.trackingsystem.user.Account;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthBean {

    @Inject
    @CurrentESUser
    private ESUser user;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AuthService authService;

    @POST
    @Path("/login")
    public Account logon(@FormParam("email") String email, @FormParam("password") String pass, @Context HttpServletRequest req) {
        return authService.logon(email, pass, req);
    }

    @GET
    @Path("/loggedIn")
    public void loggedIn() {
        authService.isLoggedIn();
    }

    @POST
    @Path("/logout")
    public void logoff(@Context HttpServletRequest req) {
        authService.logout(req);
    }

    // example getter
    @GET
    @EasySecurity(requiresUser = true)
    @Path("/me")
    public Response you() {
        return Response.ok("Yes, you").build();
    }
}
