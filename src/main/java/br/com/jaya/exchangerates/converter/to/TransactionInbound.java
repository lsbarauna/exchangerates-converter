package br.com.jaya.exchangerates.converter.to;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionInbound {

    private Long userId;
    private String sourceCurrency;
    private String targetCurrency;
    private Double sourceAmount;

}
