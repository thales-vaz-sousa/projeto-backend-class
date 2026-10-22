// Define o pacote da camada de regras de negócio.
package GameStoreClassLibrary.Compra.Services;

import java.math.BigDecimal;
import java.time.LocalDate;

import GameStoreClassLibrary.Carrinho.Entities.Exceptions.CarrinhoNaoEncontradoException;
import GameStoreDomain.Util.Exceptions.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import GameStoreClassLibrary.Compra.Entities.Dtos.CompraRequestDTO;
import GameStoreClassLibrary.Compra.Entities.Dtos.CompraResponseDTO;
import GameStoreClassLibrary.Compra.Entities.Mappers.CompraMapper;
import GameStoreClassLibrary.Carrinho.Entities.Carrinho;
import GameStoreClassLibrary.Cliente.Entities.Cliente;
import GameStoreClassLibrary.Compra.Entities.Compra;
import GameStoreClassLibrary.Carrinho.Entities.ItemCarrinho;
import GameStoreClassLibrary.Produto.Entities.Produto;
import GameStoreClassLibrary.Carrinho.Repositories.CarrinhoRepository;
import GameStoreClassLibrary.Compra.Repositories.CompraRepository;

// CAMADA: Service
// RESPONSABILIDADE: finalizar a compra de um carrinho: conferir as regras,
//                   descontar o saldo do cliente e baixar o estoque.
// REGRAS: R1 (todo produto precisa ter estoque > 0 e suficiente) e
//         R3 (o cliente só compra com saldo >= total).
// ROTAS ATENDIDAS: POST /compra.
// SITUAÇÃO: implementada.
@Service
public class CompraService {

    private final CompraRepository repository;
    private final CarrinhoRepository carrinhoRepository;
    private final CompraMapper mapper;

    public CompraService(CompraRepository repository, CarrinhoRepository carrinhoRepository,
                         CompraMapper mapper) {
        this.repository = repository;
        this.carrinhoRepository = carrinhoRepository;
        this.mapper = mapper;
    }

    // POST /compra. @Transactional: cobrar o saldo, baixar o estoque e gravar a
    // compra acontecem juntos; se qualquer regra falhar, nada fica gravado.
    @Transactional
    public CompraResponseDTO finalizar(CompraRequestDTO dto) {
        // 404 se o carrinho não existir.
        Carrinho carrinho = carrinhoRepository.findById(dto.carrinhoId())
                .orElseThrow(() -> new CarrinhoNaoEncontradoException(dto.carrinhoId()));

        // Um carrinho não é pago duas vezes.
        if (carrinho.isFinalizado()) {
            throw new RegraDeNegocioException("O carrinho " + carrinho.getId() + " já foi finalizado");
        }
        // Não existe compra de carrinho vazio.
        if (carrinho.getItens().isEmpty()) {
            throw new RegraDeNegocioException("O carrinho " + carrinho.getId() + " está vazio");
        }

        // R1: confere o estoque de cada item antes de mexer em qualquer coisa.
        for (ItemCarrinho item : carrinho.getItens()) {
            Produto produto = item.getProduto();
            if (!produto.temEstoquePara(item.getQuantidade())) {
                throw new RegraDeNegocioException("R1: o produto " + produto.getNome()
                        + " não tem estoque suficiente (disponível: " + produto.getEstoque() + ")");
            }
        }

        // R3: o saldo precisa cobrir o total.
        BigDecimal total = carrinho.calcularTotal();
        Cliente cliente = carrinho.getCliente();
        if (!cliente.temSaldoPara(total)) {
            throw new RegraDeNegocioException("R3: o cliente " + cliente.getNome()
                    + " não possui saldo suficiente para efetuar a compra (saldo: " + cliente.getSaldo()
                    + ", total: " + total + ")");
        }

        // Todas as regras passaram: baixa o estoque, cobra o saldo e fecha o carrinho.
        for (ItemCarrinho item : carrinho.getItens()) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        cliente.debitar(total);
        carrinho.finalizar();

        // A data da compra quem decide é a API.
        Compra salva = repository.save(new Compra(carrinho, cliente, total, LocalDate.now()));
        return mapper.toResponse(salva);
    }
}
