package org.revolut.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dao.TransactionDao;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.model.Account;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private AccountService accountService;

    @Test
    @DisplayName("Should throw an exception when status code is not OK")
    public void shouldTransferFundsWithoutErrors() throws AccountException, TransactionException {

        Account toAccount= new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(1)
                .debitAccountId(2)
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();

        Mockito.when(accountService.getAccount(1L)).thenReturn(toAccount);
        Mockito.when(accountService.getAccount(2L)).thenReturn(toAccount);

        verify(transactionDao, times(1));
        long transactionId = transactionService.transferFunds(accountTransactionDto);
        System.out.println("transactionId " +transactionId);
    }
}
