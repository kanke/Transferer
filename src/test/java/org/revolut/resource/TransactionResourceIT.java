package org.revolut.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.revolut.dto.AccountDto;
import org.revolut.dto.AccountTransactionDto;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionResourceIT extends BaseResourceTest {

    private static final String APPLICATION_JSON = "application/json";
    private static final String TRANSFER_ENDPOINT = "/transfer";
    private static final String ACCOUNT_ENDPOINT = "/account";
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String fromAccountPayload;
    private String toAccountPayload;
    private String transactionPayload;
    private String transactionPayloadFail;

    {
        try {
            fromAccountPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/toAccount.json"), AccountDto.class));
            toAccountPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/fromAccount.json"), AccountDto.class));
            transactionPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/transactionBody.json"), AccountTransactionDto.class));
            transactionPayloadFail = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/transactionBodyInsufficient.json"), AccountTransactionDto.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        createBankAccount(fromAccountPayload);
        createBankAccount(toAccountPayload);
    }

    @Test
    public void shouldTransferFunds() {
        Response resp = given()
                .contentType(APPLICATION_JSON)
                .body(transactionPayload)
                .post("/transfer")
                .then()
                .extract()
                .response();

        assertEquals("1", resp.body().asString());
        assertEquals(HttpStatus.OK_200, resp.getStatusCode());
    }

    @Test
    public void shouldNotTransferFundsDueToInsufficientBalance() {

        Response resp = given()
                .contentType(APPLICATION_JSON)
                .body(transactionPayloadFail)
                .post(TRANSFER_ENDPOINT)
                .then()
                .extract()
                .response();

        assertTrue(resp.body().asString().contains("There was an error processing your request."));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, resp.getStatusCode());
    }

    public String createBankAccount(String payload) {
        Response response = given()
                .contentType(APPLICATION_JSON)
                .body(payload)
                .when()
                .post(ACCOUNT_ENDPOINT)
                .then()
                .extract()
                .response();
        return response.getBody().asString();
    }
}
