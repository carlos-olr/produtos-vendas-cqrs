package br.com.carlos.produtosvendascqrs.controller;


import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.produtosvendascqrs.model.command.itempedido.DeleteItemPedidoCommand;
import br.com.carlos.produtosvendascqrs.model.command.itempedido.UpdateQuantidadeItemPedidoCommand;

import lombok.extern.slf4j.Slf4j;


/**
 * @author carlos.oliveira
 */
@Slf4j
@RestController
@RequestMapping("/itempedido")
public class ItemPedidoController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/{id}/quantidade")
    public String atualizarQuantidadeDoItemPedido(@PathVariable String id, @RequestBody Integer quantidade) {
        var command = new UpdateQuantidadeItemPedidoCommand(id, quantidade);
        log.info("Executing command: {}", command);
        this.commandGateway.send(command);
        return id;
    }

    @DeleteMapping("/{id}")
    public String deletarItemPedido(@PathVariable String id) {
        var command = new DeleteItemPedidoCommand(id);
        log.info("Executing command: {}", command);
        this.commandGateway.send(command);
        return id;
    }

}
