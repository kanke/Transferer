package org.revolut.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dao.impl.InMemoryAccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceImplTest {

    private static final long ACCOUNT_ID = 1;
    private static final long ACCOUNT_ID2 = 2;

    @Mock
    private InMemoryAccountDao inMemoryAccountDao;

    @Mock
    private Account mockAccount;

    private AccountServiceImpl accountServiceImpl;
    private Account expectedAccount;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        accountServiceImpl = new AccountServiceImpl(inMemoryAccountDao);
    }

    @Test
    @DisplayName("Should get correct account by id")
    public void shouldFindAccountById() throws AccountException {
        expectedAccount = new Account();
        expectedAccount.setCurrencyCode("GBP");
        expectedAccount.setAccountName("test");
        expectedAccount.setAccountId(ACCOUNT_ID);

        when(inMemoryAccountDao.findAccountById(ACCOUNT_ID)).thenReturn(expectedAccount);

        Account actualAccount = accountServiceImpl.getAccount(ACCOUNT_ID);

        verify(inMemoryAccountDao, atLeastOnce()).findAccountById(ACCOUNT_ID);
        assertEquals(expectedAccount.getAccountId(), actualAccount.getAccountId());
        assertEquals(expectedAccount.getCurrencyCode(), actualAccount.getCurrencyCode());
        assertEquals(expectedAccount.getAccountName(), actualAccount.getAccountName());
    }

    @Test
    @DisplayName("Should get correct account by id")
    public void shouldNotFindAccountById() throws AccountException {

        Account actualAccount = accountServiceImpl.getAccount(ACCOUNT_ID2);

        verify(inMemoryAccountDao, atLeastOnce()).findAccountById(ACCOUNT_ID2);
        assertNull(actualAccount);
    }


    @Test
    @DisplayName("Should create a new bank account")
    public void shouldCreateAccountById() throws AccountException {

        AccountDto accountDto = AccountDto.builder()
                .accountName("test2")
                .balance(BigDecimal.valueOf(20))
                .currencyCode("EUR")
                .build();

        when(inMemoryAccountDao.createAccount(accountDto)).thenReturn(mockAccount);
        when(mockAccount.getAccountId()).thenReturn(ACCOUNT_ID);

        long actualAccountId = accountServiceImpl.createAccount(accountDto);

        verify(inMemoryAccountDao, atLeastOnce()).createAccount(accountDto);
        assertEquals(1l, actualAccountId);
    }

    @Test
    @DisplayName("Should not create a new bank account")
    public void shouldNotCreateAccountByIdDueToDuplicate() throws AccountException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("An account already exist for test2 in currency EUR");

        AccountDto accountDto = AccountDto.builder()
                .accountName("test2")
                .balance(BigDecimal.valueOf(20))
                .currencyCode("EUR")
                .build();

        when(inMemoryAccountDao.createAccount(accountDto)).thenThrow(new AccountException("An account already exist for test2 in currency EUR"));

        accountServiceImpl.createAccount(accountDto);

        //should throw AccountException
        verify(inMemoryAccountDao, atLeast(0)).createAccount(accountDto);
    }
}
