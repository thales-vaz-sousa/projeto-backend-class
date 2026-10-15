// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

// CAMADA: DTO
// RESPONSABILIDADE: o que a API devolve sobre um carrinho: dono, itens e total.
// SITUAÇÃO: implementada.
@Schema(description = "Um carrinho de compras")
public record CarrinhoResponseDTO(

        @Schema(description = "Id do carrinho", example = "1")
        Long id,

        @Schema(description = "Id do cliente dono", example = "1")
        Long clienteId,

        @Schema(description = "Nome do cliente dono", example = "João")
        String clienteNome,

        @Schema(description = "Dia em que o carrinho foi aberto", example = "2026-10-06")
        LocalDate dataCriacao,

        @Schema(description = "Itens do carrinho (vazio se ainda não tem nenhum)")
        List<ItemCarrinhoResponseDTO> itens,

        @Schema(description = "Soma dos subtotais", example = "179.80")
        BigDecimal total,

        @Schema(description = "Se a compra já foi finalizada", example = "false")
        boolean finalizado
) {
}
