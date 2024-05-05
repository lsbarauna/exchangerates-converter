package br.com.jaya.exchangerates.converter.service;

import br.com.jaya.exchangerates.converter.to.NewApikeyOutbound;
import br.com.jaya.exchangerates.converter.to.TransactionOutbound;
import br.com.jaya.exchangerates.converter.to.TransactionInbound;
import br.com.jaya.exchangerates.converter.to.UserInBound;
import br.com.jaya.exchangerates.converter.to.UserOutbound;
import br.com.jaya.exchangerates.converter.to.NewApikeyInbound;
import java.util.List;

public interface ExchangeRatesService {
    TransactionOutbound convertCurrency(TransactionInbound transactionInbound);
    List<TransactionOutbound> listTransactions(Long userId);
    UserOutbound createUser(UserInBound userInBound);
    NewApikeyOutbound generateNewApiKey(NewApikeyInbound newApikeyInbound);
}
