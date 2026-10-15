// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// CAMADA: DTO
// RESPONSABILIDADE: o que o cliente envia para pôr um produto no carrinho.
// SITUAÇÃO: implementada.
@Schema(description = "Um produto a colocar no carrinho")
public record ItemCarrinhoRequestDTO(

        @Schema(description = "Id do produto", example = "1")
        @NotNull(message = "O id do produto é obrigatório")
        Long produtoId,

        @Schema(description = "Quantidade de unidades", example = "2")
        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}
