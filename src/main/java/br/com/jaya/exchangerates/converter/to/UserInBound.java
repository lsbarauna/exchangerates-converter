package br.com.jaya.exchangerates.converter.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInBound {
    @NotEmpty
    @Schema(example = "Jhon")
    private String name;
    @NotEmpty
    @Schema(example = "jhon@exemple.com")
    private String email;
    @NotEmpty
    @Schema(example = "Spaf3tam$")
    private String password;
}
