// Define o pacote onde as entidades ficam organizadas.
package GameStoreClassLibrary.Cliente.Entities;

// Importa o tipo numérico exato usado para dinheiro.
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: uma pessoa cadastrada na loja, com saldo para comprar,
//                   gravada na tabela clientes (migration V1).
// SITUAÇÃO: implementada.
@Entity
@Table(name = "clientes")
public class Cliente {

    // Chave primária gerada pelo banco no INSERT.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do cliente.
    @Column(nullable = false)
    private String nome;

    // E-mail do cliente.
    @Column(nullable = false)
    private String email;

    // Saldo em reais. Quem muda é a API (recarga e compra), nunca o cliente direto.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    // Construtor sem argumentos exigido pelo Hibernate.
    protected Cliente() {
    }

    // Construtor de cadastro: id = null (o banco gera) e saldo = 0.
    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    // Recarga: soma o valor ao saldo. Quem valida que o valor é positivo é o DTO.
    public void adicionarSaldo(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    // Diz se o saldo cobre o valor informado (base da regra R3).
    public boolean temSaldoPara(BigDecimal valor) {
        return saldo.compareTo(valor) >= 0;
    }

    // Cobra o valor do saldo. Quem chama (CompraService) já conferiu a regra R3.
    public void debitar(BigDecimal valor) {
        this.saldo = this.saldo.subtract(valor);
    }
}
