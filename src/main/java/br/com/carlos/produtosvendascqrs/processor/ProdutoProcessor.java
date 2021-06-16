package br.com.carlos.produtosvendascqrs.processor;


import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.carlos.produtosvendascqrs.model.entity.Produto;
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
    public void on(ProdutoAddedEvent event) {
        Produto novoProduo = new Produto(event.getId(), event.getNome(), event.getPreco());
        Produto produtoSalvo = this.produtoRepository.save(novoProduo);
        log.info("um produto foi adicionado {}", produtoSalvo);
    }

    @EventHandler
    public void on(PrecoProdutoUpdatedEvent event) {
        Produto produto = this.produtoRepository.findById(event.getId()).orElseThrow();
        produto.setPreco(event.getPreco());
        this.produtoRepository.save(produto);
        log.info("um produto teve o preço atualizado {}", produto);
    }

}
