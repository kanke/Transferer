package org.revolut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountTransactionDto {
    @NotNull
    private long debitAccountId;

    @NotNull
    private long creditAccountId;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @NotEmpty
    private String reference;

}
