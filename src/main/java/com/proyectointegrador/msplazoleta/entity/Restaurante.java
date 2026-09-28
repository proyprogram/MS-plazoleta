package com.proyectointegrador.msplazoleta.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String nit;

    private String telefono;

    @Column(name = "urllogo")
    private String urlLogo;

    @Column(name = "idpropietario")
    private Integer idPropietario;
}