package br.com.jaya.exchangerates.converter.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOutbound {

    @Schema(example = "1")
    private Long userId;
    @Schema(example = "Jhon")
    private String name;
    @Schema(example = "jhon@exemple.com")
    private String email;
    @Schema(example = "961550845ffdd067ec79c9e42b4c1954")
    private String apikey;
}
