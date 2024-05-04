package br.com.jaya.exchangerates.converter.client.apilayer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "apilayer", url = "http://api.apilayer.com/exchangerates_data" )
public interface ExchangeRatesDataAPI {
    @RequestMapping(method = RequestMethod.GET, value = "/latest", produces = {"application/json"})
    ResponseEntity<ExchangeRatesData> getExchangeRatesData(@RequestHeader("apikey") String apikey,
                                                                        @RequestParam(value = "symbols") String symbols,
                                                                        @RequestParam(value = "base") String base);

}
