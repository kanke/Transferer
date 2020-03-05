package org.revolut.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.revolut.exception.AccountException;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;

@Data
@Slf4j
public class Account {
    public enum Status {
        ACTIVE,
        INACTIVE
    }

    private String currencyCode;
    private long accountId;
    private String accountName;
    private long accountNumber;
    private long sortCode;
    private BigDecimal balance;
    private LocalDate openDate;
    private Status status;

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
        log.info("amount withdrawn from account id  {}", accountId);
    }

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
        log.info("deposit with id {} added", accountId);
    }
}
