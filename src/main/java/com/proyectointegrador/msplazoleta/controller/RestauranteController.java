package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.response.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.dto.request.CrearRestauranteRequest;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.service.RestauranteServicio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private final RestauranteServicio restauranteServicio;

    public RestauranteController(RestauranteServicio restauranteServicio) {
        this.restauranteServicio = restauranteServicio;
    }

    @PostMapping
    public ResponseEntity<Restaurante> crear(
            @Valid @RequestBody CrearRestauranteRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        Restaurante restaurante = restauranteServicio.registrar(
                request.getNombre(),
                request.getNit(),
                request.getDireccion(),
                request.getTelefono(),
                request.getUrlLogo(),
                request.getIdPropietario(),
                authorization
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }

    @GetMapping
    public Page<RestauranteResponseDTO> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {

        return restauranteServicio.listarRestaurantes(pagina, cantidad);
    }

    // Endpoint interno usado por MS-usuarios para validar, al crear un empleado,
    // quien es el propietario real del restaurante (cierra el hueco de seguridad
    // de POST /usuarios/empleado donde el idPropietario llegaba sin validar).
    @GetMapping("/{id}/propietario")
    public ResponseEntity<Map<String, Object>> consultarPropietario(@PathVariable Long id) {
        Restaurante restaurante = restauranteServicio.buscarPorId(id);
        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante con id " + id + " no existe.");
        }
        return ResponseEntity.ok(Map.of("idPropietario", restaurante.getIdPropietario()));
    }
}
