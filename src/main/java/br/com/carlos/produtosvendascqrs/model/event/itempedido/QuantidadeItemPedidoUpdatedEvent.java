package br.com.carlos.produtosvendascqrs.model.event.itempedido;


import java.io.Serializable;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.carlos.produtosvendascqrs.model.event.EventoIdentificavel;

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
public class QuantidadeItemPedidoUpdatedEvent extends EventoIdentificavel implements Serializable {

    @TargetAggregateIdentifier
    private String id;
    private Integer quantidade;

}
