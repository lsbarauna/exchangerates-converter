package br.com.jaya.exchangerates.converter.client.apilayer;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRatesData {

    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;

}
