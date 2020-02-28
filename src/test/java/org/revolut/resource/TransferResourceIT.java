package org.revolut.resource;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class TransferResourceIT {

    @Test
    public void shouldTransferFunds() {

        String firstTransfer = "{\n" +
                "\n" +
                "\t\"amount\": \"35.00\"\n" +
                "\t\"reference\": \"bribe\"\n" +
                "\t\"account\" {\n" +
                "\t\t\"number\": \"26262626\"\n" +
                "\t\t\"sortCode\": \"00 - 90 - 00\"\n" +
                "\t\t\"name\": \"bad person\"\n" +
                "\t}\n" +
                "}";

        Response resp = given()
                .body(firstTransfer)
                .post("/transfer").then()
                .extract()
                .response();

        final ResponseBody body = resp.getBody();
        assertEquals("Transfer Successful", body.path("message"));
    }

}
