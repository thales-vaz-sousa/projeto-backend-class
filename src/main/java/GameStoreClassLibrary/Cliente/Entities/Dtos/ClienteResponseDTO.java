// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Cliente.Entities.Dtos;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre um cliente.
// SITUAÇÃO: implementada.
@Schema(description = "Um cliente cadastrado")
public record ClienteResponseDTO(

        @Schema(description = "Id do cliente", example = "1")
        Long id,

        @Schema(description = "Nome do cliente", example = "João")
        String nome,

        @Schema(description = "E-mail do cliente", example = "joao@exemplo.com")
        String email,

        @Schema(description = "Saldo atual em reais", example = "150.00")
        BigDecimal saldo
) {
}
