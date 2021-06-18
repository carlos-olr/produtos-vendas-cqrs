package br.com.carlos.produtosvendascqrs.model.command.pedido;


import java.io.Serializable;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.carlos.produtosvendascqrs.json.ItemPedidoJson;

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
public class AddPedidoCommand implements Serializable {

    @TargetAggregateIdentifier
    private String id;
    private List<ItemPedidoJson> itens;

}
