package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.dto.request.CrearEmpleadoRequest;
import com.proyectointegrador.msplazoleta.entity.Restaurante;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmpleadoServicio {

    private static final String URL_MS_USUARIOS = "http://localhost:8081/usuarios/empleado";

    private final RestauranteServicio restauranteServicio;
    private final RestTemplate restTemplate;

    public EmpleadoServicio(RestauranteServicio restauranteServicio, RestTemplate restTemplate) {
        this.restauranteServicio = restauranteServicio;
        this.restTemplate = restTemplate;
    }

    public Map crearEmpleado(CrearEmpleadoRequest request, String authorization) {

        Restaurante restaurante = restauranteServicio.buscarPorId(request.getIdRestaurante());

        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante no existe");
        }

        if (!restaurante.getIdPropietario().equals(request.getIdPropietario())) {
            throw new IllegalStateException("Solo el propietario del restaurante puede crear empleados");
        }

        Map<String, Object> body = Map.of(
                "nombre", request.getNombre(),
                "apellido", request.getApellido(),
                "documentoDeIdentidad", request.getDocumentoDeIdentidad(),
                "celular", request.getCelular(),
                "correo", request.getCorreo(),
                "clave", request.getClave(),
                "idRestaurante", request.getIdRestaurante()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(URL_MS_USUARIOS, entity, Map.class);
    }
}