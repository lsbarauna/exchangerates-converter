package br.com.jaya.exchangerates.converter.to;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOutbound {

    private Long userId;
    private String name;
    private String email;
    private String apikey;
}
