package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.entity.DetallePedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.entity.PedidoLog;
import com.proyectointegrador.msplazoleta.entity.Plato;
import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.repository.DetallePedidoRepository;
import com.proyectointegrador.msplazoleta.repository.PedidoRepository;
import com.proyectointegrador.msplazoleta.repository.PlatoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PlatoRepository platoRepository;
    private final PedidoLogService pedidoLogService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            PlatoRepository platoRepository,
            PedidoLogService pedidoLogService) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.platoRepository = platoRepository;
        this.pedidoLogService = pedidoLogService;
    }

    @Transactional
    public Pedido crearPedido(Long idCliente, Long idRestaurante, List<Map<String, Object>> platosSolicitados) {
        EstadoPedido[] estadosActivos = {
            EstadoPedido.PENDIENTE,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO
        };
        Optional<Pedido> pedidoActivo = pedidoRepository.findByIdClienteAndEstadoIn(idCliente, estadosActivos);
        if (pedidoActivo.isPresent()) {
            throw new IllegalStateException("No puedes crear otro pedido. Tienes uno en proceso.");
        }

        if (platosSolicitados == null || platosSolicitados.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un plato.");
        }

        List<DetallePedido> detallesConfirmados = new ArrayList<>();
        Double total = 0.0;

        for (Map<String, Object> solicitud : platosSolicitados) {
            Long idPlato = ((Number) solicitud.get("idPlato")).longValue();
            Integer cantidad = ((Number) solicitud.get("cantidad")).intValue();

            Optional<Plato> platoOpt = platoRepository.findById(idPlato);
            if (platoOpt.isEmpty()) {
                throw new IllegalArgumentException("El plato con id " + idPlato + " no existe.");
            }
            Plato platoReal = platoOpt.get();

            Long idRestauranteDelPlato = platoReal.getRestaurante().getId();
            if (!idRestauranteDelPlato.equals(idRestaurante)) {
                throw new IllegalArgumentException(
                    "El plato '" + platoReal.getNombre() + "' no pertenece al restaurante indicado."
                );
            }

            Double precioReal = platoReal.getPrecio().doubleValue();
            total += precioReal * cantidad;

            DetallePedido detalle = new DetallePedido();
            detalle.setIdPlato(idPlato);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioReal);
            detallesConfirmados.add(detalle);
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setIdRestaurante(idRestaurante);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
        nuevoPedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        pedidoLogService.registrar(pedidoGuardado.getId(), null, EstadoPedido.PENDIENTE, idCliente);

        for (DetallePedido detalle : detallesConfirmados) {
            detalle.setIdPedido(pedidoGuardado.getId());
            detallePedidoRepository.save(detalle);
        }

        pedidoGuardado.setDetalles(detallesConfirmados);
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

    @Transactional
    public Pedido asignarPedido(Long idPedido, Long idEmpleado, Long idRestauranteEmpleado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con id " + idPedido + " no existe."));

        if (!pedido.getIdRestaurante().equals(idRestauranteEmpleado)) {
            throw new IllegalArgumentException("No puedes asignarte un pedido de otro restaurante.");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo puedes asignarte pedidos que estén en estado PENDIENTE.");
        }

        pedido.setIdEmpleadoAsignado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        Pedido guardado = pedidoRepository.save(pedido);
        pedidoLogService.registrar(guardado.getId(), EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, idEmpleado);
        return guardado;
    }

    public List<PedidoLog> consultarTrazabilidad(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido con id " + idPedido + " no existe."));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new SecurityException("Solo puedes consultar el historial de tus propios pedidos.");
        }

        return pedidoLogService.historial(idPedido);
    }
}
