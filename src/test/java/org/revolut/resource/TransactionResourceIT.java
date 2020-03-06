package org.revolut.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.revolut.dropwizard.App;
import org.revolut.dto.AccountDto;
import org.revolut.dto.AccountTransactionDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import static io.dropwizard.testing.FixtureHelpers.fixture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionResourceIT {

    private static final String TRANSFER_ENDPOINT = "/transfer";
    private static final String ACCOUNT_ENDPOINT = "/account";
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String fromAccountPayload;
    private String toAccountPayload;
    private String transactionPayload;
    private String transactionPayloadFail;
    Client client;

    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE =
            new DropwizardAppRule<>(App.class);

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
        client = new JerseyClientBuilder().build();

        createBankAccount(fromAccountPayload);
        createBankAccount(toAccountPayload);
    }

    @Test
    public void shouldTransferFunds() {

        Response resp = getClient(TRANSFER_ENDPOINT)
                .post(Entity.entity(transactionPayload, MediaType.APPLICATION_JSON));

        assertEquals("1", resp.readEntity(String.class));
        assertEquals(HttpStatus.OK_200, resp.getStatus());
    }

    @Test
    public void shouldNotTransferFundsDueToInsufficientBalance() {

        Response resp = getClient(TRANSFER_ENDPOINT)
                .post(Entity.entity(transactionPayloadFail, MediaType.APPLICATION_JSON));

        assertTrue(resp.readEntity(String.class).contains("There was an error processing your request."));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, resp.getStatus());
    }

    public String createBankAccount(String payload) {
        Response resp = getClient(ACCOUNT_ENDPOINT)
                .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        return resp.readEntity(String.class);
    }

    private Invocation.Builder getClient(String path) {
        return client.target(
                String.format("http://localhost:%d" + path, RULE.getLocalPort())).request(MediaType.APPLICATION_JSON);
    }
}
