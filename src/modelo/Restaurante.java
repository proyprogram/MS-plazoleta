package modelo;

public class Restaurante {
    private long id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private long idPropietario;

    public Restaurante(long id, String nombre, String nit, String direccion, String telefono, long idPropietario) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.idPropietario = idPropietario;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public long getIdPropietario() { return idPropietario; }
    public void setIdPropietario(long idPropietario) { this.idPropietario = idPropietario; }
}