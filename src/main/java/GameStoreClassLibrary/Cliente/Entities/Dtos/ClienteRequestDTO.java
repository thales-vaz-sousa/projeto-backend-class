// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package GameStoreClassLibrary.Cliente.Entities.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o que o dono envia para cadastrar um cliente. Não tem id
//                   nem saldo: o saldo nasce zerado e é carregado em PUT /saldo-cliente.
// SITUAÇÃO: implementada.
@Schema(description = "Dados para cadastrar um cliente")
public record ClienteRequestDTO(

        @Schema(description = "Nome do cliente", example = "João")
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome tem no máximo 255 caracteres")
        String nome,

        @Schema(description = "E-mail do cliente", example = "joao@exemplo.com")
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "E-mail tem no máximo 255 caracteres")
        String email
) {
}
