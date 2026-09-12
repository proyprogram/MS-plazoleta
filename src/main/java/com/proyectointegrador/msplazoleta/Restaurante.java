package com.proyectointegrador.msplazoleta;

public class Restaurante {

    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private int idPropietario;

    public Restaurante(String nombre, String nit, String direccion,
                       String telefono, String urlLogo, int idPropietario) {

        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.urlLogo = urlLogo;
        this.idPropietario = idPropietario;
    }

    public boolean nitValido() {
        return nit.matches("[0-9]+");
    }

    public boolean telefonoValido() {
        return telefono.length() <= 13 && telefono.matches("\\+?[0-9]+");
    }

    public boolean nombreValido() {
        return !nombre.matches("[0-9]+");
    }

    public boolean camposObligatoriosValidos() {
        return !nombre.isEmpty()
                && !nit.isEmpty()
                && !direccion.isEmpty()
                && !telefono.isEmpty()
                && !urlLogo.isEmpty()
                && idPropietario > 0;
    }

    public boolean propietarioValido(Usuario usuario) {
        return usuario != null
                && idPropietario == usuario.getId()
                && usuario.getRol().equals("PROPIETARIO");
    }
}


