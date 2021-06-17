package br.com.carlos.produtosvendascqrs.model.entity;


import static java.util.UUID.randomUUID;


/**
 * @author carlos.oliveira
 */
public class GeradorId {

    public static String newId(String tipo) {
        return tipo + "-" + randomUUID().toString();
    }

}
