package org.revolut.model;

import lombok.Data;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@Data
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

}
