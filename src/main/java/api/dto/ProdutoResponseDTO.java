// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre um produto.
// SITUAÇÃO: implementada.
@Schema(description = "Um produto (mídia física) da loja")
public record ProdutoResponseDTO(

        @Schema(description = "Id do produto", example = "1")
        Long id,

        @Schema(description = "Nome da mídia", example = "Fita Super Nintendo - Donkey Kong")
        String nome,

        @Schema(description = "Preço unitário em reais", example = "89.90")
        BigDecimal preco,

        @Schema(description = "Unidades em estoque", example = "5")
        int estoque
) {
}
