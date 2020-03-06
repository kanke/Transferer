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
import org.revolut.exception.AccountException;
import org.revolut.model.Account;
import org.revolut.service.impl.AccountServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountServiceImplTest {

    private static final int ACCOUNT_ID = 1;
    private static final int ACCOUNT_ID2 = 2;

    @Mock
    private InMemoryAccountDao inMemoryAccountDao;

    private AccountServiceImpl accountServiceImpl;
    Account expectedAccount;

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

}
