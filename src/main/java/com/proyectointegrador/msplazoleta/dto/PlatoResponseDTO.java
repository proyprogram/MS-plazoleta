package com.proyectointegrador.msplazoleta.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlatoResponseDTO {

    private String nombre;
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    private Boolean activo;
    private String categoria;
}