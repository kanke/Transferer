package org.revolut.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@Ignore
@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountDaoTest {

    @InjectMocks
    private AccountDao accountDao;

    private ConcurrentHashMap<Long, Account> accountMap;
    private Account fromAccount;
    private Account toAccount;

    @Before
    public void setUp() {

        accountMap = new ConcurrentHashMap<>();

        fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100.00));
        fromAccount.setCurrencyCode("GBP");
        fromAccount.setAccountId(1l);
        fromAccount.setAccountNumber(303030);
        accountMap.put(fromAccount.getAccountId(), fromAccount);

        toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50.00));
        toAccount.setCurrencyCode("GBP");
        toAccount.setAccountId(2l);
        toAccount.setAccountNumber(202020);
        accountMap.put(toAccount.getAccountId(), toAccount);

    }

    @Test
    @DisplayName("Should throw an exception when status code is not OK")
    public void shouldFindAccountById() throws AccountException {

        Account account = accountDao.findAccountById(1l);

        verify(accountDao, atLeastOnce()).findAccountById(1L);
        Assert.assertEquals(0, account.getAccountId());

    }
}
