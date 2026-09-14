package modelo;

public class Plato {
    private long id;
    private String nombre;
    private double precio;
    private String descripcion;
    private long idRestaurante;
    private boolean activo;

    public Plato(long id, String nombre, double precio, String descripcion, long idRestaurante) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.idRestaurante = idRestaurante;
        this.activo = true;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }
    public long getIdRestaurante() { return idRestaurante; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setActivo(boolean activo) { this.activo = activo; }
}