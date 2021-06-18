package br.com.carlos.produtosvendascqrs.aggregate;


import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.carlos.produtosvendascqrs.model.command.itempedido.DeleteItemPedidoCommand;
import br.com.carlos.produtosvendascqrs.model.command.itempedido.UpdateQuantidadeItemPedidoCommand;
import br.com.carlos.produtosvendascqrs.model.event.itempedido.ItemPedidoDeletedEvent;
import br.com.carlos.produtosvendascqrs.model.event.itempedido.QuantidadeItemPedidoUpdatedEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@Getter
@Setter
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoAggregate implements Serializable {

    @AggregateIdentifier
    private String id;
    private Integer quantidade;

    @CommandHandler
    public ItemPedidoAggregate(UpdateQuantidadeItemPedidoCommand comando) {
        log.info("Handling {} command: {}", comando.getClass().getSimpleName(), comando);
        notNull(comando.getId(), "Id não pode ser null");
        isTrue(comando.getQuantidade() != null && comando.getQuantidade() > 0, "A quantidade não pode estar vazia");

        apply(new QuantidadeItemPedidoUpdatedEvent(comando.getId(), comando.getQuantidade()));
        log.info("Done handling {} command: {}", comando.getClass().getSimpleName(), comando);
    }

    @EventSourcingHandler
    public void on(QuantidadeItemPedidoUpdatedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);

        this.id = event.getId();
        this.quantidade = event.getQuantidade();

        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    //-------------------------------------------------------------------------------------------------------//

    @CommandHandler
    public ItemPedidoAggregate(DeleteItemPedidoCommand comando) {
        log.info("Handling {} command: {}", comando.getClass().getSimpleName(), comando);
        notNull(comando.getId(), "Id não pode ser null");

        apply(new ItemPedidoDeletedEvent(comando.getId()));
        log.info("Done handling {} command: {}", comando.getClass().getSimpleName(), comando);
    }

    @EventSourcingHandler
    public void on(ItemPedidoDeletedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);

        this.id = event.getId();

        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    //-------------------------------------------------------------------------------------------------------//

}
