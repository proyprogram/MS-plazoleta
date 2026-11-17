package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.entity.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class NotificacionService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificacionService.class);

    private final RestClient restClient;

    public NotificacionService(
            @Value("${ms.usuarios.url}") String usuariosUrl) {

        this.restClient = RestClient.create(usuariosUrl);
    }

    public void notificarPedidoListo(Pedido pedido, String token) {

        // Consultar el celular del cliente en MS-Usuarios
        Map<?, ?> respuesta = restClient.get()
                .uri("/usuarios/{id}/celular", pedido.getIdCliente())
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .body(Map.class);

        if (respuesta == null || respuesta.get("celular") == null) {
            throw new IllegalStateException(
                    "No se pudo obtener el celular del cliente."
            );
        }

        String celular = respuesta.get("celular").toString();

        // Simular el envío del SMS mostrando los datos en consola
        log.info(
                "SMS SIMULADO | Destino: {} | Pedido: {} | PIN: {}",
                celular,
                pedido.getId(),
                pedido.getPinSeguridad()
        );
    }
}
