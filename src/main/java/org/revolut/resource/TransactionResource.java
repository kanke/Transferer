package org.revolut.resource;

import com.google.inject.Inject;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.service.TransactionService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private TransactionService transactionService;

    @Inject
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds(AccountTransactionDto accountTransaction){
        try {
            transactionService.transferFunds(accountTransaction);
        } catch (TransactionException transactionException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(transactionException.getMessage())
                    .build();
        } catch (AccountException accountException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(accountException.getMessage())
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity("Transaction created successfully")
                .build();
    }
}
