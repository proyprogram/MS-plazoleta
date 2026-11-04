package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrearPedidoRequest {

    @NotNull(message = "El id del restaurante es obligatorio")
    @Positive(message = "El id del restaurante debe ser válido")
    private Long idRestaurante;

    @NotEmpty(message = "El pedido debe tener al menos un plato")
    @Valid
    private List<PlatoPedidoRequest> platos;

    /**
     * Se mantiene por compatibilidad, pero se ignora: el id del cliente que
     * realiza el pedido siempre se toma del token JWT, nunca del body.
     */
    private Long idCliente;
}
