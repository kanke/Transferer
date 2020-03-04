package org.revolut.service;


import com.google.inject.Inject;
import org.joda.time.LocalDate;
import org.revolut.dao.TransactionDao;
import org.revolut.dto.AccountTransactionDto;
import org.revolut.exception.AccountException;
import org.revolut.exception.TransactionException;
import org.revolut.model.Account;
import org.revolut.model.Transaction;

import java.math.BigDecimal;

public class TransactionService {

    private final AccountService accountService;
    private final TransactionDao transactionDao;

    @Inject
    public TransactionService(AccountService accountService, TransactionDao transactionDao) {
        this.accountService = accountService;
        this.transactionDao = transactionDao;
    }

    public long transferFunds(AccountTransactionDto accountTransaction) throws TransactionException, AccountException {

        Account fromAccount = accountService.getAccount(accountTransaction.getDebitAccountId());
        Account toAccount = accountService.getAccount(accountTransaction.getCreditAccountId());

        if (fromAccount != null && toAccount != null) {

            validateAccountStatus(fromAccount, toAccount);
            validateAccountType(fromAccount.getCurrencyCode(), toAccount.getCurrencyCode());

            BigDecimal transactionAmount = accountTransaction.getAmount();
            accountService.withdrawFromAccount(fromAccount.getAccountId(), transactionAmount);
            accountService.depositToAccount(toAccount.getAccountId(), transactionAmount);

            Transaction transaction = Transaction.builder()
                    .amount(accountTransaction.getAmount())
                    .debitAccountId(accountTransaction.getDebitAccountId())
                    .creditAccountId(accountTransaction.getCreditAccountId())
                    .reference(accountTransaction.getReference())
                    .transactionDate(LocalDate.now())
                    .status(Transaction.Status.COMPLETED)
                    .build();

            return transactionDao.addTransaction(transaction);

        } else {
            throw new AccountException(String.format("No account with id %s or %s found", fromAccount.getAccountId(), toAccount.getAccountId()));
        }
    }

    private void validateAccountStatus(Account fromAccount, Account toAccount) throws TransactionException {
        //check inactive bank account
        if (fromAccount.getStatus() == Account.Status.INACTIVE || toAccount.getStatus() == Account.Status.INACTIVE)
            throw new TransactionException(String.format(String.format("The sending account with id %s or receiving account with id %s is inactive", fromAccount.getAccountId(), toAccount.getAccountId())));
    }

    private void validateAccountType(String fromCurrencyCode, String toCurrencyCode) throws TransactionException {
        //check account currency matches
        if (!fromCurrencyCode.equalsIgnoreCase(toCurrencyCode)) {
            throw new TransactionException(String.format("Different currency transfer not supported, " +
                    "Sending account is in %s and receiving account is in %s", fromCurrencyCode, toCurrencyCode));
        }
    }
}
