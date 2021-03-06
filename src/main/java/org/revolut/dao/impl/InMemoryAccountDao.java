package org.revolut.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.revolut.dao.AccountDao;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;


/**
 * This class represents the in memory data store implementation of Accounts entity or table
 * using a concurrent hash map.
 */
@Slf4j
@Singleton
public class InMemoryAccountDao implements AccountDao {

    private LongAdder ID_GENERATOR = new LongAdder();
    private ConcurrentHashMap<Long, Account> accountMap = new ConcurrentHashMap<>();

    public Account findAccountById(long accountId) throws AccountException {
        if (accountMap.containsKey(accountId)) {
            return accountMap.get(accountId);
        } else {
            throw new AccountException(String.format("No account with id %s found", accountId));
        }
    }

    public Account createAccount(AccountDto accountDto) throws AccountException {
       validateAccountDetails(accountDto);

        ID_GENERATOR.increment();
        Account account = new Account();
        account.setAccountId(ID_GENERATOR.longValue());
        account.setCurrencyCode(accountDto.getCurrencyCode());
        account.setStatus(Account.Status.ACTIVE);
        account.setBalance(accountDto.getBalance());
        account.setAccountName(accountDto.getAccountName());
        account.setOpenDate(LocalDate.now());

        accountMap.putIfAbsent(account.getAccountId(), account);
        log.debug("account with id {} created", account.getAccountId());
        return account;
    }

    private void validateAccountDetails(AccountDto accountDto) throws AccountException {
        for (Map.Entry<Long, Account> entry : accountMap.entrySet()) {
            Account value = entry.getValue();
            if (value.getAccountName().equalsIgnoreCase(accountDto.getAccountName()) && value.getCurrencyCode().equalsIgnoreCase(accountDto.getCurrencyCode())) {
                throw new AccountException(String.format("An account already exist for %s in currency %s ",
                        accountDto.getAccountName(), accountDto.getCurrencyCode()));
            }
        }

    }
}
