package com.github.fontys.trackingsystem.beans;

import com.github.fontys.trackingsystem.mock.DatabaseMock;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.vehicle.CustomerVehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
        GenericEntity<List<Bill>> list = new GenericEntity<List<Bill>>(bills) {};
        return Response.ok(list).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getBillByID(@PathParam("id") int id) {
        List<Bill> bills = db.getBills();
        Bill result = null;
        for (Bill b : bills) {
            if (b.getBillnr() == id) {
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
