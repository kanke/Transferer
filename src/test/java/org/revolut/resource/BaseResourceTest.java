package org.revolut.resource;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.*;

public class BaseResourceTest {

    @BeforeClass
    public static void setup() {
        String port = System.getProperty("server.port");
        if (port == null) {
            RestAssured.port = Integer.valueOf(8080);
        } else {
            RestAssured.port = Integer.valueOf(port);
        }

        String baseHost = System.getProperty("server.host");
        if (baseHost == null) {
            baseHost = "http://localhost";
        }
        baseURI = baseHost;

    }
}
