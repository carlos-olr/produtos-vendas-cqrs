package br.com.carlos.produtosvendascqrs.json;


import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * @author carlos.oliveira
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PedidoJson implements Serializable {

    private List<ItemPedido> itens;

}
