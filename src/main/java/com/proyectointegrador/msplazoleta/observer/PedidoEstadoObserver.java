package com.proyectointegrador.msplazoleta.observer;

import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;

public interface PedidoEstadoObserver {

    void onCambioEstado(
            Pedido pedido,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo,
            Long idActor,
            String token);
}
