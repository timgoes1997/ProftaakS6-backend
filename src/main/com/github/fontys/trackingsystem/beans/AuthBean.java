package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.security.auth.ESAuth;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.User;

import javax.inject.Inject;
import javax.persistence.NoResultException;
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

    @POST
    @Path("/login")
    public Response logon(@FormParam("email") String email, @FormParam("password") String pass, @Context HttpServletRequest req) {
        try {
            Account a = accountDAO.findByEmail(email);
            if (a != null) {
                if(!a.getUser().getVerified()){
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Not registered").build();
                }
                // do stuff with password
                if (a.getPassword().equals(pass)) {
                    ESAuth.logon(req, a.getUser());
                    return Response.ok().build();
                }
            }
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    @GET
    @Path("/loggedIn")
    public Response loggedIn() {
        if (user != null) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    @POST
    @Path("/logout")
    public Response logoff(@Context HttpServletRequest req) {
        if (ESAuth.logout(req)) {
            // logged out
        }
        // / shrug
        return Response.ok().build();
    }

    // example getter
    @GET
    @EasySecurity(requiresUser = true)
    @Path("/me")
    public Response you() {
        return Response.ok("Yes, you").build();
    }
}
