package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.request.CambiarEstadoPlatoRequest;
import com.proyectointegrador.msplazoleta.dto.request.CrearPlatoRequest;
import com.proyectointegrador.msplazoleta.entity.Plato;
import com.proyectointegrador.msplazoleta.service.PlatoServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    private final PlatoServicio platoServicio;

    public PlatoController(PlatoServicio platoServicio) {
        this.platoServicio = platoServicio;
    }

    @PostMapping
    public ResponseEntity<Plato> crear(@Valid @RequestBody CrearPlatoRequest request) {
        Plato plato = platoServicio.crearPlato(
                request.getNombre(),
                request.getPrecio(),
                request.getDescripcion(),
                request.getUrlImagen(),
                request.getCategoria(),
                request.getIdRestaurante(),
                request.getIdPropietario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(plato);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Plato> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoPlatoRequest request) {
        Plato plato = platoServicio.cambiarEstado(id, request.getIdPropietario(), request.getActivo());
        return ResponseEntity.ok(plato);
    }
}
