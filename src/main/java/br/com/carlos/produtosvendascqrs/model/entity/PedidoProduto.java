package br.com.carlos.produtosvendascqrs.model.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author carlos.oliveira
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProduto implements Serializable {

    @Id
    private String id;

    @JsonIgnoreProperties({"preco"})
    @JoinColumn(name = "PRODUTO_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Produto produto;

    @JsonIgnore
    @JoinColumn(name = "PEDIDO_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Pedido.class)
    private Pedido pedido;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private BigDecimal precoTotal;

    @Column(nullable = false)
    private Integer quantidade;

}
