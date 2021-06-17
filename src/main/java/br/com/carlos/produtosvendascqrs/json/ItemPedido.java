package br.com.carlos.produtosvendascqrs.json;


import java.io.Serializable;

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
public class ItemPedido implements Serializable {

    private String produtoId;
    private Integer quantidade;

}
