package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.services.interfaces.UserService;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    @Produces(MediaType.APPLICATION_JSON)
    public User confirmRegistration(@PathParam("token") String token){
        return userService.confirmRegistration(token);
    }
    
    @POST
    @Path("/recovery/create")
    @Produces(MediaType.APPLICATION_JSON)
    public void createRecoveryLink(@FormParam("email") String email){
        userService.recoverPassword(email);
    }

    @POST
    @Path("/recovery/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public User resetPassword(@FormParam("email") String email,
                              @FormParam("password") String password,
                              @FormParam("link") String link){
        return userService.resetPassword(password, link);
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/edit")
    public User edit(
            @FormParam("email") String email,
            @FormParam("address") String address,
            @FormParam("residency") String residency,
            @FormParam("department") String department) {
        return userService.edit(email, address, residency, department);
    }

}
