package com.proyectointegrador.msplazoleta.repository;

import com.proyectointegrador.msplazoleta.entity.PedidoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoLogRepository extends JpaRepository<PedidoLog, Long> {

    List<PedidoLog> findByIdPedidoOrderByFechaAsc(Long idPedido);
}
