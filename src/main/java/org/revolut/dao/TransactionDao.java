package org.revolut.dao;

import lombok.extern.slf4j.Slf4j;
import org.revolut.model.Transaction;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TransactionDao {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private ConcurrentHashMap<Integer, Transaction> transactionMap = new ConcurrentHashMap<>();

    public long addTransaction(Transaction transaction) {
        int transactionId = ID_GENERATOR.getAndIncrement();
        transactionMap.put(transactionId, transaction);
        log.debug("transaction with id {} added", transactionId);
        return transactionId;
    }
}
