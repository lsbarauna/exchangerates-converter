package br.com.jaya.exchangerates.converter.repository;

import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesData;
import br.com.jaya.exchangerates.converter.client.apilayer.ExchangeRatesDataAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DefaultExchangeRatesDataRapository implements ExchangeRatesDataRapository {
    @Autowired
    private ExchangeRatesDataAPI exchangeRatesDataAPI;

    @Value("${apikey}")
    private String apikey;

    @Value("${symbols}")
    private String symbols;

    @Value("${base}")
    private String base;

    @Cacheable("rates")
    public ExchangeRatesData findExchangeRatesData() {

        try {
            ResponseEntity<ExchangeRatesData> exchangeRatesDataResponseEntity = exchangeRatesDataAPI.getExchangeRatesData(apikey, symbols, base);
            return  exchangeRatesDataResponseEntity.getBody();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("System unavailable");
        }
    }

    @CacheEvict(allEntries = true, cacheNames = { "rates"})
    @Scheduled(fixedDelay = 60000)
    public void cacheEvict() {

    }

}
