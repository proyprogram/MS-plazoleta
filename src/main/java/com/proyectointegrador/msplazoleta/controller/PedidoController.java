package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.entity.PedidoLog;
import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.security.JwtService;
import com.proyectointegrador.msplazoleta.service.PedidoService;
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

    private final PedidoService pedidoService;
    private final JwtService jwtService;

    public PedidoController(PedidoService pedidoService, JwtService jwtService) {
        this.pedidoService = pedidoService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> datos) {
        try {
            Long idCliente = ((Number) datos.get("idCliente")).longValue();
            Long idRestaurante = ((Number) datos.get("idRestaurante")).longValue();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> platos = (List<Map<String, Object>>) datos.get("platos");

            Pedido pedido = pedidoService.crearPedido(idCliente, idRestaurante, platos);
            return new ResponseEntity<>(pedido, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<?> asignarPedido(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long idEmpleado = jwtService.extractIdUsuario(token);
            Long idRestauranteEmpleado = jwtService.extractIdRestaurante(token);

            Pedido pedido = pedidoService.asignarPedido(id, idEmpleado, idRestauranteEmpleado);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.CONFLICT);
        }
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
            Long idRestauranteEmpleado = jwtService.extractIdRestaurante(token);
            if (idRestauranteEmpleado == null || !idRestauranteEmpleado.equals(idRestaurante)) {
                return new ResponseEntity<>(
                        "Solo puedes consultar los pedidos del restaurante al que perteneces",
                        HttpStatus.FORBIDDEN
                );
            }
        }

        Page<Pedido> pedidos = pedidoService.listarPedidosPorRestauranteYEstado(idRestaurante, estado, pagina, cantidad);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{idPedido}/trazabilidad")
    public ResponseEntity<?> consultarTrazabilidad(
            @PathVariable Long idPedido,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            Long idCliente = jwtService.extractIdUsuario(token);
            List<PedidoLog> historial = pedidoService.consultarTrazabilidad(idPedido, idCliente);
            return ResponseEntity.ok(historial);
        } catch (SecurityException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
