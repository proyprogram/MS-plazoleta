package com.proyectointegrador.msplazoleta.controller;

import com.proyectointegrador.msplazoleta.dto.request.CrearEmpleadoRequest;
import com.proyectointegrador.msplazoleta.service.EmpleadoServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoServicio empleadoServicio;

    public EmpleadoController(EmpleadoServicio empleadoServicio) {
        this.empleadoServicio = empleadoServicio;
    }

    @PostMapping
    public ResponseEntity<Map> crear(@Valid @RequestBody CrearEmpleadoRequest request,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        Map response = empleadoServicio.crearEmpleado(request, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleForbidden(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}