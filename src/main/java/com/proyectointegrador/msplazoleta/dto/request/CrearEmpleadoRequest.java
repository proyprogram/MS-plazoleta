package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearEmpleadoRequest {

    @NotNull
    private Long idRestaurante;

    @NotNull
    private Long idPropietario;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String documentoDeIdentidad;

    @NotBlank
    private String celular;

    @NotBlank
    private String correo;

    @NotBlank
    private String clave;
}
