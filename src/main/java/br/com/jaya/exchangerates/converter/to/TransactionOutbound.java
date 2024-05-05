package br.com.jaya.exchangerates.converter.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
@Data
public class TransactionOutbound {

    @Schema(example = "1")
    private String transactionId;
    @Schema(example = "1")
    private String userId;
    @Schema(example = "USD")
    private String sourceCurrency;
    @Schema(example = "100")
    private Double sourceAmount;
    @Schema(example = "BRL")
    private String targetCurrency;
    @Schema(example = "519.590015")
    private Double targetAmount;
    @Schema(example = "5.1959")
    private Double conversionRate;
    @Schema(example = "2024-05-01 16:06:25")
    private LocalDateTime dateTime;

    public void setTargetAmount(Double amount) {
        BigDecimal bd = new BigDecimal(amount).setScale(6, RoundingMode.FLOOR);
        this.targetAmount = bd.doubleValue();
    }
    public void setConversionRate(Double amount) {
        BigDecimal bd = new BigDecimal(amount).setScale(6, RoundingMode.FLOOR);
        this.conversionRate = bd.doubleValue();
    }
}

