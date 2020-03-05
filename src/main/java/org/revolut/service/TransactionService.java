package org.revolut.service;

import com.google.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.revolut.dao.TransactionDao;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.model.Account;
import org.revolut.model.Transaction;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TransactionService {

    private final AccountService accountService;
    private final TransactionDao transactionDao;
    private Lock firstLock = new ReentrantLock();
    private Lock secondLock = new ReentrantLock();

    @Inject
    public TransactionService(AccountService accountService, TransactionDao transactionDao) {
        this.accountService = accountService;
        this.transactionDao = transactionDao;
    }

    public long transferFunds(AccountTransactionDto accountTransaction) throws TransactionException, AccountException {
        Account fromAccount = accountService.getAccount(accountTransaction.getDebitAccountId());
        Account toAccount = accountService.getAccount(accountTransaction.getCreditAccountId());
        BigDecimal transactionAmount = accountTransaction.getAmount();
        long transactionId;

        validateAccountTransaction(accountTransaction);

        try {
            acquireLocks(firstLock, secondLock);

            fromAccount.withdraw(transactionAmount);
            toAccount.deposit(transactionAmount);

            Transaction transaction = Transaction.builder()
                    .amount(accountTransaction.getAmount())
                    .debitAccountId(accountTransaction.getDebitAccountId())
                    .creditAccountId(accountTransaction.getCreditAccountId())
                    .reference(accountTransaction.getReference())
                    .transactionDate(LocalDate.now())
                    .status(Transaction.Status.COMPLETED)
                    .build();

            transactionId = transactionDao.addTransaction(transaction);

        } catch (InterruptedException e) {
            throw new TransactionException(String.format("Something happened whilst making transfer. Transfer not completed"));
        } finally {
            firstLock.unlock();
            secondLock.unlock();
        }

        log.info("transaction with id {} completed", transactionId);
        return transactionId;
    }

    //Getting locks in the same order and not holding locks at the same time
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

    private void validateAccountTransaction(AccountTransactionDto accountTransaction) throws TransactionException, AccountException {
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
            throw new TransactionException(String.format(String.format("The sending account with id %s or receiving account with id %s is inactive", fromAccount.getAccountId(), toAccount.getAccountId())));
        }

        //check account currency matches
        if (!fromAccount.getCurrencyCode().equalsIgnoreCase(toAccount.getCurrencyCode())) {
            throw new TransactionException(String.format("Different currency transfer not supported, " +
                    "Sending account is in %s and receiving account is in %s", fromAccount.getCurrencyCode(), toAccount.getCurrencyCode()));
        }

        //check sending account has enough funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            System.out.println("Yep, " + fromAccount.getBalance() + " is smaller than " + amount + "!");
            throw new AccountException(String.format("Insufficient balance in account with id %s", fromAccount.getAccountId()));
        }
    }

}
