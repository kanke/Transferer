package org.revolut.service.impl;

import com.google.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.AccountTransactionException;
import org.revolut.model.Account;
import org.revolut.service.AccountService;
import org.revolut.service.AccountTransactionService;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * This class is responsible processing and validating account transactions
 */
@Slf4j
public class AccountTransactionServiceImpl implements AccountTransactionService {

    private final AccountService accountService;
    private Lock withdrawAccountLock = new ReentrantLock();
    private Lock depositAccountLock = new ReentrantLock();

    @Inject
    public AccountTransactionServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    public void transferFunds(AccountTransactionDto accountTransaction) throws AccountTransactionException, AccountException {
        Account fromAccount = accountService.getAccount(accountTransaction.getDebitAccountId());
        Account toAccount = accountService.getAccount(accountTransaction.getCreditAccountId());
        BigDecimal transactionAmount = accountTransaction.getAmount();

        validateAccountTransaction(accountTransaction);

        try {
            acquireLocks(withdrawAccountLock, depositAccountLock);

            fromAccount.withdraw(transactionAmount);
            toAccount.deposit(transactionAmount);

        } catch (InterruptedException e) {
            throw new AccountTransactionException(String.format("Something happened whilst making transfer. Transfer not completed"));
        } finally {
            withdrawAccountLock.unlock();
            depositAccountLock.unlock();
        }

        log.info("transaction with id {} completed");
    }


    /**
     * Getting locks in the same order and not holding locks at the same time to avoid deadlock
     * Many threads can acquire a lock, but only one can have it at a time. If a thread tryLock while it's being used by another, it has to wait until the other thread unlock.
     * Technically one lock is enough for the transactions between two accounts, but if we had 1000 accounts, the number of locks would be huge as we need one lock for each pair of accounts.
     * By using a lock for an account, we would need only 1000 locks in that case.
     */
    private void acquireLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
        while (true) {
            // Acquire locks
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;
            try {
                gotFirstLock = firstLock.tryLock();
                gotSecondLock = secondLock.tryLock();
            } finally {
                if (gotFirstLock && gotSecondLock) return;
                else if (gotFirstLock) firstLock.unlock();
                else if (gotSecondLock) secondLock.unlock();
            }
            // Locks not acquired
            Thread.sleep(1);
        }
    }

    private void validateAccountTransaction(AccountTransactionDto accountTransaction) throws AccountTransactionException, AccountException {
        Account fromAccount = accountService.getAccount(accountTransaction.getDebitAccountId());
        Account toAccount = accountService.getAccount(accountTransaction.getCreditAccountId());
        BigDecimal amount = accountTransaction.getAmount();

        //check transfer accounts exist
        if (fromAccount == null || toAccount == null) {
            throw new AccountException(String.format("No account with id %s or %s found", accountTransaction.getDebitAccountId(), accountTransaction.getCreditAccountId()));
        }

        //check account transfer to itself
        if (fromAccount.getAccountId() == toAccount.getAccountId() || toAccount.getAccountId() == fromAccount.getAccountId()) {
            throw new AccountException(String.format("The sending account %s cannot be same as receiving account %s", fromAccount.getAccountId(), toAccount.getAccountId()));
        }

        //check inactive bank account
        if (fromAccount.getStatus() == Account.Status.INACTIVE || toAccount.getStatus() == Account.Status.INACTIVE) {
            throw new AccountTransactionException(String.format(String.format("The sending account with id %s or receiving account with id %s is inactive", fromAccount.getAccountId(), toAccount.getAccountId())));
        }

        //check account currency matches
        if (!fromAccount.getCurrencyCode().equalsIgnoreCase(toAccount.getCurrencyCode())) {
            throw new AccountTransactionException(String.format("Different currency transfer not supported, " +
                    "Sending account is in %s and receiving account is in %s", fromAccount.getCurrencyCode(), toAccount.getCurrencyCode()));
        }

        //check sending account has enough funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new AccountException(String.format("Insufficient balance in account with id %s", fromAccount.getAccountId()));
        }
    }

}
