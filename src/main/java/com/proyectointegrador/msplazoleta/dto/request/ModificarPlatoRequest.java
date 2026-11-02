package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificarPlatoRequest {

    @NotNull
    @Positive
    private Integer precio;

    @NotBlank
    private String descripcion;

    @NotNull
    @Positive
    private Long idPropietario;
}
