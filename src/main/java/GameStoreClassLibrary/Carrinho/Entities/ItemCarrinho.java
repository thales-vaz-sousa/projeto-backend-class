// Define o pacote onde as entidades ficam organizadas.
package GameStoreClassLibrary.Carrinho.Entities;

import java.math.BigDecimal;

import GameStoreClassLibrary.Produto.Entities.Produto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: uma linha do carrinho: um produto e quantas unidades,
//                   gravada na tabela itens_carrinho (migration V4).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Carrinho que contém este item.
    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    // Produto escolhido.
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Quantidade de unidades.
    @Column(nullable = false)
    private int quantidade;

    protected ItemCarrinho() {
    }

    public ItemCarrinho(Carrinho carrinho, Produto produto, int quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    // Soma unidades a este item.
    public void adicionarQuantidade(int mais) {
        this.quantidade += mais;
    }

    // Preço unitário vezes quantidade.
    public BigDecimal getSubtotal() {
        return produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }
}
