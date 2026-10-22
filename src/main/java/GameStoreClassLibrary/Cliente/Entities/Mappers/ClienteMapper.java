// Define o pacote dos mappers.
package GameStoreClassLibrary.Cliente.Entities.Mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteRequestDTO;
import GameStoreClassLibrary.Cliente.Entities.Dtos.ClienteResponseDTO;
import GameStoreClassLibrary.Cliente.Entities.Cliente;

// CAMADA: Mapper
// RESPONSABILIDADE: converter ClienteRequestDTO em Cliente, e Cliente em ClienteResponseDTO.
// SITUAÇÃO: implementada.
@Component
public class ClienteMapper {

    // POST /cliente: monta um cliente novo (id = null, saldo = 0).
    public Cliente toEntity(ClienteRequestDTO dto) {
        return new Cliente(dto.nome(), dto.email());
    }

    // Toda resposta: copia da entidade só o que o contrato da API mostra.
    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getSaldo());
    }

    // GET /clientes: converte cada cliente da lista.
    public List<ClienteResponseDTO> toResponseList(List<Cliente> clientes) {
        return clientes.stream().map(this::toResponse).toList();
    }
}
