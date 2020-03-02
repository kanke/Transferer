package org.revolut.dao;

import lombok.extern.slf4j.Slf4j;
import org.revolut.exception.TransactionException;
import org.revolut.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TransactionDao {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private ConcurrentHashMap<Integer, Transaction> transactionMap = new ConcurrentHashMap<>();

    public Transaction getTransactionByAccountId(long accountId) throws TransactionException {
        if (transactionMap.containsKey(accountId)) {
            return transactionMap.get(accountId);
        } else {
            throw new TransactionException(String.format("No transaction found with account id %s found", accountId));
        }
    }

    public long addTransaction(Transaction transaction) {
        int transactionId = ID_GENERATOR.getAndIncrement();
        transactionMap.put(transactionId, transaction);
        log.debug("transaction with id {} added", transactionId);
        return transactionId;
    }
}
