package org.revolut.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountDaoTest {

    @InjectMocks
    private AccountDao accountDao;

    @Mock
    private ConcurrentHashMap<Long, Account> accountMap;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Account toAccount;

    @Before
    public void setUp() {

        toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");
        toAccount.setAccountId(2l);
        toAccount.setAccountNumber(202020);
        accountMap.put(toAccount.getAccountId(), toAccount);
    }

    @Test
    @DisplayName("Should return correct account")
    public void shouldFindAccountById() throws AccountException {
        when(accountMap.entrySet()).thenReturn(new ConcurrentHashMap<Long, Account>() {{
            put(toAccount.getAccountId(), toAccount);
        }}.entrySet());
        when(accountMap.containsKey(toAccount.getAccountId())).thenReturn(true);

        when(accountDao.findAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        Account account = accountDao.findAccountById(toAccount.getAccountId());

        verify(accountMap, atLeastOnce()).containsKey(toAccount.getAccountId());
        Assert.assertEquals(2, account.getAccountId());
    }

    @Test
    @DisplayName("Should return exception when account not found")
    public void shouldNotFindAccountById() throws AccountException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("No account with id 2 found");

        when(accountDao.findAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        accountDao.findAccountById(toAccount.getAccountId());

        //should throw TransactionException
    }
}
