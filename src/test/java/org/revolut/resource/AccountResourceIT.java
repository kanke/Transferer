package org.revolut.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.restassured.response.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.revolut.dto.AccountDto;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountResourceIT extends BaseResourceTest {

    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCOUNT_ENDPOINT = "/account";
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String testAccountPayload;
    private String testTwoAccountPayload;
    private String testThreeAccountPayload;

    {
        try {
            testAccountPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/testAccount.json"), AccountDto.class));
            testTwoAccountPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/testTwoAccount.json"), AccountDto.class));
            testThreeAccountPayload = MAPPER.writeValueAsString(
                    MAPPER.readValue(fixture("fixtures/testThreeAccount.json"), AccountDto.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldCreateBankAccount() {
        Response response = given()
                .contentType(APPLICATION_JSON)
                .body(testAccountPayload)
                .when()
                .post(ACCOUNT_ENDPOINT)
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.CREATED_201, response.getStatusCode());
    }

    @Test
    public void shouldNotCreateBankAccountDueToDuplicate() {
        given()
                .contentType(APPLICATION_JSON)
                .body(testTwoAccountPayload)
                .when()
                .post(ACCOUNT_ENDPOINT)
                .then()
                .statusCode(201);

        Response response = given()
                .contentType(APPLICATION_JSON)
                .body(testThreeAccountPayload)
                .when()
                .post(ACCOUNT_ENDPOINT)
                .then()
                .extract()
                .response();


        assertTrue(response.body().asString().contains("There was an error processing your request."));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatusCode());
    }
}
