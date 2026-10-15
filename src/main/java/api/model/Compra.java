// Define o pacote onde as entidades ficam organizadas.
package api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: o registro de um carrinho pago, gravado na tabela compras
//                   (migration V5). Guarda o total cobrado e o dia da compra.
// SITUAÇÃO: implementada.
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada carrinho vira, no máximo, uma compra.
    @OneToOne
    @JoinColumn(name = "carrinho_id", nullable = false, unique = true)
    private Carrinho carrinho;

    // Um cliente faz várias compras.
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Total cobrado do saldo (calculado e conferido pelo CompraService).
    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    // Dia da compra; quem decide é a API.
    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    protected Compra() {
    }

    public Compra(Carrinho carrinho, Cliente cliente, BigDecimal valorTotal, LocalDate dataCompra) {
        this.carrinho = carrinho;
        this.cliente = cliente;
        this.valorTotal = valorTotal;
        this.dataCompra = dataCompra;
    }

    public Long getId() {
        return id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }
}
