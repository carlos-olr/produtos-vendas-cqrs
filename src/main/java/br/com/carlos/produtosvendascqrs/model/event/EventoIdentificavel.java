package br.com.carlos.produtosvendascqrs.model.event;


import java.io.Serializable;


/**
 * @author carlos.oliveira
 */
public abstract class EventoIdentificavel implements Serializable {

    public abstract String getId();

}
