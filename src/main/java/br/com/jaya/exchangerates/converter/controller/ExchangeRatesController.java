package br.com.jaya.exchangerates.converter.controller;

import br.com.jaya.exchangerates.converter.security.ApiKeyAuthentication;
import br.com.jaya.exchangerates.converter.service.ExchangeRatesService;
import br.com.jaya.exchangerates.converter.to.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/exchangerates")
@Validated
public class ExchangeRatesController {

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    @Operation(summary = "Create User",
            description = "Create a new user",
            responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserOutbound.class))
            })
        }
    )
    @PostMapping("/user")
    public ResponseEntity<UserOutbound> createUser(@Valid @RequestBody UserInBound userInBound) {
        UserOutbound userOutbound = exchangeRatesService.createUser(userInBound);
        return ResponseEntity.status(HttpStatus.CREATED).body(userOutbound);
    }

    @Operation(
            summary = "Convert Currency",
            description = "\n" +
                    "Currency conversion endpoint, which can be used to convert value from one currency to another. To convert currencies, use the API conversion endpoint, supported currencies are: BRL,USD,EUR,JPY. We adopt the base currency EUR.",
            parameters = {
                    @Parameter(
                            name = "X-API-KEY",
                            description = "API Access Key",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "d4a5725e7dc182b0c41f8d07b0d1a08d"
                    ),
                    @Parameter(
                            name = "sourceCurrency",
                            description = "The three-letter currency code of the currency you would like to convert from",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "USD"
                    ),
                    @Parameter(
                            name = "targetCurrency",
                            description = "The three-letter currency code of the currency you would like to convert to",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "BRL"

                    ),
                    @Parameter(
                            name = "sourceAmount",
                            description = "The amount to be converted",
                            required = true,
                            in = ParameterIn.QUERY,
                            example = "100"
                    )
            }
    )

    @GetMapping("/convert")
    public ResponseEntity<TransactionOutbound> convertCurrency(@RequestParam String sourceCurrency,
                                                               @RequestParam String targetCurrency,
                                                               @RequestParam Double sourceAmount) {
        TransactionInbound transactionInbound = TransactionInbound.builder()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .sourceAmount(sourceAmount)
                .userId(getLoggedUser())
                .build();

        TransactionOutbound transactionOutbound = exchangeRatesService.convertCurrency(transactionInbound);

        return ResponseEntity.status(HttpStatus.OK).body(transactionOutbound);
    }
    @Operation(summary = "List Transactions",
            description = "List all transactions of the current user",
            parameters = {
                    @Parameter(
                            name = "X-API-KEY",
                            description = "API Access Key",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "d4a5725e7dc182b0c41f8d07b0d1a08d"
                    )
            }
    )
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionOutbound>> listTransactions() {

        List<TransactionOutbound> transactionOutboundList = exchangeRatesService.listTransactions(getLoggedUser());

        return ResponseEntity.status(HttpStatus.OK).body(transactionOutboundList);
    }

    @Operation(summary = "Generate New API Key",
            description = "Generate a new API key for the user",
            responses = {
                    @ApiResponse(responseCode = "200",  content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = NewApikeyOutbound.class))
                    })
            }
    )
    @PutMapping("/new-apikey")
    public ResponseEntity<NewApikeyOutbound> generateNewApiKey(@RequestBody NewApikeyInbound newApikeyInbound) {
        NewApikeyOutbound apikey = exchangeRatesService.generateNewApiKey(newApikeyInbound);
        return ResponseEntity.status(HttpStatus.CREATED).body(apikey);
    }

    private Long getLoggedUser() {
        ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return apiKeyAuthentication.getUserId();
    }
}
