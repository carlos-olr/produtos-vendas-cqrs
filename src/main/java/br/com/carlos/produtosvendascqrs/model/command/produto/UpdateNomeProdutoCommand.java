package br.com.carlos.produtosvendascqrs.model.command.produto;


import java.io.Serializable;
import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * @author carlos.oliveira
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNomeProdutoCommand implements Serializable {

    @TargetAggregateIdentifier
    private String id;
    private String nome;

}
