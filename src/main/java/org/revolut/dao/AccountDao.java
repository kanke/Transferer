package org.revolut.dao;


import lombok.extern.slf4j.Slf4j;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AccountDao {

    private ConcurrentHashMap<Long, Account> accountMap = new ConcurrentHashMap<>();

    public Account findAccountById(long accountId) throws AccountException {
        if (accountMap.containsKey(accountId)) {
            return accountMap.get(accountId);
        } else {
            throw new AccountException(String.format("No account with id %s found", accountId));
        }
    }

    public BigDecimal getBalance(long accountId) throws AccountException {
        return findAccountById(accountId).getBalance();
    }

    public void withdraw(long accountId, BigDecimal amount) throws AccountException {
        Account fromAccount = findAccountById(accountId);
        if (amount.compareTo(getBalance(accountId)) > 0) {
            throw new AccountException(String.format("Insufficient balance in account with id %s found", accountId));
        } else {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountMap.put(accountId, fromAccount);
            log.debug("amount withdrawn from account id  {}", accountId);
        }
    }

    public void deposit(long accountId, BigDecimal amount) throws AccountException {
        Account toAccount = findAccountById(accountId);
        toAccount.setBalance(getBalance(accountId).add(amount));
        accountMap.put(accountId, toAccount);
    }


}
