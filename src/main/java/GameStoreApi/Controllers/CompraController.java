// Define o pacote responsável pelas rotas HTTP da aplicação.
package GameStoreApi.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import GameStoreClassLibrary.Compra.Entities.Dtos.CompraRequestDTO;
import GameStoreClassLibrary.Compra.Entities.Dtos.CompraResponseDTO;
import GameStoreDomain.Util.Validations.ErroDTO;
import GameStoreClassLibrary.Compra.Services.CompraService;

// CAMADA: Controller
// RESPONSABILIDADE: receber o pedido de finalizar a compra. As regras R1 e R3
//                   não estão aqui: ficam no CompraService.
// ROTAS ATENDIDAS: POST /compra.
// SITUAÇÃO: implementada.
@Tag(name = "Compras", description = "Finalização da compra, com as regras R1 e R3")
@RestController
public class CompraController {

    private final CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    @Operation(summary = "Finaliza a compra de um carrinho",
            description = "R1: todo produto precisa ter estoque. R3: o saldo do cliente precisa cobrir o total. "
                    + "Se tudo passar, o saldo é descontado e o estoque baixa.")
    @ApiResponse(responseCode = "200", description = "Compra registrada")
    @ApiResponse(responseCode = "400", description = "Corpo inválido, carrinho vazio ou já finalizado, ou regra R1 ou R3 violada (a mensagem diz qual)",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "Não existe carrinho com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping("/compra")
    public CompraResponseDTO finalizar(@Valid @RequestBody CompraRequestDTO dto) {
        return service.finalizar(dto);
    }
}
