package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.request.PlatoPedidoRequest;
import com.proyectointegrador.msplazoleta.entity.DetallePedido;
import com.proyectointegrador.msplazoleta.entity.EstadoPedido;
import com.proyectointegrador.msplazoleta.entity.Pedido;
import com.proyectointegrador.msplazoleta.entity.PedidoLog;
import com.proyectointegrador.msplazoleta.entity.Plato;
import com.proyectointegrador.msplazoleta.exception.PedidoNoEncontradoException;
import com.proyectointegrador.msplazoleta.observer.PedidoEstadoObserver;
import com.proyectointegrador.msplazoleta.repository.DetallePedidoRepository;
import com.proyectointegrador.msplazoleta.repository.PedidoRepository;
import com.proyectointegrador.msplazoleta.repository.PlatoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PlatoRepository platoRepository;
    private final PedidoLogService pedidoLogService;
    private final List<PedidoEstadoObserver> observers;
    private final SecureRandom secureRandom = new SecureRandom();

    public PedidoService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            PlatoRepository platoRepository,
            PedidoLogService pedidoLogService,
            List<PedidoEstadoObserver> observers) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.platoRepository = platoRepository;
        this.pedidoLogService = pedidoLogService;
        this.observers = observers;
    }

    // Patron Observer: notifica a todos los observadores registrados
    // (trazabilidad, notificaciones, etc.) cada vez que un pedido cambia de estado.
    private void notificarCambioEstado(
            Pedido pedido,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo,
            Long idActor,
            String token) {

        for (PedidoEstadoObserver observer : observers) {
            observer.onCambioEstado(pedido, estadoAnterior, estadoNuevo, idActor, token);
        }
    }

    @Transactional
    public Pedido crearPedido(
            Long idCliente,
            Long idRestaurante,
            List<PlatoPedidoRequest> platosSolicitados) {

        EstadoPedido[] estadosActivos = {
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO
        };

        Optional<Pedido> pedidoActivo =
                pedidoRepository.findByIdClienteAndEstadoIn(
                        idCliente, estadosActivos);

        if (pedidoActivo.isPresent()) {
            throw new IllegalStateException(
                    "No puedes crear otro pedido. Tienes uno en proceso.");
        }

        if (platosSolicitados == null || platosSolicitados.isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe tener al menos un plato.");
        }

        List<DetallePedido> detallesConfirmados = new ArrayList<>();
        Double total = 0.0;

        for (PlatoPedidoRequest solicitud : platosSolicitados) {
            Long idPlato = solicitud.getIdPlato();
            Integer cantidad = solicitud.getCantidad();

            Optional<Plato> platoOpt = platoRepository.findById(idPlato);

            if (platoOpt.isEmpty()) {
                throw new IllegalArgumentException(
                        "El plato con id " + idPlato + " no existe.");
            }

            Plato platoReal = platoOpt.get();

            if (!platoReal.isActivo()) {
                throw new IllegalArgumentException(
                        "El plato '" + platoReal.getNombre()
                                + "' no está disponible actualmente.");
            }

            Long idRestauranteDelPlato =
                    platoReal.getRestaurante().getId();

            if (!idRestauranteDelPlato.equals(idRestaurante)) {
                throw new IllegalArgumentException(
                        "El plato '" + platoReal.getNombre()
                                + "' no pertenece al restaurante indicado.");
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
        notificarCambioEstado(pedidoGuardado, null, EstadoPedido.PENDIENTE, idCliente, null);

        for (DetallePedido detalle : detallesConfirmados) {
            detalle.setIdPedido(pedidoGuardado.getId());
            detallePedidoRepository.save(detalle);
        }

        pedidoGuardado.setDetalles(detallesConfirmados);
        return pedidoGuardado;
    }

    // HU16: el cliente cancela su pedido solo si esta pendiente.
    @Transactional
    public Pedido cancelarPedido(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "El pedido con id " + idPedido + " no existe."));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new SecurityException(
                    "Solo puedes cancelar tus propios pedidos.");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse.");
        }

        EstadoPedido estadoAnterior = pedido.getEstado();
        pedido.setEstado(EstadoPedido.CANCELADO);
        Pedido guardado = pedidoRepository.save(pedido);
        notificarCambioEstado(guardado, estadoAnterior, EstadoPedido.CANCELADO, idCliente, null);
        return guardado;
    }

    public Page<Pedido> listarPedidosPorRestauranteYEstado(
            Long idRestaurante,
            EstadoPedido estado,
            int pagina,
            int cantidad) {

        Pageable paginaDatos = PageRequest.of(pagina, cantidad);

        Page<Pedido> pedidos =
                pedidoRepository.findByIdRestauranteAndEstado(
                        idRestaurante, estado, paginaDatos);

        for (Pedido pedido : pedidos) {
            List<DetallePedido> detalles =
                    detallePedidoRepository.findByIdPedido(pedido.getId());

            pedido.setDetalles(detalles);
        }

        return pedidos;
    }

    // HU17: el cliente consulta el historial de cambios de estado de su pedido.
    public List<PedidoLog> consultarTrazabilidad(Long idPedido, Long idCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "El pedido con id " + idPedido + " no existe."));

        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new SecurityException(
                    "Solo puedes consultar el historial de tus propios pedidos.");
        }

        return pedidoLogService.historial(idPedido);
    }

    @Transactional
    public Pedido asignarPedido(
            Long idPedido,
            Long idEmpleado,
            Long idRestauranteEmpleado) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "El pedido con id " + idPedido + " no existe."));

        if (!pedido.getIdRestaurante().equals(idRestauranteEmpleado)) {
            throw new SecurityException(
                    "No puedes asignarte un pedido de otro restaurante.");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo puedes asignarte pedidos que estén en estado PENDIENTE.");
        }

        pedido.setIdEmpleadoAsignado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        Pedido guardado = pedidoRepository.save(pedido);
        notificarCambioEstado(guardado, EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, idEmpleado, null);
        return guardado;
    }

    // HU14: el empleado marca como listo un pedido en preparación.
    @Transactional
    public Pedido marcarPedidoListo(
            Long idPedido,
            Long idRestauranteEmpleado,
            Long idEmpleado,
            String token) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "El pedido no existe."));

        if (!pedido.getIdRestaurante().equals(idRestauranteEmpleado)) {
            throw new SecurityException(
                    "No puedes modificar pedidos de otro restaurante.");
        }

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new IllegalStateException(
                    "Solo puedes marcar como listo un pedido en preparación.");
        }

        String pin = String.valueOf(
                secureRandom.nextInt(900000) + 100000);

        pedido.setPinSeguridad(pin);
        pedido.setEstado(EstadoPedido.LISTO);

        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        notificarCambioEstado(pedidoActualizado, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO, idEmpleado, token);

        List<DetallePedido> detalles =
                detallePedidoRepository.findByIdPedido(
                        pedidoActualizado.getId());

        pedidoActualizado.setDetalles(detalles);

        return pedidoActualizado;
    }

    // HU15: el empleado entrega un pedido validando el PIN de seguridad.
    @Transactional
    public Pedido entregarPedido(
            Long idPedido,
            Long idRestauranteEmpleado,
            Long idEmpleado,
            String pinIngresado) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException(
                        "El pedido no existe."));

        if (!pedido.getIdRestaurante().equals(idRestauranteEmpleado)) {
            throw new SecurityException(
                    "No puedes entregar pedidos de otro restaurante.");
        }

        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new IllegalStateException(
                    "Solo puedes entregar pedidos que estén en estado LISTO.");
        }

        if (pedido.getPinSeguridad() == null
                || !pedido.getPinSeguridad().equals(pinIngresado)) {
            throw new IllegalArgumentException(
                    "El PIN de seguridad es incorrecto.");
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);

        Pedido entregado = pedidoRepository.save(pedido);
        notificarCambioEstado(entregado, EstadoPedido.LISTO, EstadoPedido.ENTREGADO, idEmpleado, null);
        return entregado;
    }
}
