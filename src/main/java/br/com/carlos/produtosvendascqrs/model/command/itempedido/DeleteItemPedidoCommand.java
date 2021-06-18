package br.com.carlos.produtosvendascqrs.model.command.itempedido;


import java.io.Serializable;

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
public class DeleteItemPedidoCommand implements Serializable {

    @TargetAggregateIdentifier
    private String id;

}
