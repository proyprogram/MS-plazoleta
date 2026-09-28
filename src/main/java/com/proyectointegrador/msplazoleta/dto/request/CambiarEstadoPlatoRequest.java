package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoPlatoRequest {

    @NotNull(message = "El id del propietario es obligatorio")
    private Long idPropietario;

    @NotNull(message = "El estado (activo) es obligatorio")
    private Boolean activo;
}
