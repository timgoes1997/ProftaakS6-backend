package com.github.fontys;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AppTest {

    private static JsonParser parser;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/Rekeningrijden/api/";
        parser = new JsonParser();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
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
        } catch (Exception e) {
            fail(String.format("Could not retrieve all expected fields from the http response.\nError: %s", e.getMessage()));
        }
    }

    @Test
    public void getRegisteredVehiclesTest() {
        // Get all registered vehicles
        String body = given().get("vehicles/registered").asString();

        // Parse body into JsonArray -- an array of vehicles
        JsonArray array = parser.parse(body).getAsJsonArray();

        // Check if we have at least one vehicle
        if (array.size() == 0) {
            fail("Size of array is 0. Did not get any vehicles from endpoint.");
        }

        // Get first vehicle, we'll validate the response using this object
        JsonObject vehicle = array.get(0).getAsJsonObject();

        // Try to get the expected fields
        try {
            vehicle.get("id").getAsInt();
            vehicle.get("licensePlate").getAsString();
            vehicle.get("vehicle").getAsJsonObject();
            vehicle.get("proofOfOwnership").getAsString();

            JsonArray bills = vehicle.get("bills").getAsJsonArray();
            JsonObject bill = bills.get(0).getAsJsonObject();
            // region Check bills on registered vehicle
            // Note: Licenseplate does not exist anymore
            // bill.get("licenseplate").getAsString();
            bill.get("startDate").getAsJsonObject();
            bill.get("endDate").getAsJsonObject();
            bill.get("paid").getAsInt();
            bill.get("price").getAsInt();
            bill.get("status").getAsInt();

            JsonArray trips = bill.get("trips").getAsJsonArray();
            JsonObject trip = trips.get(0).getAsJsonObject();
            // region Trips
            trip.get("region").getAsString();
            trip.get("price").getAsInt();
            trip.get("miles").getAsInt();
            trip.get("date").getAsJsonObject();
            trip.get("time_rate").getAsInt();
            // endregion
            // endregion
        } catch (Exception e) {
            fail(String.format("Could not retrieve all expected fields from the http response.\nError: %s", e.getMessage()));
        }
    }
}