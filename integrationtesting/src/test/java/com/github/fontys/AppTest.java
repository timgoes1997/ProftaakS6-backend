package com.github.fontys;

import static com.github.fontys.BasicLocationTrackerTest.ARRIVAL_URL;
import static com.github.fontys.BasicLocationTrackerTest.LOCATION_URL;
import static com.github.fontys.BasicLocationTrackerTest.post;
import static com.jayway.restassured.RestAssured.given;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.*;
import java.util.logging.Logger;

import com.biepbot.stress.StressRunner;
import com.biepbot.stress.StressTest;
import okhttp3.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class AppTest {

    private static JsonParser parser;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = Globals.BASE_URL;
        RestAssured.port = Globals.SERVER_PORT;
        RestAssured.basePath = Globals.API_PATH;
        parser = new JsonParser();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void simulateForeignRoute1() throws IOException, InterruptedException {
        Date startDate = Calendar.getInstance().getTime();

        String license = "XXX-001";

        sendLocation(51.412513, 6.475905, license); // Germany
        sendLocation(51.410221, 6.425278, license); // Germany
        sendLocation(51.400221, 6.325278, license); // Germany
        sendLocation(51.388004, 6.308915, license); // Germany

        sendLocation(51.389580, 6.174433, license); // Netherlands
        sendLocation(51.397853, 6.146652, license); // Netherlands
        sendLocation(51.432899, 6.135287, license); // Netherlands
        sendLocation(51.460444, 6.151072, license); // Netherlands
        sendLocation(51.478929, 6.202844, license); // Netherlands

        sendLocation(51.477356, 6.256511, license); // Germany
        sendLocation(51.459657, 6.285554, license); // Germany
        sendLocation(51.421483, 6.330382, license); // Germany
        sendLocation(51.412513, 6.475905, license); // Germany

        Date endDate = Calendar.getInstance().getTime();

        sendArrival(startDate, endDate, license);
    }

    @Test
    public void simulateDomesticRoute() throws IOException, InterruptedException {
        Date startDate = Calendar.getInstance().getTime();

        String license = "XXX-001";

        sendLocation(51.506457, 7.404453, license); // Germany
        sendLocation(51.506457, 7.294590, license); // Germany
        sendLocation(51.511585, 7.242405, license); // Germany
        sendLocation(51.547468, 7.209446, license); // Germany
        sendLocation(51.600168, 7.180321, license); // Germany
        sendLocation(51.686056, 7.157029, license); // Germany
        sendLocation(51.719976, 7.150062, license); // Germany
        sendLocation(51.734154, 7.161009, license); // Germany
        sendLocation(51.740933, 7.232661, license); // Germany
        sendLocation(51.734154, 7.311280, license); // Germany
        sendLocation(51.760648, 7.383927, license); // Germany
        sendLocation(51.729223, 7.401840, license); // Germany
        sendLocation(51.694692, 7.373976, license); // Germany
        sendLocation(51.656431, 7.347106, license); // Germany
        sendLocation(51.605778, 7.318246, license); // Germany
        sendLocation(51.562367, 7.379491, license); // Germany
        sendLocation(51.525325, 7.396324, license); // Germany

        Date endDate = Calendar.getInstance().getTime();

        sendArrival(startDate, endDate, license);
    }

    @Test
    public void simulateForeignRoute3() throws IOException, InterruptedException {
        Date startDate = Calendar.getInstance().getTime();

        String license = "XXX-001";

        sendLocation(51.412513, 6.475905, license); // Germany

        sendLocation(51.389580, 6.174433, license); // Netherlands

        sendLocation(51.477356, 6.256511, license); // Germany

        Date endDate = Calendar.getInstance().getTime();

        sendArrival(startDate, endDate, license);
    }

    private void sendArrival(Date startDate, Date endDate, String license) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault()).parse(dateString);

        String startdateFormatted = df.format(startDate);

        formBuilder.add("startdate", startdateFormatted);
        formBuilder.add("enddate", df.format(endDate));
        formBuilder.add("license", license);

        post(ARRIVAL_URL, formBuilder.build());
    }

    private void sendLocation(double lat, double lon, String license) throws IOException, InterruptedException {
        Thread.sleep(2000);

        FormBody.Builder formBuilder = new FormBody.Builder();

        formBuilder.add("lon", String.valueOf(lon));
        formBuilder.add("lat", String.valueOf(lat));
        formBuilder.add("license", license);

        post(LOCATION_URL, formBuilder.build());
    }

    /**
     * Validates the integrity of a bill returned from the http response
     *
     * @param bill JsonObject representation of a bill
     */
    private void parseBillFromJsonObject(JsonObject bill) {
        bill.get("alreadyPaid").getAsInt();
        bill.get("billnr").getAsInt();
        bill.get("calendarEndDate").getAsString();
        bill.get("calendarStartDate").getAsString();
        bill.get("id").getAsInt();
        bill.get("id").getAsString();
        bill.get("mileage").getAsDouble();
        bill.get("month").getAsString();
        bill.get("numberEndDate").getAsLong();
        bill.get("numberStartDate").getAsLong();
        bill.get("price").getAsInt();

        // region Check registeredVehicle values
        JsonObject registeredVehicle = bill.get("registeredVehicle").getAsJsonObject();

        JsonObject customer = registeredVehicle.get("customer").getAsJsonObject();
        // region Check customer values
        customer.get("address").getAsString();
        customer.get("id").getAsInt();
        customer.get("name").getAsString();
        customer.get("privilege").getAsString();
        // note; there's an empty 'registeredVehicles' list here. Not checking that one.
        customer.get("residency").getAsString();
        customer.get("role").getAsString();
        // endregion

        registeredVehicle.get("id").getAsInt();
        registeredVehicle.get("licensePlate").getAsString();
        registeredVehicle.get("proofOfOwnership").getAsString();

        JsonObject vehicle = registeredVehicle.get("vehicle").getAsJsonObject();
        // region Check vehicle values
        vehicle.get("brand").getAsString();
        vehicle.get("buildDate").getAsString();
        vehicle.get("edition").getAsString();
        vehicle.get("energyLabel").getAsString();
        vehicle.get("fuelType").getAsString();
        vehicle.get("id").getAsString();
        vehicle.get("modelName").getAsString();
        // endregion
        // endregion

        bill.get("status").getAsString();
    }

    @Test
    public void getBillsByIdTest() {
        // Get a bill
        String body = given().get("bills/1").asString();

        // Parse body into JsonObject
        JsonObject bill = parser.parse(body).getAsJsonObject();

        // Try to get the expected fields
        try {
            parseBillFromJsonObject(bill);
        } catch (Exception e) {
            fail(String.format("Could not retrieve all expected fields from the http response.\nError: %s", e.getMessage()));
        }
    }

    @Test
    public void getBillsTest() {
        // Get all bills
        String body = given().get("bills/all").asString();

        // Parse body into JsonArray -- an array of bills
        JsonArray array = parser.parse(body).getAsJsonArray();

        // Check if we have at least one bill
        if (array.size() == 0) {
            fail("Size of array is 0. Did not get any bills from endpoint.");
        }

        // Get first bill, we'll validate the response using this object
        JsonObject bill = array.get(0).getAsJsonObject();

        // Try to get the expected fields
        try {
            parseBillFromJsonObject(bill);
        } catch (Exception e) {
            fail(String.format("Could not retrieve all expected fields from the http response.\nError: %s", e.getMessage()));
        }
    }

