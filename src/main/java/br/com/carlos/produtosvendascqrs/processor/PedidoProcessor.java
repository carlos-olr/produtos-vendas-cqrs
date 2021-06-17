package br.com.carlos.produtosvendascqrs.processor;


import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlos.produtosvendascqrs.json.ItemPedido;
import br.com.carlos.produtosvendascqrs.model.entity.GeradorId;
import br.com.carlos.produtosvendascqrs.model.entity.Pedido;
import br.com.carlos.produtosvendascqrs.model.entity.PedidoProduto;
import br.com.carlos.produtosvendascqrs.model.entity.Produto;
import br.com.carlos.produtosvendascqrs.model.event.EventoIdentificavel;
import br.com.carlos.produtosvendascqrs.model.event.pedido.ItemPedidoAddedToPedidoEvent;
import br.com.carlos.produtosvendascqrs.model.event.pedido.PedidoAddedEvent;
import br.com.carlos.produtosvendascqrs.repository.PedidoProdutoRepository;
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
    private PedidoProdutoRepository pedidoProdutoRepository;

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
        ItemPedido itemEvento = evento.getItem();
        this.findPedidoAndDoStuff(evento, (pedido -> {
            PedidoProduto pedidoProduto = this.criarPedidoProduto(pedido, itemEvento);

            this.pedidoProdutoRepository.save(pedidoProduto);
        }));
    }

    //    @EventHandler
    //    public void on(PrecoProdutoUpdatedEvent evento) {
    //        this.findProdutoAndDoStuff(evento, (produto) -> {
    //            produto.setPreco(evento.getPreco());
    //            this.produtoRepository.save(produto);
    //            log.info("um produto teve o preço atualizado {}", produto);
    //        });
    //    }
    //
    //    @EventHandler
    //    public void on(NomeProdutoUpdatedEvent evento) {
    //        this.findProdutoAndDoStuff(evento, (produto) -> {
    //            produto.setNome(evento.getNome());
    //            this.produtoRepository.save(produto);
    //            log.info("um produto teve o nome atualizado {}", produto);
    //        });
    //    }

    private PedidoProduto criarPedidoProduto(Pedido pedido, ItemPedido itemPedido) {
        String id = GeradorId.newId("pedidoProduto");
        PedidoProduto pedidoProduto = new PedidoProduto();
        pedidoProduto.setId(id);

        Produto produto = this.produtoRepository.findById(itemPedido.getProdutoId()).orElseThrow();
        pedidoProduto.setProduto(produto);

        pedidoProduto.setQuantidade(itemPedido.getQuantidade());
        pedidoProduto.setPrecoUnitario(produto.getPreco());
        pedidoProduto.setPrecoTotal(produto.getPreco().multiply(new BigDecimal(itemPedido.getQuantidade())));

        pedidoProduto.setPedido(pedido);

        return pedidoProduto;
    }

    private void findPedidoAndDoStuff(EventoIdentificavel evento, Consumer<Pedido> funcao) {
        Pedido pedido = this.pedidoRepository.findById(evento.getId()).orElseThrow();
        funcao.accept(pedido);
    }

}
