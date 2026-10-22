// Define o pacote responsável pelas rotas HTTP da aplicação.
package GameStoreApi.Controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteRequestDTO;
import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteResponseDTO;
import GameStoreDomain.Util.Validations.ErroDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.SaldoClienteRequestDTO;
import GameStoreClassLibrary.Cliente.Services.ClienteService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as requisições de clientes e de saldo.
// ROTAS ATENDIDAS: POST /cliente, GET /clientes, GET /cliente/{id}, PUT /saldo-cliente.
// SITUAÇÃO: implementada.
@Tag(name = "Clientes", description = "Cadastro de clientes e saldo")
@RestController
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra um cliente", description = "O saldo nasce zerado; use PUT /saldo-cliente para carregar.")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado: o corpo já traz o id gerado")
    @ApiResponse(responseCode = "400", description = "Corpo inválido: a lista \"campos\" diz o que corrigir",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping("/cliente")
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @Operation(summary = "Lista os clientes", description = "Sem clientes cadastrados, a lista vem vazia, não 404.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes (pode vir vazia)")
    @GetMapping("/clientes")
    public List<ClienteResponseDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Busca um cliente pelo id")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Não existe cliente com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/cliente/{id}")
    public ClienteResponseDTO buscarPorId(@Parameter(description = "Id do cliente", example = "1") @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Carrega saldo de um cliente", description = "O valor é somado ao saldo atual.")
    @ApiResponse(responseCode = "200", description = "Saldo atualizado: o corpo traz o saldo novo")
    @ApiResponse(responseCode = "400", description = "Corpo inválido (valor ausente ou não positivo)",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "Não existe cliente com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PutMapping("/saldo-cliente")
    public ClienteResponseDTO adicionarSaldo(@Valid @RequestBody SaldoClienteRequestDTO dto) {
        return service.adicionarSaldo(dto);
    }
}
