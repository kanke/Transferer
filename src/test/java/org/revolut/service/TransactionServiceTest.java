package org.revolut.service;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dao.AccountDao;
import org.revolut.dao.TransactionDao;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.model.Account;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private AccountService accountService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Account fromAccount;
    private Account toAccount;
    private AccountDao accountDao;

    @Before
    public void setUp() throws Exception {
        accountDao = mock(AccountDao.class);

        fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100.00));
        fromAccount.setCurrencyCode("GBP");
        fromAccount.setAccountNumber(303030);
        fromAccount.setAccountId(1l);

        toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");
        toAccount.setAccountNumber(202020);
        toAccount.setAccountId(2l);

        when(accountService.getAccount(toAccount.getAccountId())).thenReturn(toAccount);
    }

    @Test
    @DisplayName("Should transfer funds from one account to another")
    public void shouldTransferFundsWithoutErrors() throws AccountException, TransactionException {

        when(accountService.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();

        long transactionId = transactionService.transferFunds(accountTransactionDto);

        verify(accountService, atLeastOnce()).getAccount(fromAccount.getAccountId());
        verify(transactionDao, times(1)).addTransaction(any());
        Assert.assertEquals(0, transactionId);
        Assert.assertEquals(BigDecimal.valueOf(70.0), accountService.getAccount(toAccount.getAccountId()).getBalance());
    }

    @Test
    @DisplayName("Should throw an exception when receiving account is inactive")
    public void shouldNotTransferFundsWithInActiveStatus() throws AccountException, TransactionException {

        when(accountService.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        exceptionRule.expect(TransactionException.class);
        exceptionRule.expectMessage("The sending account with id 1 or receiving account with id 2 is inactive");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        toAccount.setStatus(Account.Status.INACTIVE);

        transactionService.transferFunds(accountTransactionDto);

        //should throw TransactionException
        verifyNoInteractions(transactionDao.addTransaction(any()));
    }

    @Test
    @DisplayName("Should throw an exception with currency accounts")
    public void shouldNotTransferFundsWithDifferentCurrencies() throws AccountException, TransactionException {

        when(accountService.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        exceptionRule.expect(TransactionException.class);
        exceptionRule.expectMessage("Different currency transfer not supported, Sending account is in GBP and receiving account is in NGN");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        toAccount.setCurrencyCode("NGN");

        transactionService.transferFunds(accountTransactionDto);

        //should throw TransactionException
        verifyNoInteractions(transactionDao.addTransaction(any()));
    }

    @Test
    @DisplayName("Should not transfer funds with missing account")
    public void shouldNotTransferFundsWithMissingAccount() throws AccountException, TransactionException {

        exceptionRule.expect(Exception.class);

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();

        transactionService.transferFunds(accountTransactionDto);

        verify(transactionDao, times(0)).addTransaction(any());
    }

    @Test
    @DisplayName("Should not transfer funds with insufficient balance from sending account")
    public void shouldNotTransferFundsWithInsufficientBalance() throws AccountException, TransactionException {

        when(accountService.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        //exceptionRule.expect(AccountException.class);
        //exceptionRule.expectMessage("Insufficient balance in account with id %s");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20000.00))
                .build();

        transactionService.transferFunds(accountTransactionDto);

        //should throw TransactionException
        //verify(transactionDao, times(0)).addTransaction(any());
    }

}
