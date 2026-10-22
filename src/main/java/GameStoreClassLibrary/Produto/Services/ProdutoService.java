// Define o pacote da camada de regras de negócio.
package GameStoreClassLibrary.Produto.Services;

import java.util.List;

import GameStoreClassLibrary.Produto.Entities.Exceptions.ProdutoNaoEncontradoException;
import GameStoreDomain.Util.Exceptions.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoRequestDTO;
import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoResponseDTO;
import GameStoreClassLibrary.Produto.Entities.Mappers.ProdutoMapper;
import GameStoreClassLibrary.Produto.Entities.Produto;
import GameStoreClassLibrary.Produto.Repositories.ProdutoRepository;

// CAMADA: Service
// RESPONSABILIDADE: cadastrar, listar, buscar e apagar produtos. Recebe DTO,
//                   usa o mapper e devolve DTO: a entidade não sobe para o Controller.
// REGRAS: R2 (cadastro de mídia sem campos em branco). A validação de formato
//         está no ProdutoRequestDTO; o Service confere de novo porque a regra
//         não pode depender só de quem chama.
// ROTAS ATENDIDAS: POST /produto, GET /produtos, GET /produto/{id}, DELETE /produto/{id}.
// SITUAÇÃO: implementada.
@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // POST /produto: confere R2, grava e devolve o produto com o id gerado.
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        // R2: nenhum campo em branco.
        if (dto.nome() == null || dto.nome().isBlank() || dto.preco() == null || dto.estoque() == null) {
            throw new RegraDeNegocioException("R2: nome, preço e estoque são obrigatórios");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    // GET /produtos: devolve todos os produtos. Sem produtos, a lista vem vazia.
    public List<ProdutoResponseDTO> listarTodos() {
        return mapper.toResponseList(repository.findAll());
    }

    // GET /produto/{id}: devolve um produto ou lança a exceção que vira 404.
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return mapper.toResponse(produto);
    }

    // DELETE /produto/{id}: apaga o produto; 404 se não existir.
    @Transactional
    public void deletar(Long id) {
        Produto produto = repository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        try {
            repository.delete(produto);
            // flush força o DELETE agora, para o erro de chave estrangeira aparecer aqui.
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            // O produto já está em algum carrinho: apagar quebraria o histórico.
            throw new RegraDeNegocioException("O produto " + id + " está em carrinhos e não pode ser apagado");
        }
    }
}
