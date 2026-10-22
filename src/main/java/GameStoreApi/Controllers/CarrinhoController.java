// Define o pacote responsável pelas rotas HTTP da aplicação.
package GameStoreApi.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import GameStoreClassLibrary.Carrinho.Entities.Dtos.CarrinhoRequestDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.CarrinhoResponseDTO;
import GameStoreDomain.Util.Validations.ErroDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.ItemCarrinhoRequestDTO;
import GameStoreClassLibrary.Carrinho.Services.CarrinhoService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as requisições de carrinho. A regra R1 não está
//                   aqui: fica no CarrinhoService.
// ROTAS ATENDIDAS: POST /carrinho, POST /carrinho/{idCarrinho}/itens, GET /carrinho/{idCarrinho}.
// SITUAÇÃO: implementada.
@Tag(name = "Carrinhos", description = "Montagem e consulta do carrinho de compras")
@RestController
public class CarrinhoController {

    private final CarrinhoService service;

    public CarrinhoController(CarrinhoService service) {
        this.service = service;
    }

    @Operation(summary = "Abre um carrinho para um cliente")
    @ApiResponse(responseCode = "201", description = "Carrinho aberto, vazio")
    @ApiResponse(responseCode = "400", description = "Corpo inválido",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "O cliente não existe",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping("/carrinho")
    public ResponseEntity<CarrinhoResponseDTO> criar(@Valid @RequestBody CarrinhoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @Operation(summary = "Coloca um produto no carrinho",
            description = "R1: o produto precisa ter estoque > 0 e suficiente. Repetir o produto soma a quantidade.")
    @ApiResponse(responseCode = "200", description = "Item adicionado: o corpo traz o carrinho atualizado")
    @ApiResponse(responseCode = "400", description = "Corpo inválido, carrinho já finalizado ou regra R1 violada",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "O carrinho ou o produto não existe",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping("/carrinho/{idCarrinho}/itens")
    public CarrinhoResponseDTO adicionarItem(
            @Parameter(description = "Id do carrinho", example = "1") @PathVariable Long idCarrinho,
            @Valid @RequestBody ItemCarrinhoRequestDTO dto) {
        return service.adicionarItem(idCarrinho, dto);
    }

    @Operation(summary = "Lista o carrinho (itens e total)")
    @ApiResponse(responseCode = "200", description = "Carrinho encontrado")
    @ApiResponse(responseCode = "404", description = "Não existe carrinho com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/carrinho/{idCarrinho}")
    public CarrinhoResponseDTO buscarPorId(
            @Parameter(description = "Id do carrinho", example = "1") @PathVariable Long idCarrinho) {
        return service.buscarPorId(idCarrinho);
    }
}
