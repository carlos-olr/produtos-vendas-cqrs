package br.com.carlos.produtosvendascqrs.model.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Produto implements Serializable {

    @Id
    private String id;
    private String nome;
    private BigDecimal preco;

}
