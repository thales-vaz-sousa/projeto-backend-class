// Define o pacote onde as entidades ficam organizadas.
package api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: uma mídia física de jogo à venda, com preço e estoque,
//                   gravada na tabela produtos (migration V2).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "produtos")
public class Produto {

    // Chave primária gerada pelo banco no INSERT.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da mídia.
    @Column(nullable = false)
    private String nome;

    // Preço unitário em reais.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    // Unidades em estoque.
    @Column(nullable = false)
    private int estoque;

    // Construtor sem argumentos exigido pelo Hibernate.
    protected Produto() {
    }

    // Construtor de cadastro: id = null (o banco gera no INSERT).
    public Produto(String nome, BigDecimal preco, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    // R1: só tem estoque para a quantidade pedida se o estoque for > 0 e cobrir o pedido.
    public boolean temEstoquePara(int quantidade) {
        return estoque > 0 && estoque >= quantidade;
    }

    // Tira unidades do estoque. Quem chama (CompraService) já conferiu a regra R1.
    public void baixarEstoque(int quantidade) {
        this.estoque -= quantidade;
    }
}
