package br.com.carlos.produtosvendascqrs.processor;


import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlos.produtosvendascqrs.model.entity.Produto;
import br.com.carlos.produtosvendascqrs.model.event.EventoIdentificavel;
import br.com.carlos.produtosvendascqrs.model.event.produto.NomeProdutoUpdatedEvent;
import br.com.carlos.produtosvendascqrs.model.event.produto.PrecoProdutoUpdatedEvent;
import br.com.carlos.produtosvendascqrs.model.event.produto.ProdutoAddedEvent;
import br.com.carlos.produtosvendascqrs.repository.ProdutoRepository;

import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@Component
@ProcessingGroup("amqpEvents")
public class ProdutoProcessor {

    @Autowired
    private ProdutoRepository produtoRepository;

    @EventHandler
    public void on(ProdutoAddedEvent evento) {
        Produto novoProduo = new Produto(evento.getId(), evento.getNome(), evento.getPreco());
        Produto produtoSalvo = this.produtoRepository.save(novoProduo);
        log.info("um produto foi adicionado {}", produtoSalvo);
    }

    @EventHandler
    public void on(PrecoProdutoUpdatedEvent evento) {
        this.findProdutoAndDoStuff(evento, (produto) -> {
            produto.setPreco(evento.getPreco());
            this.produtoRepository.save(produto);
            log.info("um produto teve o preço atualizado {}", produto);
        });
    }

    @EventHandler
    public void on(NomeProdutoUpdatedEvent evento) {
        this.findProdutoAndDoStuff(evento, (produto) -> {
            produto.setNome(evento.getNome());
            this.produtoRepository.save(produto);
            log.info("um produto teve o nome atualizado {}", produto);
        });
    }

    private void findProdutoAndDoStuff(EventoIdentificavel evento, Consumer<Produto> funcao) {
        Produto produto = this.produtoRepository.findById(evento.getId()).orElseThrow();
        funcao.accept(produto);
    }

}
