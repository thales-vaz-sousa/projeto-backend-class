// Define o pacote dos mappers.
package GameStoreClassLibrary.Carrinho.Entities.Mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import GameStoreClassLibrary.Carrinho.Entities.Dtos.CarrinhoResponseDTO;
import GameStoreClassLibrary.Carrinho.Entities.Dtos.ItemCarrinhoResponseDTO;
import GameStoreClassLibrary.Carrinho.Entities.Carrinho;
import GameStoreClassLibrary.Carrinho.Entities.ItemCarrinho;

// CAMADA: Mapper
// RESPONSABILIDADE: converter Carrinho e seus itens em DTOs de saída.
//                   Também é usado pelo CompraMapper, para listar o que foi comprado.
// SITUAÇÃO: implementada.
@Component
public class CarrinhoMapper {

    // Uma linha do carrinho.
    public ItemCarrinhoResponseDTO toItemResponse(ItemCarrinho item) {
        return new ItemCarrinhoResponseDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getProduto().getPreco(),
                item.getQuantidade(),
                item.getSubtotal());
    }

    // Todas as linhas de um carrinho.
    public List<ItemCarrinhoResponseDTO> toItensResponse(Carrinho carrinho) {
        return carrinho.getItens().stream().map(this::toItemResponse).toList();
    }

    // Toda resposta de carrinho: dono, itens e total.
    public CarrinhoResponseDTO toResponse(Carrinho carrinho) {
        return new CarrinhoResponseDTO(
                carrinho.getId(),
                carrinho.getCliente().getId(),
                carrinho.getCliente().getNome(),
                carrinho.getDataCriacao(),
                toItensResponse(carrinho),
                carrinho.calcularTotal(),
                carrinho.isFinalizado());
    }
}
