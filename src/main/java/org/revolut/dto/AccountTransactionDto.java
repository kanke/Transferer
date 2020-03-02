package org.revolut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountTransactionDto {
    @JsonProperty(required = true)
    private long debitAccountId;

    @JsonProperty(required = true)
    private long creditAccountId;

    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty(required = true)
    private String reference;

}
