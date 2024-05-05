package br.com.jaya.exchangerates.converter.controller;

import br.com.jaya.exchangerates.converter.service.ExchangeRatesService;
import br.com.jaya.exchangerates.converter.to.UserInBound;
import br.com.jaya.exchangerates.converter.to.UserOutbound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

}
