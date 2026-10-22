// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Compra.Entities.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import GameStoreClassLibrary.Carrinho.Entities.Dtos.ItemCarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre uma compra feita.
// SITUAÇÃO: implementada.
@Schema(description = "Uma compra finalizada")
public record CompraResponseDTO(

        @Schema(description = "Id da compra", example = "1")
        Long id,

        @Schema(description = "Nome do cliente que comprou", example = "João")
        String clienteNome,

        @Schema(description = "O que foi comprado")
        List<ItemCarrinhoResponseDTO> itens,

        @Schema(description = "Total cobrado do saldo", example = "179.80")
        BigDecimal valorTotal,

        @Schema(description = "Dia da compra", example = "2026-10-06")
        LocalDate dataCompra
) {
}
