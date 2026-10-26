package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.response.PlatoResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoPlatoRequest request) {

        try {
            Plato plato = platoServicio.cambiarEstado(
                    id,
                    request.getIdPropietario(),
                    request.getActivo()
            );

            return ResponseEntity.ok(plato);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public Page<PlatoResponseDTO> listarPlatos(
            @RequestParam Long idRestaurante,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {

        return platoServicio.listarPlatos(
                idRestaurante,
                categoria,
                pagina,
                cantidad
        );
    }
}
