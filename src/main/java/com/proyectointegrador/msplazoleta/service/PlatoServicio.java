package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.entity.Plato;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.repository.PlatoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoServicio {

    private final PlatoRepository platoRepository;
    private final RestauranteServicio restauranteServicio;

    public PlatoServicio(PlatoRepository platoRepository, RestauranteServicio restauranteServicio) {
        this.platoRepository = platoRepository;
        this.restauranteServicio = restauranteServicio;
    }

    public Plato crearPlato(String nombre, Integer precio, String descripcion, String urlImagen, String categoria, Long idRestaurante, Long idPropietario) {
        Restaurante restaurante = restauranteServicio.buscarPorId(idRestaurante);
        if (restaurante == null)
            throw new IllegalArgumentException("El restaurante no existe");
        if (!restaurante.getIdPropietario().equals(idPropietario))
            throw new IllegalArgumentException("Solo el propietario del restaurante puede crear platos");

        Plato plato = Plato.builder()
                .nombre(nombre)
                .precio(precio)
                .descripcion(descripcion)
                .urlImagen(urlImagen)
                .categoria(categoria)
                .restaurante(restaurante)
                .activo(true)
                .build();

        return platoRepository.save(plato);
    }

    public Plato cambiarEstado(Long idPlato, Long idPropietario, boolean nuevoEstado) {
        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new IllegalArgumentException("El plato no existe"));

        if (!plato.getRestaurante().getIdPropietario().equals(idPropietario))
            throw new IllegalArgumentException("Solo el propietario del restaurante puede habilitar o deshabilitar este plato");

        plato.setActivo(nuevoEstado);
        return platoRepository.save(plato);
    }

    public List<Plato> listarTodos() {
        return platoRepository.findAll();
    }
}
