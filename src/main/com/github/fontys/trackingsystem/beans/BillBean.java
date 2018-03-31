package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

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
    private DatabaseMock db;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{year}/{month}")
    public Response getBillByTime(@PathParam("year") int year, @PathParam("month") int month) {
        List<Bill> bills = new ArrayList<>();
        for (Bill b : db.getBills()) {
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
    @Path("/all")
    public Response getAllBills() {
        List<Bill> bills = db.getBills();
        GenericEntity<List<Bill>> list = new GenericEntity<List<Bill>>(bills) {
        };
        return Response.ok(list).build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getBillByID(@PathParam("id") int id, @QueryParam("status") String status) {
        List<Bill> bills = db.getBills();
        Bill result = null;
        for (Bill b : bills) {
            if (b.getId() == id) {
                result = b;
                break;
            }
        }

        if (result.getStatus() == PaymentStatus.PAID) {
            // mag niet naar open of cancelled gaan
            if (status.equals("cancelled") || status.equals("open")) {
                return Response.status(405).build();
            }
        } else if (result.getStatus() == PaymentStatus.CANCELED) {
            // mag niet naar open of cancelled gaan
            if (status.equals("open") || status.equals("cancelled")) {
                return Response.status(405).build();
            }
        }
        db.updateBillStatus(result, status);

        return Response.ok(result).build();
    }

    private boolean isAPaymentStatus(String test) {
        for (PaymentStatus c : PaymentStatus.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{status}")
    public Response getBillByStatus(@QueryParam("status") String status) {
        // Check if status is a valid status string
        if (!isAPaymentStatus(status)) {
            return Response.status(400).build();
        }

        List<Bill> bills = db.getBills();
        List<Bill> result = new ArrayList<>();
        for (Bill b : bills) {
            if (b.getStatus().toString().equals(status)) {
                result.add(b);
                break;
            }
        }

        // Check if there was any bill found
        if (result.size() == 0) {
            return Response.status(404).build();
        }

        // Good request, found results. Return results.
        return Response.ok(result).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getBillByID(@PathParam("id") int id) {
        List<Bill> bills = db.getBills();
        Bill result = null;
        for (Bill b : bills) {
            if (b.getId() == id) {
                result = b;
                break;
            }
        }
        return Response.ok(result).build();
    }

    private boolean compareYearAndMonth(Bill b, int year, int month) {
        System.out.println(String.format("Comparing year %s month %s", year, month));
        // Check if start date is within the same year/month
        Calendar cal1 = b.getStartDate();
        if (cal1.get(Calendar.YEAR) == year &&
                cal1.get(Calendar.MONTH) == month) {
            return true;
        }
        System.out.println(String.format("Calendar year %s month %s", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)));

        // Check if end date is within the same year/month
        Calendar cal2 = b.getEndDate();
        if (cal2.get(Calendar.YEAR) == year &&
                cal2.get(Calendar.MONTH) == month) {
            return true;
        }

        // Neither are within the same year/month, return false
        return false;
    }
}
