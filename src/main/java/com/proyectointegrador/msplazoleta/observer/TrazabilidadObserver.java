package com.proyectointegrador.msplazoleta.observer;

import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.service.PedidoLogService;
import org.springframework.stereotype.Component;

// Observador concreto: registra cada cambio de estado en el historial (HU-17).
@Component
public class TrazabilidadObserver implements PedidoEstadoObserver {

    private final PedidoLogService pedidoLogService;

    public TrazabilidadObserver(PedidoLogService pedidoLogService) {
        this.pedidoLogService = pedidoLogService;
    }

    @Override
    public void onCambioEstado(
            Pedido pedido,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo,
            Long idActor,
            String token) {

        pedidoLogService.registrar(
                pedido.getId(),
                estadoAnterior,
                estadoNuevo,
                idActor
        );
    }
}
