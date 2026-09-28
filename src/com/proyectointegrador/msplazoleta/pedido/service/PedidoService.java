package com.proyectointegrador.msplazoleta.pedido.service;

import com.proyectointegrador.msplazoleta.pedido.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.enums.EstadoPedido;
import com.proyectointegrador.msplazoleta.pedido.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoService pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido crearPedido(Long idCliente, Long idRestaurante, Double total) {
        EstadoPedido[] estadosActivos = {
            EstadoPedido.PENDIENTE,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO
        };
        Optional<Pedido> pedidoActivo = pedidoRepository.findByIdClienteAndEstadoIn(idCliente, estadosActivos);
        if (pedidoActivo.isPresent()) {
            throw new IllegalStateException("No puedes crear otro pedido. Tienes uno en proceso.");
        }
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setIdRestaurante(idRestaurante);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
        nuevoPedido.setTotal(total);
        return pedidoRepository.save(nuevoPedido);
    }

    public Page<Pedido> listarPedidosPorEstado(Long idRestaurante, EstadoPedido estado, int pagina, int cantidad) {
        Pageable paginaDatos = PageRequest.of(pagina, cantidad);
        return pedidoRepository.findByIdRestauranteAndEstado(idRestaurante, estado, paginaDatos);
    }
}
