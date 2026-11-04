package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoPlatoRequest {

    /**
     * Se mantiene por compatibilidad, pero se ignora: el id del propietario
     * siempre se toma del token JWT, nunca del body.
     */
    private Long idPropietario;

    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
