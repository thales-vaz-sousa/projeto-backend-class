// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Carrinho.Entities.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

// CAMADA: DTO
// RESPONSABILIDADE: o que o cliente envia para abrir um carrinho. Entra só o
//                   id do cliente; a data de criação quem decide é a API.
// SITUAÇÃO: implementada.
@Schema(description = "Dados para abrir um carrinho")
public record CarrinhoRequestDTO(

        @Schema(description = "Id do cliente dono do carrinho", example = "1")
        @NotNull(message = "O id do cliente é obrigatório")
        Long clienteId
) {
}
