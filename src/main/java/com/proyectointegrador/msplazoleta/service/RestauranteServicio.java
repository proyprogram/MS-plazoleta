package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.response.RestauranteResponseDTO;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import com.proyectointegrador.msplazoleta.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RestauranteServicio {

    private final RestauranteRepository restauranteRepository;
    private final RestTemplate restTemplate;

    @Value("${ms.usuarios.url:http://localhost:8081}")
    private String msUsuariosUrl;

    public RestauranteServicio(RestauranteRepository restauranteRepository, RestTemplate restTemplate) {
        this.restauranteRepository = restauranteRepository;
        this.restTemplate = restTemplate;
    }

    public Restaurante registrar(String nombre, String nit, String direccion, String telefono, String urlLogo, Long idPropietario, String authorization) {

        if (restauranteRepository.existsByNit(nit)) {
            throw new IllegalArgumentException("Ya existe un restaurante registrado con el NIT " + nit + ".");
        }

        String rol = consultarRolDelUsuario(idPropietario, authorization);
        if (!"PROPIETARIO".equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("El usuario indicado como propietario debe tener el rol PROPIETARIO.");
        }

        Restaurante r = Restaurante.builder()
                .nombre(nombre)
                .nit(nit)
                .direccion(direccion)
                .telefono(telefono)
                .urlLogo(urlLogo == null ? "" : urlLogo)
                .idPropietario(idPropietario)
                .build();
        return restauranteRepository.save(r);
    }

    private String consultarRolDelUsuario(Long idUsuario, String authorization) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = msUsuariosUrl + "/usuarios/" + idUsuario + "/rol";

            Map respuesta = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

            if (respuesta == null || respuesta.get("rol") == null) {
                throw new IllegalArgumentException("El usuario indicado como propietario no existe.");
            }

            return respuesta.get("rol").toString();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("El usuario indicado como propietario no existe.");
        }
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
