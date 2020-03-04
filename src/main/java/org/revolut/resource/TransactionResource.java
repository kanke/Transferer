package org.revolut.resource;

import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.service.TransactionService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
public class TransactionResource {

//    TransactionDao transactionDao = new TransactionDao();
//    AccountDao accountDao = new AccountDao();
//    AccountService accountService = new AccountService(accountDao);
//
//    private TransactionService transactionService = new TransactionService(accountService, transactionDao);

    private TransactionService transactionService;

    @Inject
    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds(AccountTransactionDto accountTransaction) throws AccountException, TransactionException {
        System.out.println("accountTransaction " + accountTransaction);
        long transactionId = transactionService.transferFunds(accountTransaction);
        return Response.status(HttpStatus.OK_200).entity(transactionId).build();
    }
}
