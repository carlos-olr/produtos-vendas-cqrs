package br.com.carlos.produtosvendascqrs.controller;


import static java.util.UUID.randomUUID;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.produtosvendascqrs.json.ProdutoJson;
import br.com.carlos.produtosvendascqrs.model.command.produto.AddProdutoCommand;
import br.com.carlos.produtosvendascqrs.model.command.produto.UpdateNomeProdutoCommand;
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
    public String criarProduto(@RequestBody ProdutoJson json) {
        String id = randomUUID().toString();
        var command = new AddProdutoCommand(id, json.getNome(), json.getPreco());
        log.info("Executing command: {}", command);

        this.commandGateway.send(command);

        return id;
    }

    @PostMapping("/{id}/preco")
    public String atualizarPreco(@PathVariable String id, @RequestBody ProdutoJson json) {
        var command = new UpdatePrecoProdutoCommand(id, json.getPreco());
        log.info("Executing command: {}", command);
        this.commandGateway.send(command);
        return id;
    }

    @PostMapping("/{id}/nome")
    public String atualizarNome(@PathVariable String id, @RequestBody ProdutoJson json) {
        var command = new UpdateNomeProdutoCommand(id, json.getNome());
        log.info("Executing command: {}", command);
        this.commandGateway.send(command);
        return id;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Produto> buscar() {
        return this.produtoRepository.findAll();
    }

}
