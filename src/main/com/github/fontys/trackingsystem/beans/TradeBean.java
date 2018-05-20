package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.trackingsystem.services.interfaces.TradeService;
import com.github.fontys.trackingsystem.transfer.Transfer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

@RequestScoped
@Path("trade")
public class TradeBean {

    @Inject
    private TradeService tradeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/transfers/vehicle/{id}")
    public List<Transfer> getTransfersForVehicle(@PathParam("id") long id) {
        return tradeService.getTransfersVehicle(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/transfers/to/{id}")
    public List<Transfer> getTransfersToUser(@PathParam("id") long id) {
        return tradeService.getTransfersToUser(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/transfers/from/{id}")
    public List<Transfer> getTransfersFromUser(@PathParam("id") long id) {
        return tradeService.getTransfersFromUser(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/transfers/from")
    public List<Transfer> getTransfersFromCurrentUser() {
        return tradeService.getTransfersFromCurrentUser();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/transfers/to")
    public List<Transfer> getTransfersToCurrentUser() {
        return tradeService.getTransfersToCurrentUser();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/accept/new/")
    public Transfer acceptTransferNewOwner(@FormParam("id") long transferId) {
        return tradeService.acceptTransferNewOwner(transferId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/accept/current/")
    public Transfer acceptTransferCurrentOwner(@FormParam("id") long transferId) {
        return tradeService.acceptTransferCurrentOwner(transferId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/decline/new/")
    public Transfer declineTransferNewOwner(@FormParam("id") long transferId) {
        return tradeService.declineTransferNewOwner(transferId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/decline/current/")
    public Transfer declineTransferCurrentOwner(@FormParam("id") long transferId) {
        return tradeService.declineTransferCurrentOwner(transferId);
    }

    @POST
    @Path("/confirm")
    @EasySecurity(requiresUser = true)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Transfer confirmOwnership(@FormDataParam("transfer") long transferID,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
        return tradeService.confirmOwnership(transferID, uploadedInputStream, fileDetails);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/complete")
    public Transfer complete(@FormParam("id") long transferId) {
        return tradeService.completeTransfer(transferId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/create")
    public Transfer createTransfer(@FormParam("license") String license,
                                   @FormParam("email") String email) {
        return tradeService.createTransfer(license, email);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @EasySecurity(requiresUser = true)
    @Path("/token")
    public Transfer acceptTokenAlreadyLoggedIn(@FormParam("token") String token) {
        return tradeService.acceptTokenAlreadyLoggedIn(token);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Transfer acceptTokenLogin(@FormParam("token") String token,
                                     @FormParam("email") String email,
                                     @FormParam("password") String password,
                                     @Context HttpServletRequest req) {
        return tradeService.acceptTokenLogin(token, email, password, req);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Transfer acceptTokenRegister(@FormParam("token") String token,
                                        @FormParam("name") String name,
                                        @FormParam("address") String address,
                                        @FormParam("residency") String residency,
                                        @FormParam("email") String email,
                                        @FormParam("username") String username,
                                        @FormParam("password") String password,
                                        @Context HttpServletRequest req) {
        return tradeService.acceptTokenRegister(token, name, address, residency, email, username, password, req);
    }
}
