package org.revolut.resource;

import com.google.inject.Inject;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.AccountTransactionException;
import org.revolut.service.impl.AccountTransactionServiceImpl;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private AccountTransactionServiceImpl accountTransactionServiceImpl;

    @Inject
    public TransactionResource(AccountTransactionServiceImpl accountTransactionServiceImpl) {
        this.accountTransactionServiceImpl = accountTransactionServiceImpl;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds(@Valid AccountTransactionDto accountTransaction){
        try {
            accountTransactionServiceImpl.transferFunds(accountTransaction);
        } catch (AccountTransactionException accountTransactionException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(accountTransactionException.getMessage())
                    .build();
        } catch (AccountException accountException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(accountException.getMessage())
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity("Transaction created successfully")
                .build();
    }
}
