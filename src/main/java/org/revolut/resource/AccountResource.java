package org.revolut.resource;

import com.google.inject.Inject;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.response.StandardResponse;
import org.revolut.service.impl.AccountServiceImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {
    private AccountServiceImpl accountServiceImpl;

    @Inject
    public AccountResource(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDto accountDto) throws AccountException {
        long accountId = accountService.createAccount(accountDto);
        return Response.status(HttpStatus.CREATED_201).entity(accountId).build();
    }
}
