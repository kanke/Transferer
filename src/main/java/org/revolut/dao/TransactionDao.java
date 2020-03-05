package org.revolut.dao;

import lombok.extern.slf4j.Slf4j;
import org.revolut.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class TransactionDao {

    private LongAdder ID_GENERATOR = new LongAdder();
    private ConcurrentHashMap<Long, Transaction> transactionMap = new ConcurrentHashMap<>();

    public long addTransaction(Transaction transaction) {

        ID_GENERATOR.increment();
        long transactionId = ID_GENERATOR.longValue();
        transactionMap.put(transactionId, transaction);
        log.debug("transaction with id {} added", transactionId);
        return transactionId;
    }
}