//    @Test
//    public void getRegisteredVehiclesTest() {
//        // Get all registered vehicles
//        String body = given().get("vehicles/registered").asString();
//
//        // Parse body into JsonArray -- an array of vehicles
//        JsonArray array = parser.parse(body).getAsJsonArray();
//
//        // Check if we have at least one vehicle
//        if (array.size() == 0) {
//            fail("Size of array is 0. Did not get any vehicles from endpoint.");
//        }
//
//        // Get first vehicle, we'll validate the response using this object
//        JsonObject vehicle = array.get(0).getAsJsonObject();
//
//        // Try to get the expected fields
//        try {
//            vehicle.get("id").getAsInt();
//            vehicle.get("licensePlate").getAsString();
//            vehicle.get("vehicle").getAsJsonObject();
//            vehicle.get("proofOfOwnership").getAsString();
//
//            JsonArray bills = vehicle.get("bills").getAsJsonArray();
//            JsonObject bill = bills.get(0).getAsJsonObject();
//            // region Check bills on registered vehicle
//            // Note: Licenseplate does not exist anymore
//            // bill.get("licenseplate").getAsString();
//            bill.get("startDate").getAsJsonObject();
//            bill.get("endDate").getAsJsonObject();
//            bill.get("paid").getAsInt();
//            bill.get("price").getAsInt();
//            bill.get("status").getAsInt();
//
//            JsonArray trips = bill.get("trips").getAsJsonArray();
//            JsonObject trip = trips.get(0).getAsJsonObject();
//            // region Trips
//            trip.get("region").getAsString();
//            trip.get("price").getAsInt();
//            trip.get("miles").getAsInt();
//            trip.get("date").getAsJsonObject();
//            trip.get("time_rate").getAsInt();
//            // endregion
//            // endregion
//        } catch (Exception e) {
//            fail(String.format("Could not retrieve all expected fields from the http response.\nError: %s", e.getMessage()));
//        }
//    }
}