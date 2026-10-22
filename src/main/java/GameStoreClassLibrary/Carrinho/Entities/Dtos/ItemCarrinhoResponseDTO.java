// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Carrinho.Entities.Dtos;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre uma linha do carrinho.
// SITUAÇÃO: implementada.
@Schema(description = "Uma linha do carrinho")
public record ItemCarrinhoResponseDTO(

        @Schema(description = "Id do produto", example = "1")
        Long produtoId,

        @Schema(description = "Nome do produto", example = "Fita Super Nintendo - Donkey Kong")
        String produtoNome,

        @Schema(description = "Preço unitário", example = "89.90")
        BigDecimal precoUnitario,

        @Schema(description = "Quantidade de unidades", example = "2")
        int quantidade,

        @Schema(description = "Preço unitário vezes quantidade", example = "179.80")
        BigDecimal subtotal
) {
}
