package br.com.jaya.exchangerates.converter.to;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserInBound {
    private String name;
    private String email;
    private String password;
}
