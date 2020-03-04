package org.revolut.service;

import com.google.inject.Inject;
import org.revolut.dao.AccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import java.math.BigDecimal;


public class AccountService {

    private final AccountDao accountDao;

    @Inject
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    protected Account getAccount(long accountId) throws AccountException {
        return accountDao.findAccountById(accountId);
    }

    protected void withdrawFromAccount(long accountId, BigDecimal amount) throws AccountException {
        accountDao.withdraw(accountId, amount);
    }

    protected void depositToAccount(long accountId, BigDecimal amount) throws AccountException {
        accountDao.deposit(accountId, amount);
    }

    public long createAccount(AccountDto accountDto) throws AccountException {
        return accountDao.createAccount(accountDto).getAccountId();
    }
}
