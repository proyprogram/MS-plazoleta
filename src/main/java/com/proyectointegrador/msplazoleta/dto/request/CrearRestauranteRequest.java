package com.proyectointegrador.msplazoleta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearRestauranteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El NIT debe ser únicamente numérico")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^(\\+[0-9]{1,12}|[0-9]{1,13})$",
            message = "El teléfono debe tener máximo 13 caracteres, solo números y puede iniciar con +"
    )
    private String telefono;

    private String urlLogo;

    @NotNull(message = "El id del propietario es obligatorio")
    @Positive(message = "El id del propietario debe ser válido")
    private Long idPropietario;
}
