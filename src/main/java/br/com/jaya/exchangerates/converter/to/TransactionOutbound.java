package br.com.jaya.exchangerates.converter.to;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TransactionOutbound {

    private String transactionId;

    private String userId;

    private String sourceCurrency;

    private Double sourceAmount;

    private String targetCurrency;

    private Double targetAmount;

    private Double conversionRate;

    private LocalDateTime dateTime;
}

