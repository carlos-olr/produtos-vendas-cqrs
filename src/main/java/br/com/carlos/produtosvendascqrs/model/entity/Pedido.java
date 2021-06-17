package br.com.carlos.produtosvendascqrs.model.entity;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class Pedido implements Serializable {

    @Id
    private String id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "pedido", fetch = FetchType.EAGER)
    private List<PedidoProduto> itensPedido;

}
