package br.com.jaya.exchangerates.converter.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewApikeyInbound {

    @NotEmpty
    @Schema(example = "jhon@exemple.com")
    private String email;
    @NotEmpty
    @Schema(example = "Spaf3tam$")
    private String password;
}
