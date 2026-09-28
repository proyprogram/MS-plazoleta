package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.service.RestauranteServicioV2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteServicioV2 restauranteServicio;

    @GetMapping
    public Page<RestauranteResponseDTO> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int cantidad) {

        return restauranteServicio.listarRestaurantes(pagina, cantidad);
    }
}