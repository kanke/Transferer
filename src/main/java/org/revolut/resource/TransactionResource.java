package org.revolut.resource;

import com.google.inject.Inject;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.response.StandardResponse;
import org.revolut.service.impl.TransactionServiceImpl;

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
    private TransactionServiceImpl transactionServiceImpl;

    @Inject
    public TransactionResource(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds(@Valid AccountTransactionDto accountTransaction) throws AccountException, TransactionException {
        transactionServiceImpl.transferFunds(accountTransaction);
        return Response.ok(new StandardResponse(StandardResponse.SUCCESS, "Transaction created successfully")).build();
    }
}
