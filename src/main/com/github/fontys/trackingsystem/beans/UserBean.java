package com.github.fontys.trackingsystem.beans;

import com.github.fontys.services.EmailService;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.UserDAO;
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
    private AccountDAO accountDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private EmailService emailService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{id}")
    public Response getAccount(@PathParam("id") int id) {
        try {
            Account acc = accountDAO.find(id);
            return Response.status(Response.Status.FOUND).entity(acc).build();
        } catch (Exception e) {
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
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/email/{email}")
    public Response getAccountByEmail(@PathParam("email") String email) {
        try {
            Account acc = accountDAO.findByEmail(email);
            return Response.status(Response.Status.FOUND).entity(acc).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        try {
            User user = userDAO.find(id);
            return Response.status(Response.Status.FOUND).entity(user).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/verify/{token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmRegistration(@PathParam("token") String token){
        try{
            if(!userDAO.verificationLinkExists(token)){
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }

            if(userDAO.hasBeenVerified(token)){
                return Response.status(Response.Status.CONFLICT).entity("You have already verified your account").build();
            }

            User user = userDAO.findByVerificationLink(token);
            if(user == null){
                return Response.serverError().build();
            }
            user.setVerified(true);
            userDAO.edit(user);

            return Response.ok("Account has been verified").build();

        }catch(Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response createCustomer(@FormParam("name") String name,
                                    @FormParam("address") String address,
                                    @FormParam("residency") String residency,
                                    @FormParam("email") String email,
                                    @FormParam("username") String username,
                                    @FormParam("password") String password) {
        Account acc;
        try {
            acc = accountDAO.findByEmail(email);
            if (acc != null) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        } catch (Exception e) {
            if (!(e instanceof EJBException)) {
                return Response.serverError().entity(e).build();
            }
        }

        User user = new User(name, address, residency, Role.CUSTOMER);
        Account account = new Account(email, username, password);
        account.setUser(user);

        try {
            accountDAO.create(account);
            Account userAccount = accountDAO.findByEmail(account.getEmail());
            User createdUser = userDAO.findByAccount(userAccount);
            createdUser.setVerifyLink(emailService.generateVerificationLink(createdUser));
            userDAO.edit(createdUser);
            emailService.sendVerificationMail(userAccount);
            return Response.ok(createdUser).build();
        } catch (Exception e) { //Expects a NoResultException but that is hidden in EJBException
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }
}
