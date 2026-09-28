package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.dto.request.CrearRestauranteRequest;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.service.RestauranteServicio;
import com.proyectointegrador.msplazoleta.service.RestauranteServicioV2;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private final RestauranteServicio restauranteServicio;
    private final RestauranteServicioV2 restauranteServicioV2;

    public RestauranteController(RestauranteServicio restauranteServicio, RestauranteServicioV2 restauranteServicioV2) {
        this.restauranteServicio = restauranteServicio;
        this.restauranteServicioV2 = restauranteServicioV2;
    }

    @PostMapping
    public ResponseEntity<Restaurante> crear(@Valid @RequestBody CrearRestauranteRequest request) {
        Restaurante restaurante = restauranteServicio.registrar(
                request.getNombre(),
                request.getNit(),
                request.getDireccion(),
                request.getTelefono(),
                request.getIdPropietario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }

    @GetMapping
    public Page<RestauranteResponseDTO> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {

        return restauranteServicioV2.listarRestaurantes(pagina, cantidad);
    }
}
