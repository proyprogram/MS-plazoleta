package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.response.PlatoResponseDTO;
import com.proyectointegrador.msplazoleta.entity.Plato;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.repository.PlatoRepository;
import com.proyectointegrador.msplazoleta.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class PlatoServicio {

    private final PlatoRepository platoRepository;
    private final RestauranteServicio restauranteServicio;
    private final JwtService jwtService;

    public PlatoServicio(PlatoRepository platoRepository, RestauranteServicio restauranteServicio, JwtService jwtService) {
        this.platoRepository = platoRepository;
        this.restauranteServicio = restauranteServicio;
        this.jwtService = jwtService;
    }

    private Long extraerIdPropietarioDelToken(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtService.extractIdUsuario(token);
    }

    public Plato crearPlato(String nombre, Integer precio, String descripcion, String urlImagen, String categoria, Long idRestaurante, String authorization) {
        Long idPropietarioToken = extraerIdPropietarioDelToken(authorization);

        Restaurante restaurante = restauranteServicio.buscarPorId(idRestaurante);
        if (restaurante == null)
            throw new IllegalArgumentException("El restaurante no existe");
        if (!restaurante.getIdPropietario().equals(idPropietarioToken))
            throw new SecurityException("Solo el propietario del restaurante puede crear platos");
        if (platoRepository.existsByNombreIgnoreCaseAndRestaurante_Id(nombre, idRestaurante))
            throw new IllegalStateException("Ya existe un plato con este nombre en este restaurante");

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

    public Plato cambiarEstado(Long idPlato, String authorization, boolean nuevoEstado) {
        Long idPropietarioToken = extraerIdPropietarioDelToken(authorization);

        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new IllegalArgumentException("El plato no existe"));

        if (!plato.getRestaurante().getIdPropietario().equals(idPropietarioToken))
            throw new SecurityException("Solo el propietario del restaurante puede habilitar o deshabilitar este plato");

        plato.setActivo(nuevoEstado);
        return platoRepository.save(plato);
    }

    public List<Plato> listarTodos() {
        return platoRepository.findAll();
    }

    public Page<PlatoResponseDTO> listarPlatos(
            Long idRestaurante,
            String categoria,
            int pagina,
            int cantidad) {

        Pageable pageable = PageRequest.of(pagina, cantidad);

        Page<Plato> platos;

        if (categoria != null && !categoria.isBlank()) {
            platos = platoRepository.findByRestaurante_IdAndCategoriaIgnoreCaseAndActivoTrue(
                    idRestaurante,
                    categoria,
                    pageable
            );
        } else {
            platos = platoRepository.findByRestaurante_IdAndActivoTrue(
                    idRestaurante,
                    pageable
            );
        }

        return platos.map(plato ->
                PlatoResponseDTO.builder()
                        .nombre(plato.getNombre())
                        .descripcion(plato.getDescripcion())
                        .precio(plato.getPrecio())
                        .urlImagen(plato.getUrlImagen())
                        .activo(plato.isActivo())
                        .categoria(plato.getCategoria())
                        .build()
        );
    }

    public Plato modificarPlato(Long idPlato, Integer nuevoPrecio, String nuevaDescripcion, String authorization) {
        Long idPropietarioToken = extraerIdPropietarioDelToken(authorization);

        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new IllegalArgumentException("El plato indicado no existe"));

        if (!plato.getRestaurante().getIdPropietario().equals(idPropietarioToken)) {
            throw new SecurityException("Solo el propietario del restaurante puede modificar este plato");
        }

        if (!plato.isActivo()) {
            throw new IllegalStateException("No se puede modificar un plato inactivo");
        }

        plato.setPrecio(nuevoPrecio);
        plato.setDescripcion(nuevaDescripcion);

        return platoRepository.save(plato);
    }

}
