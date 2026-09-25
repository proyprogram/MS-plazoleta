package com.proyectointegrador.msplazoleta;

public class PruebaRestaurante {

    public static void main(String[] args) {

        Usuario usuario = new Usuario(1, "PROPIETARIO");

        Restaurante restaurante = new Restaurante(
                "Restaurante Juanca",
                "123456789",
                "Calle 10 # 20-30",
                "+573665698325",
                "https://ejemplo.com/logo.png",
                1
        );

        System.out.println("NIT válido: " + restaurante.nitValido());
        System.out.println("Teléfono válido: " + restaurante.telefonoValido());
        System.out.println("Nombre válido: " + restaurante.nombreValido());
        System.out.println("Campos obligatorios: " + restaurante.camposObligatoriosValidos());
        System.out.println("Propietario válido: " + restaurante.propietarioValido(usuario));
    }
}
