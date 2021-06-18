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
public class PedidoProcessor {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @EventHandler
    public void on(PedidoAddedEvent evento) {
        Pedido pedido = new Pedido();
        pedido.setId(evento.getId());

        Pedido pedidoSalvo = this.pedidoRepository.save(pedido);

        final Pedido finalPedidoSalvo = pedidoSalvo;

        finalPedidoSalvo.setItensPedido(
                evento.getItens().stream().map(itemEvento -> this.criarPedidoProduto(finalPedidoSalvo, itemEvento))
                        .collect(Collectors.toList()));

        pedidoSalvo = this.pedidoRepository.save(finalPedidoSalvo);
        log.info("um pedido foi adicionado {}", pedidoSalvo);
    }

    @EventHandler
    public void on(ItemPedidoAddedToPedidoEvent evento) {
        ItemPedidoJson itemEvento = evento.getItem();
        this.findPedidoAndDoStuff(evento, (pedido -> {
            ItemPedido itemPedido = this.criarPedidoProduto(pedido, itemEvento);

            this.itemPedidoRepository.save(itemPedido);
        }));
    }

    private ItemPedido criarPedidoProduto(Pedido pedido, ItemPedidoJson itemPedidoJson) {
        String id = GeradorId.newId("itemPedido");
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(id);

        Produto produto = this.produtoRepository.findById(itemPedidoJson.getProdutoId()).orElseThrow();
        itemPedido.setProduto(produto);

        itemPedido.setQuantidade(itemPedidoJson.getQuantidade());
        itemPedido.setPrecoUnitario(produto.getPreco());
        itemPedido.setPrecoTotal(produto.getPreco().multiply(new BigDecimal(itemPedidoJson.getQuantidade())));

        itemPedido.setPedido(pedido);

        return itemPedido;
    }

    private void findPedidoAndDoStuff(EventoIdentificavel evento, Consumer<Pedido> funcao) {
        Pedido pedido = this.pedidoRepository.findById(evento.getId()).orElseThrow();
        funcao.accept(pedido);
    }

}
