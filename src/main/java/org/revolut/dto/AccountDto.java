package org.revolut.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String accountName;
    private BigDecimal balance;
    private String currency;
}
