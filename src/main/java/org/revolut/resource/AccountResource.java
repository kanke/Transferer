package org.revolut.resource;

import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.service.AccountService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountResource {

    private AccountService accountService;
    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDto accountDto) throws AccountException {
        long accountId = accountService.createAccount(accountDto);
        return Response.status(HttpStatus.CREATED_201).entity(accountId).build();
    }
}
