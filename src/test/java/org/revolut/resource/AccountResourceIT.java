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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountResourceIT {

    private static final String ACCOUNT_ENDPOINT = "/account";
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String testAccountPayload;
    private String testTwoAccountPayload;
    private String testThreeAccountPayload;
    Client client;

    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE =
            new DropwizardAppRule<>(App.class);

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

    @Before
    public void setUp() {
        client = new JerseyClientBuilder().build();
    }

    @Test
    public void shouldCreateBankAccount() {
        Response resp = getClient(ACCOUNT_ENDPOINT)
                .post(Entity.entity(testAccountPayload, MediaType.APPLICATION_JSON));

        assertEquals("Account with id 1 created successfully", resp.readEntity(String.class));
        assertEquals(HttpStatus.CREATED_201, resp.getStatus());
    }

    @Test
    public void shouldNotCreateBankAccountDueToDuplicate() {
        getClient(ACCOUNT_ENDPOINT)
                .post(Entity.entity(testTwoAccountPayload, MediaType.APPLICATION_JSON));

        Response resp = getClient(ACCOUNT_ENDPOINT)
                .post(Entity.entity(testThreeAccountPayload, MediaType.APPLICATION_JSON));

        assertTrue(resp.readEntity(String.class).contains("An account already exist for test4 in currency AUD"));
        assertEquals(HttpStatus.BAD_REQUEST_400, resp.getStatus());
    }

    private Invocation.Builder getClient(String path) {
        return client.target(
                String.format("http://localhost:%d" + path, RULE.getLocalPort())).request(MediaType.APPLICATION_JSON);
    }
}
