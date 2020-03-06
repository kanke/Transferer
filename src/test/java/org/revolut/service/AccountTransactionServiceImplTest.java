package org.revolut.service;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.AccountTransactionException;
import org.revolut.model.Account;
import org.revolut.service.impl.AccountServiceImpl;
import org.revolut.service.impl.AccountTransactionServiceImpl;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountTransactionServiceImplTest {

    @Mock
    private AccountServiceImpl accountServiceImpl;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Account fromAccount;
    private Account toAccount;
    private AccountTransactionServiceImpl accountTransactionServiceImpl;

    @Before
    public void setUp() throws AccountException {

        accountTransactionServiceImpl = new AccountTransactionServiceImpl(accountServiceImpl);

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

        when(accountServiceImpl.getAccount(toAccount.getAccountId())).thenReturn(toAccount);
    }

    @Test
    @DisplayName("Should transfer funds from one account to another")
    public void shouldTransferFundsWithoutErrors() throws AccountException, AccountTransactionException {

        when(accountServiceImpl.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();

        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        verify(accountServiceImpl, atLeastOnce()).getAccount(fromAccount.getAccountId());
        assertEquals(BigDecimal.valueOf(150.0), toAccount.getBalance().add(fromAccount.getBalance()));
        assertEquals(BigDecimal.valueOf(70.0), accountServiceImpl.getAccount(toAccount.getAccountId()).getBalance());
        assertEquals(BigDecimal.valueOf(80.0), accountServiceImpl.getAccount(fromAccount.getAccountId()).getBalance());
    }

    @Test
    @DisplayName("Should throw an exception when receiving account is inactive")
    public void shouldNotTransferFundsWithInActiveStatus() throws AccountException, AccountTransactionException {

        when(accountServiceImpl.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        exceptionRule.expect(AccountTransactionException.class);
        exceptionRule.expectMessage("The sending account with id 1 or receiving account with id 2 is inactive");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        toAccount.setStatus(Account.Status.INACTIVE);

        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        //should throw TransactionException
    }

    @Test
    @DisplayName("Should throw an exception with currency accounts")
    public void shouldNotTransferFundsWithDifferentCurrencies() throws AccountException, AccountTransactionException {

        when(accountServiceImpl.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        exceptionRule.expect(AccountTransactionException.class);
        exceptionRule.expectMessage("Different currency transfer not supported, Sending account is in GBP and receiving account is in NGN");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        toAccount.setCurrencyCode("NGN");

        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        //should throw TransactionException
    }

    @Test
    @DisplayName("Should not transfer funds with missing account")
    public void shouldNotTransferFundsWithMissingAccount() throws AccountException, AccountTransactionException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("No account with id 1 or 2 found");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(fromAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        //should throw AccountException
    }

    @Test
    @DisplayName("Should not transfer funds with insufficient balance from sending account")
    public void shouldNotTransferFundsWithInsufficientBalance() throws AccountException, AccountTransactionException {

        when(accountServiceImpl.getAccount(fromAccount.getAccountId())).thenReturn(fromAccount);

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("Insufficient balance in account with id 1");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .debitAccountId(fromAccount.getAccountId())
                .creditAccountId(toAccount.getAccountId())
                .amount(BigDecimal.valueOf(20000.00))
                .reference("bribe")
                .build();
        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        //should throw AccountException
    }

    @Test
    @DisplayName("Should not transfer funds with same from and to account")
    public void shouldNotTransferFundsWithItself() throws AccountException, AccountTransactionException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("The sending account 2 cannot be same as receiving account 2");

        AccountTransactionDto accountTransactionDto = AccountTransactionDto.builder()
                .creditAccountId(toAccount.getAccountId())
                .debitAccountId(toAccount.getAccountId())
                .reference("bribe")
                .amount(BigDecimal.valueOf(20.00))
                .build();
        accountTransactionServiceImpl.transferFunds(accountTransactionDto);

        //should throw AccountException
    }

}
