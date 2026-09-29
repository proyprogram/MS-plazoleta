package com.proyectointegrador.msplazoleta.pedido.service;

import com.proyectointegrador.msplazoleta.pedido.entity.DetallePedido;
import com.proyectointegrador.msplazoleta.pedido.entity.Pedido;
import com.proyectointegrador.msplazoleta.pedido.enums.EstadoPedido;
import com.proyectointegrador.msplazoleta.pedido.repository.DetallePedidoRepository;
import com.proyectointegrador.msplazoleta.pedido.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Transactional
    public Pedido crearPedido(Long idCliente, Long idRestaurante, List<Map<String, Object>> platos) {
        EstadoPedido[] estadosActivos = {
            EstadoPedido.PENDIENTE,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO
        };
        Optional<Pedido> pedidoActivo = pedidoRepository.findByIdClienteAndEstadoIn(idCliente, estadosActivos);
        if (pedidoActivo.isPresent()) {
            throw new IllegalStateException("No puedes crear otro pedido. Tienes uno en proceso.");
        }

        if (platos == null || platos.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un plato.");
        }

        Double total = 0.0;
        for (Map<String, Object> p : platos) {
            Double precio = (Double) p.get("precioUnitario");
            Integer cantidad = (Integer) p.get("cantidad");
            total += precio * cantidad;
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setIdRestaurante(idRestaurante);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
        nuevoPedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        for (Map<String, Object> p : platos) {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdPedido(pedidoGuardado.getId());
            detalle.setIdPlato(((Number) p.get("idPlato")).longValue());
            detalle.setCantidad((Integer) p.get("cantidad"));
            detalle.setPrecioUnitario((Double) p.get("precioUnitario"));
            detallePedidoRepository.save(detalle);
        }

        return pedidoGuardado;
    }

    public Page<Pedido> listarPedidosPorRestauranteYEstado(Long idRestaurante, EstadoPedido estado, int pagina, int cantidad) {
        Pageable paginaDatos = PageRequest.of(pagina, cantidad);
        Page<Pedido> pedidos = pedidoRepository.findByIdRestauranteAndEstado(idRestaurante, estado, paginaDatos);
        for (Pedido p : pedidos) {
            List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(p.getId());
            p.setDetalles(detalles);
        }
        return pedidos;
    }
}
