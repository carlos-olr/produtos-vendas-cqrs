package br.com.carlos.produtosvendascqrs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.produtosvendascqrs.model.entity.ItemPedido;


/**
 * @author carlos.oliveira
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, String> {


}
