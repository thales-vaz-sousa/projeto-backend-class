// Define o pacote onde as entidades ficam organizadas.
package api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: o conjunto de produtos que um cliente pretende comprar,
//                   gravado na tabela carrinhos (migration V3). Os itens ficam
//                   em itens_carrinho (migration V4).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "carrinhos")
public class Carrinho {

    // Chave primária gerada pelo banco no INSERT.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos carrinhos podem ser do mesmo cliente.
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Dia em que o carrinho foi aberto; quem preenche é a API.
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    // Verdadeiro depois que a compra é finalizada.
    @Column(nullable = false)
    private boolean finalizado = false;

    // Os itens pertencem ao carrinho: gravar o carrinho grava os itens.
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    // Construtor sem argumentos exigido pelo Hibernate.
    protected Carrinho() {
    }

    // Construtor de cadastro: carrinho novo, vazio e em aberto.
    public Carrinho(Cliente cliente, LocalDate dataCriacao) {
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    // Lista só para leitura: para mudar, use adicionarItem.
    public List<ItemCarrinho> getItens() {
        return Collections.unmodifiableList(itens);
    }

    // Quantas unidades deste produto já estão no carrinho (0 se não está).
    public int quantidadeDoProduto(Long produtoId) {
        return itens.stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .mapToInt(ItemCarrinho::getQuantidade)
                .sum();
    }

    // Põe o produto no carrinho; se já está, soma a quantidade na mesma linha.
    public void adicionarItem(Produto produto, int quantidade) {
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.adicionarQuantidade(quantidade);
                return;
            }
        }
        itens.add(new ItemCarrinho(this, produto, quantidade));
    }

    // Soma o subtotal de todos os itens.
    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemCarrinho::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Marca o carrinho como comprado. Quem chama é o CompraService.
    public void finalizar() {
        this.finalizado = true;
    }
}
