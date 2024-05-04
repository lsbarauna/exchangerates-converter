package br.com.jaya.exchangerates.converter.repository;

import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesData;

public interface ExchangeRatesDataRapository {
    ExchangeRatesData findExchangeRatesData();
}
