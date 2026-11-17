package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoPedidoRequest {

    @NotNull(message = "El id del plato es obligatorio")
    @Positive(message = "El id del plato debe ser válido")
    private Long idPlato;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
}
