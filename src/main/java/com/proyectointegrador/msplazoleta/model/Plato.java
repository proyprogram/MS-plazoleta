package com.proyectointegrador.msplazoleta.model;

public class Plato {
    private long id;
    private String nombre;
    private int precio;
    private String descripcion;
    private String urlImagen;
    private String categoria;
    private long idRestaurante;
    private boolean activo;

    public Plato() {
        this.activo = true;
    }

    public Plato(long id, String nombre, int precio, String descripcion, String urlImagen, String categoria, long idRestaurante) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.categoria = categoria;
        this.idRestaurante = idRestaurante;
        this.activo = true;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(long idRestaurante) { this.idRestaurante = idRestaurante; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
