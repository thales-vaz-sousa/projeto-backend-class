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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import GameStoreDomain.Util.Validations.ErroDTO;
import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoRequestDTO;
import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoResponseDTO;
import GameStoreClassLibrary.Produto.Services.ProdutoService;

// CAMADA: Controller
// RESPONSABILIDADE: receber as requisições de produtos, validar o DTO de
//                   entrada e devolver o status certo. Não tem regra de negócio.
// ROTAS ATENDIDAS: POST /produto, GET /produtos, GET /produto/{id}, DELETE /produto/{id}.
// SITUAÇÃO: implementada.
@Tag(name = "Produtos", description = "Cadastro e consulta das mídias físicas de jogos")
@RestController
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastra um produto", description = "R2: nenhum campo pode vir em branco. O id é gerado pela API.")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado: o corpo já traz o id gerado")
    @ApiResponse(responseCode = "400", description = "Corpo inválido (R2): a lista \"campos\" diz o que corrigir",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @PostMapping("/produto")
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @Operation(summary = "Lista os produtos", description = "Sem produtos cadastrados, a lista vem vazia, não 404.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos (pode vir vazia)")
    @GetMapping("/produtos")
    public List<ProdutoResponseDTO> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Busca um produto pelo id")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Não existe produto com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @GetMapping("/produto/{id}")
    public ProdutoResponseDTO buscarPorId(@Parameter(description = "Id do produto", example = "1") @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Deleta um produto pelo id")
    @ApiResponse(responseCode = "200", description = "Produto deletado")
    @ApiResponse(responseCode = "400", description = "O produto já está em carrinhos e não pode ser apagado",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @ApiResponse(responseCode = "404", description = "Não existe produto com esse id",
            content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    @DeleteMapping("/produto/{id}")
    public void deletar(@Parameter(description = "Id do produto", example = "1") @PathVariable Long id) {
        service.deletar(id);
    }
}
