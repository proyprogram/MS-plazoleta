package com.proyectointegrador.msplazoleta.observer;

import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Observador concreto: cuando el pedido pasa a LISTO, notifica al cliente por SMS (HU-14).
// Si la notificacion falla, no afecta el cambio de estado que ya se guardo.
@Component
public class NotificacionObserver implements PedidoEstadoObserver {

    private static final Logger log =
            LoggerFactory.getLogger(NotificacionObserver.class);

    private final NotificacionService notificacionService;

    public NotificacionObserver(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Override
    public void onCambioEstado(
            Pedido pedido,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo,
            Long idActor,
            String token) {

        if (estadoNuevo != EstadoPedido.LISTO) {
            return;
        }

        try {
            notificacionService.notificarPedidoListo(pedido, token);
        } catch (Exception e) {
            log.error(
                    "El pedido {} quedo LISTO, pero fallo la notificacion al cliente.",
                    pedido.getId(),
                    e
            );
        }
    }
}
