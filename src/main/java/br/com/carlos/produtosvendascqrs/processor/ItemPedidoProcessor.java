package br.com.carlos.produtosvendascqrs.processor;


import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlos.produtosvendascqrs.json.ItemPedidoJson;
import br.com.carlos.produtosvendascqrs.model.entity.GeradorId;
import br.com.carlos.produtosvendascqrs.model.entity.Pedido;
import br.com.carlos.produtosvendascqrs.model.entity.ItemPedido;
import br.com.carlos.produtosvendascqrs.model.entity.Produto;
import br.com.carlos.produtosvendascqrs.model.event.EventoIdentificavel;
import br.com.carlos.produtosvendascqrs.model.event.itempedido.ItemPedidoDeletedEvent;
import br.com.carlos.produtosvendascqrs.model.event.itempedido.QuantidadeItemPedidoUpdatedEvent;
import br.com.carlos.produtosvendascqrs.model.event.pedido.ItemPedidoAddedToPedidoEvent;
import br.com.carlos.produtosvendascqrs.model.event.pedido.PedidoAddedEvent;
import br.com.carlos.produtosvendascqrs.repository.ItemPedidoRepository;
import br.com.carlos.produtosvendascqrs.repository.PedidoRepository;
import br.com.carlos.produtosvendascqrs.repository.ProdutoRepository;

import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@Component
@ProcessingGroup("amqpEvents")
public class ItemPedidoProcessor {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @EventHandler
    public void on(QuantidadeItemPedidoUpdatedEvent evento) {
        Integer quantidade = evento.getQuantidade();
        this.findItemPedidoAndDoStuff(evento, (itemPedido -> {
            itemPedido.setQuantidade(quantidade);

            BigDecimal precoUnitario = itemPedido.getPrecoUnitario();
            itemPedido.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));

            this.itemPedidoRepository.save(itemPedido);
        }));
    }

    @EventHandler
    public void on(ItemPedidoDeletedEvent evento) {
        this.findItemPedidoAndDoStuff(evento, (itemPedido -> {
            this.itemPedidoRepository.delete(itemPedido);
        }));
    }

    private void findItemPedidoAndDoStuff(EventoIdentificavel evento, Consumer<ItemPedido> funcao) {
        ItemPedido itemPedido = this.itemPedidoRepository.findById(evento.getId()).orElseThrow();
        funcao.accept(itemPedido);
    }

}
