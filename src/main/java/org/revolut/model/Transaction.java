package org.revolut.model;

import lombok.Builder;
import lombok.Data;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@Data @Builder
public class Transaction {
    public enum Status {
        PENDING,
        CANCELLED,
        COMPLETED
    }

    private long debitAccountId;
    private long creditAccountId;
    private BigDecimal amount;
    private String reference;
    private LocalDate transactionDate;
    private Transaction.Status status;
}
