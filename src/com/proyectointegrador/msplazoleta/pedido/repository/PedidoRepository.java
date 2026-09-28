package com.proyectointegrador.msplazoleta.pedido.repository;

import com.proyectointegrador.msplazoleta.pedido.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByIdClienteAndEstadoIn(Long idCliente, EstadoPedido... estados);
}
