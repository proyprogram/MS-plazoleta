package com.proyectointegrador.msplazoleta.repository;

import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.enums.EstadoPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByIdClienteAndEstadoIn(Long idCliente, EstadoPedido... estados);
    Page<Pedido> findByIdRestauranteAndEstado(Long idRestaurante, EstadoPedido estado, Pageable pageable);
}
