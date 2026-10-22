// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Carrinho.Entities.Dtos;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// CAMADA: DTO
// RESPONSABILIDADE: o que o dono envia para carregar saldo na conta de um
//                   cliente. O valor é somado ao saldo atual.
// SITUAÇÃO: implementada.
@Schema(description = "Recarga de saldo de um cliente")
public record SaldoClienteRequestDTO(

        @Schema(description = "Id do cliente", example = "1")
        @NotNull(message = "O id do cliente é obrigatório")
        Long clienteId,

        @Schema(description = "Valor a somar ao saldo, em reais", example = "100.00")
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "Valor inválido: use até 2 casas decimais")
        BigDecimal valor
) {
}
