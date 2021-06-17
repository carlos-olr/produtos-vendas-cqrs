package br.com.carlos.produtosvendascqrs.aggregate;


import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import br.com.carlos.produtosvendascqrs.model.command.produto.AddProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.command.produto.UpdateNomeProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.command.produto.UpdatePrecoProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.event.produto.NomeProdutoUpdatedEvent;
import br.com.carlos.produtosvendascqrs.model.event.produto.PrecoProdutoUpdatedEvent;
import br.com.carlos.produtosvendascqrs.model.event.produto.ProdutoAddedEvent;

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
public class ProdutoAggregate implements Serializable {

    @AggregateIdentifier
    private String id;
    private String nome;
    private BigDecimal preco;

    @CommandHandler
    public ProdutoAggregate(AddProdutoCommand comando) {
        log.info("Handling {} command: {}", comando.getClass().getSimpleName(), comando);
        notNull(comando.getId(), "Id não pode ser null");
        hasLength(comando.getNome(), "Nome não pode ser nulo");
        notNull(comando.getPreco(), "Preço não pode ser nulo");
        Assert.isTrue(comando.getPreco().compareTo(BigDecimal.ZERO) > 0, "Preço não pode ser R$ 0 ou menor");

        apply(new ProdutoAddedEvent(comando.getId(), comando.getNome(), comando.getPreco()));
        log.info("Done handling {} command: {}", comando.getClass().getSimpleName(), comando);
    }

    @EventSourcingHandler
    public void on(ProdutoAddedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);

        this.id = event.getId();
        this.nome = event.getNome();
        this.preco = event.getPreco();

        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    //-------------------------------------------------------------------------------------------------------//

    @CommandHandler
    public ProdutoAggregate(UpdatePrecoProdutoCommand comando) {
        log.info("Handling {} command: {}", comando.getClass().getSimpleName(), comando);
        notNull(comando.getId(), "código não pode ser null");

        apply(new PrecoProdutoUpdatedEvent(comando.getId(), comando.getPreco()));
        log.info("Done handling {} command: {}", comando.getClass().getSimpleName(), comando);
    }

    @EventSourcingHandler
    public void on(PrecoProdutoUpdatedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);

        this.id = event.getId();
        this.preco = event.getPreco();

        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    //-------------------------------------------------------------------------------------------------------//

    @CommandHandler
    public ProdutoAggregate(UpdateNomeProdutoCommand comando) {
        log.info("Handling {} command: {}", comando.getClass().getSimpleName(), comando);
        notNull(comando.getId(), "código não pode ser null");
        hasLength(comando.getNome(), "nome não pode estar em branco");

        apply(new NomeProdutoUpdatedEvent(comando.getId(), comando.getNome()));
        log.info("Done handling {} command: {}", comando.getClass().getSimpleName(), comando);
    }

    @EventSourcingHandler
    public void on(NomeProdutoUpdatedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);

        this.id = event.getId();
        this.nome = event.getNome();

        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

}
