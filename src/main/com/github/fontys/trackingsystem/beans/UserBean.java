package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.services.EmailService;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
import com.github.fontys.trackingsystem.services.interfaces.UserService;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/users")
public class UserBean {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{id}")
    public Account getAccount(@PathParam("id") int id) {
        return userService.getAccount(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/name/{name}")
    public Account getAccountByUsername(@PathParam("name") String name) {
        return userService.getAccountByUsername(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/email/{email}")
    public Account getAccountByEmail(@PathParam("email") String email) {
        return userService.getAccountByEmail(email);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public User getUser(@PathParam("id") int id) {
        return userService.getUser(id);
    }

    @GET
    @Path("/verify/{token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User confirmRegistration(@PathParam("token") String token){
        return userService.confirmRegistration(token);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public User createCustomer(@FormParam("name") String name,
                                    @FormParam("address") String address,
                                    @FormParam("residency") String residency,
                                    @FormParam("email") String email,
                                    @FormParam("username") String username,
                                    @FormParam("password") String password) {
        return userService.createCustomer(name, address, residency, email, username, password);
    }
}
