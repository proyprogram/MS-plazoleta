package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.response.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.repository.RestauranteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteServicio {

    private final RestauranteRepository restauranteRepository;

    public RestauranteServicio(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante registrar(String nombre, String nit, String direccion, String telefono, Long idPropietario) {
        Restaurante r = Restaurante.builder()
                .nombre(nombre)
                .nit(nit)
                .direccion(direccion)
                .telefono(telefono)
                .urlLogo("")
                .idPropietario(idPropietario)
                .build();
        return restauranteRepository.save(r);
    }

    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id).orElse(null);
    }

    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

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
