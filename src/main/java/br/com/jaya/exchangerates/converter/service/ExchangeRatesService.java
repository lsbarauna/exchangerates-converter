package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.to.TransactionInbound;
import br.com.jaya.exchangerates.converter.to.TransactionOutbound;

import java.util.List;

public interface ExchangeRatesService {
    TransactionOutbound convertCurrency(TransactionInbound transactionInbound);
    List<TransactionOutbound> listTransactions(Long userId);
}
