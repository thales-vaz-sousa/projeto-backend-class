// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Compra.Entities.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// CAMADA: DTO
// RESPONSABILIDADE: o que o cliente envia para finalizar a compra: só o id do carrinho.
// SITUAÇÃO: implementada.
@Schema(description = "Dados para finalizar uma compra")
public record CompraRequestDTO(

        @Schema(description = "Id do carrinho a pagar", example = "1")
        @NotNull(message = "O id do carrinho é obrigatório")
        Long carrinhoId
) {
}
