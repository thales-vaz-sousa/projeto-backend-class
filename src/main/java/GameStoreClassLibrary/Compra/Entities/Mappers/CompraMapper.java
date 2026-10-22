// Define o pacote dos mappers.
package GameStoreClassLibrary.Compra.Entities.Mappers;

import GameStoreClassLibrary.Carrinho.Entities.Mappers.CarrinhoMapper;
import org.springframework.stereotype.Component;

import GameStoreClassLibrary.Compra.Entities.Dtos.CompraResponseDTO;
import GameStoreClassLibrary.Compra.Entities.Compra;

// CAMADA: Mapper
// RESPONSABILIDADE: converter Compra em CompraResponseDTO, reaproveitando o
//                   CarrinhoMapper para listar os itens comprados.
// SITUAÇÃO: implementada.
@Component
public class CompraMapper {

    // Monta os itens da compra.
    private final CarrinhoMapper carrinhoMapper;

    // O Spring injeta o CarrinhoMapper.
    public CompraMapper(CarrinhoMapper carrinhoMapper) {
        this.carrinhoMapper = carrinhoMapper;
    }

    // Toda resposta de compra.
    public CompraResponseDTO toResponse(Compra compra) {
        return new CompraResponseDTO(
                compra.getId(),
                compra.getCliente().getNome(),
                carrinhoMapper.toItensResponse(compra.getCarrinho()),
                compra.getValorTotal(),
                compra.getDataCompra());
    }
}
