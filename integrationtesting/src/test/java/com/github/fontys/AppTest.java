package com.github.fontys;

import static com.jayway.restassured.RestAssured.given;

import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppTest {

    @BeforeClass
    public static void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
//        RestAssured.basePath = "/kwetter/";
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void newAccountsTest() {
        given().when().get("http://www.google.com").then().statusCode(200);
    }
}