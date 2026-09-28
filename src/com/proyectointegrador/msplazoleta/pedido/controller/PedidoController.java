package com.proyectointegrador.msplazoleta.pedido.controller;

import com.proyectointegrador.msplazoleta.pedido.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.enums.EstadoPedido;
import com.proyectointegrador.msplazoleta.pedido.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestParam Long idCliente,
            @RequestParam Long idRestaurante,
            @RequestParam Double total) {
        try {
            Pedido pedido = pedidoService.crearPedido(idCliente, idRestaurante, total);
            return new ResponseEntity<>(pedido, HttpStatus.CREATED);
        } catch (IllegalStateException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public Page<Pedido> listarPedidos(
            @RequestParam Long idRestaurante,
            @RequestParam EstadoPedido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {
        return pedidoService.listarPedidosPorEstado(idRestaurante, estado, pagina, cantidad);
    }
}
