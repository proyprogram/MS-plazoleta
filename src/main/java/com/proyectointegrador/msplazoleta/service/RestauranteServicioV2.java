package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestauranteServicioV2 {

    private final RestauranteRepository restauranteRepository;

    public Page<RestauranteResponseDTO> listarRestaurantes(int pagina, int cantidad) {

        Pageable pageable = PageRequest.of(
                pagina,
                cantidad,
                Sort.by("nombre").ascending()
        );

        Page<Restaurante> restaurantes = restauranteRepository.findAll(pageable);

        return restaurantes.map(restaurante ->
                new RestauranteResponseDTO(
                        restaurante.getNombre(),
                        restaurante.getUrlLogo()
                )
        );
    }
}
