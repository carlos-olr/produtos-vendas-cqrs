package br.com.carlos.produtosvendascqrs.controller;


import static java.util.UUID.randomUUID;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.produtosvendascqrs.json.ProdutoJson;
import br.com.carlos.produtosvendascqrs.model.command.produto.AddProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.command.produto.UpdatePrecoProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.entity.Produto;
import br.com.carlos.produtosvendascqrs.repository.ProdutoRepository;

import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private ProdutoRepository produtoRepository;

    @PutMapping
    public CompletableFuture<String> criarProduto(@RequestBody ProdutoJson json) {
        var command = new AddProdutoCommand(randomUUID().toString(), json.getNome(), json.getPreco());
        log.info("Executing command: {}", command);

        return this.commandGateway.send(command);
    }

    @PutMapping("/{id}")
    public CompletableFuture<String> atualizarPreco(@PathVariable String id, @RequestBody ProdutoJson json) {
        var command = new UpdatePrecoProdutoCommand(id, json.getNome(), json.getPreco());
        log.info("Executing command: {}", command);
        return this.commandGateway.send(command);
    }

    @GetMapping
    public ResponseEntity<Iterable<Produto>> buscar() {
        return ResponseEntity.ok(this.produtoRepository.findAll());
    }

}
