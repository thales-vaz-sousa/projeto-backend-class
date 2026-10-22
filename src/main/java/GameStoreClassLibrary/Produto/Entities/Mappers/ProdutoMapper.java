// Define o pacote dos mappers.
package GameStoreClassLibrary.Produto.Entities.Mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoRequestDTO;
import GameStoreClassLibrary.Produto.Entities.Dtos.ProdutoResponseDTO;
import GameStoreClassLibrary.Produto.Entities.Produto;

// CAMADA: Mapper
// RESPONSABILIDADE: converter ProdutoRequestDTO em Produto, e Produto em ProdutoResponseDTO.
// SITUAÇÃO: implementada.
@Component
public class ProdutoMapper {

    // POST /produto: monta um produto novo a partir do DTO (id = null, o banco gera).
    public Produto toEntity(ProdutoRequestDTO dto) {
        return new Produto(dto.nome(), dto.preco(), dto.estoque());
    }

    // Toda resposta: copia da entidade só o que o contrato da API mostra.
    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getEstoque());
    }

    // GET /produtos: converte cada produto da lista.
    public List<ProdutoResponseDTO> toResponseList(List<Produto> produtos) {
        return produtos.stream().map(this::toResponse).toList();
    }
}
