package com.proyectointegrador.msplazoleta.pedido.controller;

import com.proyectointegrador.msplazoleta.pedido.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoController pedidoService) {
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
}
