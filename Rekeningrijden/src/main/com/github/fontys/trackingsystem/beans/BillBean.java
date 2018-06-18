package com.github.fontys.trackingsystem.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.entities.payment.Bill;
import com.github.fontys.trackingsystem.services.interfaces.BillService;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//end points
//	/api/bills/{year}/{month}
//	GET facturen
//		- GIVE
//			string maand
//			string jaar
//		+ RETURN
//			{ factuurnummer, nummerplaat, totaalbedrag, status, maand }


//	/api/bills/
//	GET facturen
//		- GIVE (FORM PARAMS)
//			string start maand + jaar (startdate)
//			string end maand + jaar (enddate)
//		+ RETURN
//			{billnr, licenseplate, price, status, month}

@Stateful
@Path("/bills")
public class BillBean {

    @Inject
    private BillService billService;

    /**
     * Gets bills by a specific date
     * Only returns the bills the user is allowed to see
     * @param year
     * @param month
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{year}/{month}")
    public List<Bill> getBillByTime(@PathParam("year") int year, @PathParam("month") int month) {
        return billService.getBillsByTime(year, month);
    }

    /**
     * Returns all bills the user is allowed to see
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/all")
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    /**
     * Updates a bill status if the user has the right rights
     * - CUSTOMER: own bills
     * - BILL_ADMINISTRATOR: all
     * @param id
     * @param status
     * @return
     */
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/{id}")
    public Bill setBillStatus(@PathParam("id") int id, @QueryParam("status") String status) {
        return billService.setBillStatus(id, status);
    }

    /**
     * Returns a bill of an owner if there's rights
     * @param ownerId
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/user/{ownerId}")
    public List<Bill> getBillByOwnerId(@PathParam("ownerId") int ownerId) {
        return billService.getBillsByOwnerId(ownerId);
    }

    /**
     * Gets all the bills of a vehicle if the owner owns this vehicle
     * @param vehicleId
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/vehicle/{vehicleId}")
    public List<Bill> getBillsByVehicleId(@PathParam("vehicleId") int vehicleId) {
        return billService.getBillsByVehicleId(vehicleId);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/status/{status}")
    public List<Bill> getBillsByStatus(@PathParam("status") String status) {
        return billService.getBillsByStatus(status);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/{id}")
    public Bill getBillByID(@PathParam("id") int id) {
        return billService.getBillByID(id);
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/currentUser")
    public List<Bill> getCurrentUserBills() {
        return billService.getCurrentUserBills();
    }

    @GET
    //@EasySecurity(requiresUser = true) TODO: uncomment
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/rates/{id}")
    public List<Rate> getBillRates(@PathParam("id") long id) {
        return billService.getRates(id);
    }

}
