package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.PedidoLog;
import com.proyectointegrador.msplazoleta.repository.PedidoLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoLogService {

    private final PedidoLogRepository pedidoLogRepository;

    public PedidoLogService(PedidoLogRepository pedidoLogRepository) {
        this.pedidoLogRepository = pedidoLogRepository;
    }

    public PedidoLog registrar(Long idPedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo, Long idUsuario) {
        PedidoLog log = new PedidoLog();
        log.setIdPedido(idPedido);
        log.setEstadoAnterior(estadoAnterior);
        log.setEstadoNuevo(estadoNuevo);
        log.setIdUsuario(idUsuario);
        log.setFecha(LocalDateTime.now());
        return pedidoLogRepository.save(log);
    }

    public List<PedidoLog> historial(Long idPedido) {
        return pedidoLogRepository.findByIdPedidoOrderByFechaAsc(idPedido);
    }
}
