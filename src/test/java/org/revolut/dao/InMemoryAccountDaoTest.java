package org.revolut.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InMemoryAccountDaoTest {

    @InjectMocks
    private InMemoryAccountDao inMemoryAccountDao;

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
        toAccount.setAccountName("test");
        accountMap.put(toAccount.getAccountId(), toAccount);
    }

    @Test
    @DisplayName("Should return correct account")
    public void shouldFindAccountById() throws AccountException {
        when(accountMap.entrySet()).thenReturn(new ConcurrentHashMap<Long, Account>() {{
            put(toAccount.getAccountId(), toAccount);
        }}.entrySet());
        when(accountMap.containsKey(toAccount.getAccountId())).thenReturn(true);

        when(inMemoryAccountDao.findAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        Account account = inMemoryAccountDao.findAccountById(toAccount.getAccountId());

        verify(accountMap, atLeastOnce()).containsKey(toAccount.getAccountId());
        Assert.assertEquals(2, account.getAccountId());
    }

    @Test
    @DisplayName("Should return exception when account not found")
    public void shouldNotFindAccountById() throws AccountException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("No account with id 2 found");

        when(inMemoryAccountDao.findAccountById(toAccount.getAccountId())).thenReturn(toAccount);

        inMemoryAccountDao.findAccountById(toAccount.getAccountId());

        //should throw TransactionException
    }

    @Test
    @DisplayName("Should create correct account")
    public void shouldCreateAccount() throws AccountException {
        AccountDto accountDto = AccountDto.builder()
                .accountName("test")
                .balance(BigDecimal.valueOf(20.00))
                .currencyCode("GBP")
                .build();

        Account account = inMemoryAccountDao.createAccount(accountDto);
        Assert.assertEquals(1, account.getAccountId());
    }

    @Test
    @DisplayName("Should not create account")
    public void shouldNotCreateAccount() throws AccountException {

        exceptionRule.expect(AccountException.class);
        exceptionRule.expectMessage("An account already exist for test in currency GBP");

        when(accountMap.entrySet()).thenReturn(new ConcurrentHashMap<Long, Account>() {{
            put(toAccount.getAccountId(), toAccount);
        }}.entrySet());
        when(accountMap.containsKey(toAccount.getAccountId())).thenReturn(true);

        when(inMemoryAccountDao.findAccountById(toAccount.getAccountId())).thenReturn(toAccount);
        AccountDto accountDto = AccountDto.builder()
                .accountName("test")
                .balance(BigDecimal.valueOf(50.00))
                .currencyCode("GBP")
                .build();

        inMemoryAccountDao.createAccount(accountDto);
    }
}
