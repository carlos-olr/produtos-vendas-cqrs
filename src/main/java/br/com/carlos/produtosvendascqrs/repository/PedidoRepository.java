package br.com.carlos.produtosvendascqrs.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.produtosvendascqrs.model.entity.Pedido;
import br.com.carlos.produtosvendascqrs.model.entity.Produto;


/**
 * @author carlos.oliveira
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {


}
