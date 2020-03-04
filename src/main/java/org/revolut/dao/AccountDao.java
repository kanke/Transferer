package org.revolut.dao;


import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.revolut.dto.AccountDto;
import org.revolut.exception.AccountException;
import org.revolut.model.Account;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Singleton
public class AccountDao {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private ConcurrentHashMap<Long, Account> accountMap = new ConcurrentHashMap<>();

    public Account findAccountById(long accountId) throws AccountException {
        for (Map.Entry<Long, Account> entry : accountMap.entrySet()) {
            String key = entry.getKey().toString();
            Account value = entry.getValue();
            System.out.println("key: " + key + " value: " + value);
            //log.info("accountMap " + accountMap.size() + " " + key + " " + value.toString());
        }


        if (accountMap.containsKey(accountId)) {
            return accountMap.get(accountId);
        } else {
            throw new AccountException(String.format("No account with id %s found", accountId));
        }

        //return account;
    }

    public BigDecimal getBalance(long accountId) throws AccountException {
        Account accountById = findAccountById(accountId);
        return accountById.getBalance();
    }

    public void withdraw(long accountId, BigDecimal amount) throws AccountException {
        Account fromAccount = findAccountById(accountId);
        if (getBalance(accountId).compareTo(amount) < 0) {
            System.out.println("Yep, " + getBalance(accountId)+ " is smaller than " +amount+ "!");
            throw new AccountException(String.format("Insufficient balance in account with id %s", accountId));
        } else {
            System.out.println("No, " + getBalance(accountId)+ " is bigger than " +amount+ "!");
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountMap.put(accountId, fromAccount);
            log.debug("amount withdrawn from account id  {}", accountId);
        }
    }

    public void deposit(long accountId, BigDecimal amount) throws AccountException {
        Account toAccount = findAccountById(accountId);
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountMap.put(accountId, toAccount);
    }


    public Account createAccount(AccountDto accountDto) throws AccountException {
        validateAccountDetails(accountDto);

        Account account = new Account();
        account.setAccountId(ID_GENERATOR.getAndIncrement());
        account.setCurrencyCode(accountDto.getCurrency());
        account.setStatus(Account.Status.ACTIVE);
        account.setBalance(accountDto.getBalance());
        account.setAccountName(accountDto.getAccountName());
        account.setOpenDate(LocalDate.now());

        accountMap.put(account.getAccountId(), account);
        return account;
    }

    public void validateAccountDetails(AccountDto accountDto) throws AccountException {
        if (accountMap.containsValue(accountDto))
            throw new AccountException(String.format("An account already exist for %s in currency %s ",
                    accountDto.getAccountName(), accountDto.getCurrency()));
    }
}
