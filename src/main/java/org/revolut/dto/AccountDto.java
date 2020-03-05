package org.revolut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @JsonProperty(required = true)
    private String accountName;

    @JsonProperty(required = true)
    @PositiveOrZero
    private BigDecimal balance;

    @JsonProperty(required = true)
    private String currencyCode;
}
