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
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("/bills")
public class BillBean {
    @Inject
    private DatabaseMock db;

    @GET
    @Path("/{year}/{month}")
    public Response getVehicle(@PathParam("year") int year, @PathParam("month") int month) {
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
    @Path("/all")
    public Response getVehicle() {
        System.out.println("Result is false");
        return Response.ok(db.getBills()).build();
    }

    private boolean compareYearAndMonth(Bill b, int year, int month) {
        System.out.println(String.format("Comparing year %s month %s", year, month));
        // Check if start date is within the same year/month
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(b.getStartDate());
        if (cal1.get(Calendar.YEAR) == year &&
                cal1.get(Calendar.MONTH) == month) {
            return true;
        }
        System.out.println(String.format("Calendar year %s month %s", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)));

        // Check if end date is within the same year/month
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(b.getEndDate());
        if (cal2.get(Calendar.YEAR) == year &&
                cal2.get(Calendar.MONTH) == month) {
            return true;
        }

        // Neither are within the same year/month, return false
        return false;
    }
}
