// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o que o dono envia para cadastrar uma mídia. Aqui mora a
//                   regra R2: nenhum campo pode vir em branco. Não tem id: o banco gera.
// SITUAÇÃO: implementada.
@Schema(description = "Dados para cadastrar um produto (mídia física)")
public record ProdutoRequestDTO(

        @Schema(description = "Nome da mídia", example = "Fita Super Nintendo - Donkey Kong")
        @NotBlank(message = "R2: o nome é obrigatório")
        @Size(max = 255, message = "Nome tem no máximo 255 caracteres")
        String nome,

        @Schema(description = "Preço unitário em reais", example = "89.90")
        @NotNull(message = "R2: o preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Preço inválido: use até 2 casas decimais")
        BigDecimal preco,

        @Schema(description = "Unidades em estoque", example = "5")
        @NotNull(message = "R2: o estoque é obrigatório")
        @PositiveOrZero(message = "O estoque não pode ser negativo")
        Integer estoque
) {
}
