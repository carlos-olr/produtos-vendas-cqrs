package br.com.carlos.produtosvendascqrs.controller;


import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.produtosvendascqrs.json.ItemPedidoJson;
import br.com.carlos.produtosvendascqrs.json.PedidoJson;
import br.com.carlos.produtosvendascqrs.model.command.pedido.AddItemPedidoToPedidoCommand;
import br.com.carlos.produtosvendascqrs.model.command.pedido.AddPedidoCommand;
import br.com.carlos.produtosvendascqrs.model.entity.GeradorId;
import br.com.carlos.produtosvendascqrs.model.entity.Pedido;
import br.com.carlos.produtosvendascqrs.repository.PedidoRepository;

import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private PedidoRepository pedidoRepository;

    @PutMapping
    public String criarPedido(@RequestBody PedidoJson json) {
        String id = GeradorId.newId("pedido");
        var command = new AddPedidoCommand(id, json.getItens());
        log.info("Executing command: {}", command);

        this.commandGateway.send(command);

        return id;
    }

    @PostMapping("/{id}/item")
    public String adicionarItemPedido(@PathVariable String id, @RequestBody ItemPedidoJson json) {
        var command = new AddItemPedidoToPedidoCommand(id, json);
        log.info("Executing command: {}", command);
        this.commandGateway.send(command);
        return id;
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Pedido> buscar() {
        return this.pedidoRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Pedido buscarPorId(@PathVariable String id) {
        return this.pedidoRepository.findById(id).orElse(null);
    }

}
