package com.proyectointegrador.msplazoleta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarPedidoNoEncontrado(
            PedidoNoEncontradoException error) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "fecha", LocalDateTime.now().toString(),
                        "estado", HttpStatus.NOT_FOUND.value(),
                        "error", "No encontrado",
                        "mensaje", error.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException error) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "fecha", LocalDateTime.now().toString(),
                        "estado", HttpStatus.BAD_REQUEST.value(),
                        "error", "Solicitud incorrecta",
                        "mensaje", error.getMessage()
                ));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(
            SecurityException error) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "fecha", LocalDateTime.now().toString(),
                        "estado", HttpStatus.FORBIDDEN.value(),
                        "error", "Acceso denegado",
                        "mensaje", error.getMessage()
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoInvalido(
            IllegalStateException error) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "fecha", LocalDateTime.now().toString(),
                        "estado", HttpStatus.CONFLICT.value(),
                        "error", "Conflicto",
                        "mensaje", error.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            Exception error) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "fecha", LocalDateTime.now().toString(),
                        "estado", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Error interno del servidor",
                        "mensaje", "Ocurrió un error interno en el servidor."
                ));
    }
}



