package com.proyectointegrador.msplazoleta.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;

/**
 * HU3: el precio de un plato debe ser un numero entero. Por defecto Jackson
 * acepta un valor decimal (ej. 15000.50) y lo trunca silenciosamente a
 * Integer. Deshabilitamos ACCEPT_FLOAT_AS_INT para que ese caso lance
 * InvalidFormatException (manejada como 400 por HttpMessageNotReadableException
 * en el GlobalExceptionHandler) en lugar de aceptarse.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> builder.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
    }
}
