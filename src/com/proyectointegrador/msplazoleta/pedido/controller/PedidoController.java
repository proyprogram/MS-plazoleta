package com.proyectointegrador.msplazoleta.pedido.controller;

import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.enums.EstadoPedido;
import com.proyectointegrador.msplazoleta.pedido.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
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

    @GetMapping
    public Page<Pedido> listarPedidos(
            @RequestParam Long idRestaurante,
            @RequestParam EstadoPedido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {
        return pedidoService.listarPedidosPorRestauranteYEstado(idRestaurante, estado, pagina, cantidad);
    }
}
