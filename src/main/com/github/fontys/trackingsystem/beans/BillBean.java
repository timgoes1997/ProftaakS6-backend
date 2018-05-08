package com.github.fontys.trackingsystem.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.annotations.interceptors.EasySecurity;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.DummyDataGenerator;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
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

@RequestScoped
@Path("/bills")
public class BillBean {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private DummyDataGenerator db;

    @Inject
    private BillDAO billDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private VehicleDAO vehicleDAO;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{year}/{month}")
    public Response getBillByTime(@PathParam("year") int year, @PathParam("month") int month) {
        List<Bill> bills = new ArrayList<>();
        for (Bill b : billDAO.getAll()) {
            boolean result = compareYearAndMonth(b, year, month - 1);
            if (result) {
                bills.add(b);
            }
        }
        if (bills.size() > 0) {
            return Response.ok(bills).build();
        }
        return Response.serverError().build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @EasySecurity(requiresUser = true)
    @Path("/all")
    public Response getAllBills() {

        List<Bill> bills;

        // RETURN OWN BILLS FOR;
        // CUSTOMERS
        if (currentUser.getPrivilege() == Role.CUSTOMER) {
            bills = billDAO.findByOwnerId(((User) currentUser).getId());
        } else {
            // FOR ANY OTHER ROLE, RETURN ALL
            bills = billDAO.getAll();
        }

        GenericEntity<List<Bill>> list = new GenericEntity<List<Bill>>(bills) {
        };
        return Response.ok(list).build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response setBillStatus(@PathParam("id") int id, @QueryParam("status") String status) {
        // Get single bill by id
        Bill b = billDAO.find(id);
        if (b == null) {
            // Bill not found, return 404 according to swagger doc
            return Response.status(404).build();
        }

        if (b.getStatus() == PaymentStatus.PAID) {
            // mag niet naar open of cancelled gaan
            if (status.equals("cancelled") || status.equals("open")) {
                return Response.status(405).build();
            }
        } else if (b.getStatus() == PaymentStatus.CANCELED) {
            // mag niet naar open of cancelled gaan
            if (status.equals("open") || status.equals("cancelled")) {
                return Response.status(405).build();
            }
        }

        // Set new status
        b.setStatus(getPaymentStatusByString(status));

        // Update in db
        billDAO.edit(b);

        return Response.ok(b).build();
    }

    private PaymentStatus getPaymentStatusByString(String paymentStatus) {
        for (PaymentStatus c : PaymentStatus.values()) {
            if (c.name().equals(paymentStatus)) {
                return c;
            }
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/user/{ownerId}")
    public Response getBillByOwnerId(@PathParam("ownerId") int ownerId) {
        // Check if owner exists
        Account a = accountDAO.find(ownerId);
        if (a == null) {
            // According to swagger endpoint, return 400 because of invalid owner id
            return Response.status(400).build();
        }

        // Owner exists, get bills for owner
        List<Bill> bills = billDAO.findByOwnerId((long) ownerId);
        GenericEntity<List<Bill>> list = new GenericEntity<List<Bill>>(bills) {
        };
        return Response.ok(list).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/vehicle/{vehicleId}")
    public Response getBillsByVehicleId(@PathParam("vehicleId") int vehicleId) {
        // Check if owner exists
        Vehicle v = vehicleDAO.find(vehicleId);
        if (v == null) {
            // According to swagger endpoint, return 400 because of invalid vehicle id
            return Response.status(400).build();
        }

        // Owner exists, get bills for owner
        List<Bill> bills = billDAO.findByVehicleId(vehicleId);
        if (bills.size() > 0) {
            // We have more than 0 bills, return status 200 with bills
            GenericEntity<List<Bill>> list = new GenericEntity<List<Bill>>(bills) {
            };
            return Response.ok(list).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/status/{status}")
    public Response getBillByStatus(@PathParam("status") String status) {
        // Check if status is a valid status string
        if (getPaymentStatusByString(status) != null) {
            return Response.status(400).build();
        }

        // Owner exists, get bills for owner
        List<Bill> bills = billDAO.findByStatus(status);
        if (bills.size() > 0) {
            // We have more than 0 bills, return status 200 with bills
            return Response.ok(bills).build();
        } else {
            // Return 404 according to swagger endpoint documentation
            return Response.status(404).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getBillByID(@PathParam("id") int id) {
        // Get single bill by id
        Bill b = billDAO.find(id);
        if (b == null) {
            // Bill not found, return 404 according to swagger doc
            return Response.status(404).build();
        }

        // Bill found, return success
        return Response.ok(b).build();
    }

    @GET
    @EasySecurity(requiresUser = true)
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/currentUser")
    public Response getCurrentUserBills() {
        User casted = (User) currentUser;

        List<Bill> bills = billDAO.findByOwnerId(casted.getId());
        GenericEntity<List<Bill>> listje = new GenericEntity<List<Bill>>(bills) {
        };

        return Response.ok(listje).build();
    }

    private boolean compareYearAndMonth(Bill b, int year, int month) {
        System.out.println(String.format("Comparing year %s month %s", year, month));
        // Check if start date is within the same year/month
        Calendar cal1 = b.getCalendarStartDate();
        if (cal1.get(Calendar.YEAR) == year &&
                cal1.get(Calendar.MONTH) == month) {
            return true;
        }
        System.out.println(String.format("Calendar year %s month %s", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)));

        // Check if end date is within the same year/month
        Calendar cal2 = b.getCalendarEndDate();
        if (cal2.get(Calendar.YEAR) == year &&
                cal2.get(Calendar.MONTH) == month) {
            return true;
        }

        // Neither are within the same year/month, return false
        return false;
    }
}
