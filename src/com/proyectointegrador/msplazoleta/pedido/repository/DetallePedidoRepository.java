package com.proyectointegrador.msplazoleta.pedido.repository;

import com.proyectointegrador.msplazoleta.pedido.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByIdPedido(Long idPedido);
}
