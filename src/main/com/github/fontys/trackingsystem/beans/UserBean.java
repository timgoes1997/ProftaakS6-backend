package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.user.Account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/user")
public class UserBean {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{id}")
    public Response getAccount(@PathParam("id") int id) {
        try {
            Account acc = accountDAO.find(id);
            return Response.status(Response.Status.FOUND).entity(acc).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/name/{name}")
    public Response getAccountByUsername(@PathParam("name") String name) {
        try {
            Account acc = accountDAO.findByUsername(name);
            return Response.status(Response.Status.FOUND).entity(acc).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/email/{email}")
    public Response getAccountByEmail(@PathParam("email") String email) {
        try {
            Account acc = accountDAO.findByEmail(email);
            return Response.status(Response.Status.FOUND).entity(email).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }
}
