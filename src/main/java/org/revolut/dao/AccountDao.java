package org.revolut.dao;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Singleton
public class AccountDao {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
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

        Account account = new Account();
        account.setAccountId(ID_GENERATOR.getAndIncrement());
        account.setCurrencyCode(accountDto.getCurrencyCode());
        account.setStatus(Account.Status.ACTIVE);
        account.setBalance(accountDto.getBalance());
        account.setAccountName(accountDto.getAccountName());
        account.setOpenDate(LocalDate.now());

        accountMap.put(account.getAccountId(), account);
        log.debug("account with id {} created", account.getAccountId());
        return account;
    }

    public void validateAccountDetails(AccountDto accountDto) throws AccountException {
        if (accountMap.containsValue(accountDto))
            throw new AccountException(String.format("An account already exist for %s in currency %s ",
                    accountDto.getAccountName(), accountDto.getCurrencyCode()));
    }
}
