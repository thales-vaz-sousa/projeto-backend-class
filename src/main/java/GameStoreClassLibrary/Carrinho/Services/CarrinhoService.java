// Define o pacote da camada de regras de negócio.
package GameStoreClassLibrary.Carrinho.Services;

import java.time.LocalDate;

import GameStoreClassLibrary.Carrinho.Entities.Exceptions.CarrinhoNaoEncontradoException;
import GameStoreClassLibrary.Compra.Entities.Exceptions.ClienteNaoEncontradoException;
import GameStoreClassLibrary.Produto.Entities.Exceptions.ProdutoNaoEncontradoException;
import GameStoreDomain.Util.Exceptions.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import GameStoreClassLibrary.Carrinho.Entities.Dtos.CarrinhoRequestDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.CarrinhoResponseDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.ItemCarrinhoRequestDTO;
import GameStoreClassLibrary.Carrinho.Entities.Mappers.CarrinhoMapper;
import GameStoreClassLibrary.Carrinho.Entities.Carrinho;
import GameStoreClassLibrary.Cliente.Entities.Cliente;
import GameStoreClassLibrary.Produto.Entities.Produto;
import GameStoreClassLibrary.Carrinho.Repositories.CarrinhoRepository;
import GameStoreClassLibrary.Cliente.Repositories.ClienteRepository;
import GameStoreClassLibrary.Produto.Repositories.ProdutoRepository;

// CAMADA: Service
// RESPONSABILIDADE: abrir o carrinho de um cliente, pôr produtos nele e consultá-lo.
// REGRAS: R1 (só entra no carrinho produto com estoque). A conferência final
//         acontece de novo no CompraService, porque o estoque pode mudar entre
//         pôr no carrinho e pagar.
// ROTAS ATENDIDAS: POST /carrinho, POST /carrinho/{id}/itens, GET /carrinho/{id}.
// SITUAÇÃO: implementada.
@Service
public class CarrinhoService {

    private final CarrinhoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoMapper mapper;

    public CarrinhoService(CarrinhoRepository repository, ClienteRepository clienteRepository,
                           ProdutoRepository produtoRepository, CarrinhoMapper mapper) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    // POST /carrinho: abre um carrinho vazio para o cliente (404 se ele não existir).
    @Transactional
    public CarrinhoResponseDTO criar(CarrinhoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException(dto.clienteId()));
        // A data de criação quem decide é a API.
        Carrinho salvo = repository.save(new Carrinho(cliente, LocalDate.now()));
        return mapper.toResponse(salvo);
    }

    // POST /carrinho/{id}/itens: põe um produto no carrinho.
    @Transactional
    public CarrinhoResponseDTO adicionarItem(Long carrinhoId, ItemCarrinhoRequestDTO dto) {
        Carrinho carrinho = buscarEntidade(carrinhoId);
        // Carrinho já pago não aceita mais itens.
        if (carrinho.isFinalizado()) {
            throw new RegraDeNegocioException("O carrinho " + carrinhoId + " já foi finalizado");
        }
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException(dto.produtoId()));

        // R1: estoque > 0 e suficiente para o que já está no carrinho mais o novo pedido.
        int totalPedido = carrinho.quantidadeDoProduto(produto.getId()) + dto.quantidade();
        if (produto.getEstoque() <= 0) {
            throw new RegraDeNegocioException("R1: o produto " + produto.getNome() + " está sem estoque");
        }
        if (!produto.temEstoquePara(totalPedido)) {
            throw new RegraDeNegocioException("R1: o produto " + produto.getNome()
                    + " não tem estoque suficiente (disponível: " + produto.getEstoque() + ")");
        }

        carrinho.adicionarItem(produto, dto.quantidade());
        return mapper.toResponse(carrinho);
    }

    // GET /carrinho/{id}: devolve o carrinho com os itens e o total, ou 404.
    @Transactional(readOnly = true)
    public CarrinhoResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    // Busca a entidade ou lança a exceção que vira 404.
    private Carrinho buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new CarrinhoNaoEncontradoException(id));
    }
}
