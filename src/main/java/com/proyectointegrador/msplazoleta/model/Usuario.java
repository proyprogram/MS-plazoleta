package com.proyectointegrador.msplazoleta;

public class Usuario {

    private int id;
    private String rol;

    public Usuario(int id, String rol) {
        this.id = id;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }
}
