package br.com.jaya.exchangerates.converter.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewApikeyOutbound {

    @Schema(example = "d4a5725e7dc182b0c41f8d07b0d1a08d")
    private String newApiKey;
}
