package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.security.JwtService;
import com.proyectointegrador.msplazoleta.service.NotificacionService;
import com.proyectointegrador.msplazoleta.service.PedidoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger log =
            LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;
    private final JwtService jwtService;
    private final NotificacionService notificacionService;

    public PedidoController(
            PedidoService pedidoService,
            JwtService jwtService,
            NotificacionService notificacionService) {
        this.pedidoService = pedidoService;
        this.jwtService = jwtService;
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody Map<String, Object> datos) {

        Long idCliente = ((Number) datos.get("idCliente")).longValue();
        Long idRestaurante = ((Number) datos.get("idRestaurante")).longValue();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> platos =
                (List<Map<String, Object>>) datos.get("platos");

        Pedido pedido = pedidoService.crearPedido(
                idCliente,
                idRestaurante,
                platos
        );

        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> listarPedidos(
            @RequestParam Long idRestaurante,
            @RequestParam EstadoPedido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        String token = authorization.replace("Bearer ", "");
        String rol = jwtService.extractRol(token);

        if ("EMPLEADO".equals(rol)) {
            Long idRestauranteEmpleado =
                    jwtService.extractIdRestaurante(token);

            if (idRestauranteEmpleado == null
                    || !idRestauranteEmpleado.equals(idRestaurante)) {
                throw new SecurityException(
                        "Solo puedes consultar los pedidos del restaurante al que perteneces."
                );
            }
        }

        Page<Pedido> pedidos =
                pedidoService.listarPedidosPorRestauranteYEstado(
                        idRestaurante,
                        estado,
                        pagina,
                        cantidad
                );

        return ResponseEntity.ok(pedidos);
    }

    @PatchMapping("/{id}/listo")
    public ResponseEntity<?> marcarPedidoListo(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        String token = authorization.replace("Bearer ", "");
        String rol = jwtService.extractRol(token);

        if (!"EMPLEADO".equals(rol)) {
            throw new SecurityException(
                    "Solo un empleado puede marcar el pedido como listo."
            );
        }

        Long idRestauranteEmpleado =
                jwtService.extractIdRestaurante(token);

        if (idRestauranteEmpleado == null) {
            throw new SecurityException(
                    "El empleado no tiene un restaurante asociado."
            );
        }

        Pedido pedido = pedidoService.marcarPedidoListo(
                id,
                idRestauranteEmpleado
        );

        try {
            notificacionService.notificarPedidoListo(
                    pedido,
                    authorization
            );
        } catch (Exception e) {
            log.error(
                    "El pedido {} quedó LISTO, pero falló la notificación al cliente.",
                    pedido.getId(),
                    e
            );
        }

        return ResponseEntity.ok(pedido);
    }
}

