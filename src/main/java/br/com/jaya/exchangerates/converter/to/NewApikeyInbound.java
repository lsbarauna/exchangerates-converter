package br.com.jaya.exchangerates.converter.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewApikeyInbound {

    private String email;

    private String password;
}
