// Define o pacote da camada de regras de negócio.
package GameStoreClassLibrary.Cliente.Services;

import java.util.List;

import GameStoreClassLibrary.Compra.Entities.Exceptions.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteRequestDTO;
import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteResponseDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.SaldoClienteRequestDTO;
import GameStoreClassLibrary.Cliente.Entities.Mappers.ClienteMapper;
import GameStoreClassLibrary.Cliente.Entities.Cliente;
import GameStoreClassLibrary.Cliente.Repositories.ClienteRepository;

// CAMADA: Service
// RESPONSABILIDADE: cadastrar, listar e buscar clientes, e carregar saldo.
// REGRAS: nenhuma específica de cliente. A regra do saldo (R3) mora no CompraService.
// ROTAS ATENDIDAS: POST /cliente, GET /clientes, GET /cliente/{id}, PUT /saldo-cliente.
// SITUAÇÃO: implementada.
@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // POST /cliente: monta a entidade, grava e devolve o cliente com o id gerado.
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    // GET /clientes: devolve todos os clientes. Sem clientes, a lista vem vazia.
    public List<ClienteResponseDTO> listarTodos() {
        return mapper.toResponseList(repository.findAll());
    }

    // GET /cliente/{id}: devolve um cliente ou lança a exceção que vira 404.
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
        return mapper.toResponse(cliente);
    }

    // PUT /saldo-cliente: soma o valor ao saldo do cliente.
    @Transactional
    public ClienteResponseDTO adicionarSaldo(SaldoClienteRequestDTO dto) {
        Cliente cliente = repository.findById(dto.clienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException(dto.clienteId()));
        cliente.adicionarSaldo(dto.valor());
        // Dentro do @Transactional, o Hibernate grava a mudança sozinho.
        return mapper.toResponse(cliente);
    }
}
