package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.to.*;

import java.util.List;

public interface ExchangeRatesService {
    TransactionOutbound convertCurrency(TransactionInbound transactionInbound);
    List<TransactionOutbound> listTransactions(Long userId);
    UserOutbound createUser(UserInBound userInBound);
    NewApikeyOutbound generateNewApiKey(NewApikeyInbound newApikeyInbound);
}
