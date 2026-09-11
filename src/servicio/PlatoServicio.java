package servicio;

import modelo.Plato;
import modelo.Restaurante;
import java.util.ArrayList;
import java.util.List;

public class PlatoServicio {
    private List<Plato> listaPlatos = new ArrayList<>();
    private long siguienteId = 1;
    private RestauranteServicio restauranteServicio;

    public PlatoServicio(RestauranteServicio restauranteServicio) {
        this.restauranteServicio = restauranteServicio;
    }

    // ==============================================
    //  CREAR PLATO
    // ==============================================
    public Plato crearPlato(String nombre, double precio, String descripcion, long idRestaurante) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() < 2)
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        if (descripcion == null || descripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción es obligatoria");
        
        Restaurante rest = restauranteServicio.buscarPorId(idRestaurante);
        if (rest == null)
            throw new IllegalArgumentException("No existe el restaurante con ID: " + idRestaurante);

        Plato nuevo = new Plato(siguienteId++, nombre, precio, descripcion, idRestaurante);
        listaPlatos.add(nuevo);
        return nuevo;
    }

    // ==============================================
    //  MODIFICAR PLATO — LO NUEVO
    // ==============================================
    public Plato modificarPlato(long idPlato, String nuevoNombre, double nuevoPrecio, String nuevaDescripcion) {
        // 1. Buscar el plato
        Plato plato = buscarPorId(idPlato);
        
        // 2. Validar que exista
        if (plato == null)
            throw new IllegalArgumentException("No existe plato con ID: " + idPlato);

        // 3. Validar los nuevos datos
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty() || nuevoNombre.length() < 2)
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        if (nuevoPrecio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        if (nuevaDescripcion == null || nuevaDescripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción es obligatoria");

        // 4. Actualizar los datos
        plato.setNombre(nuevoNombre);
        plato.setPrecio(nuevoPrecio);
        plato.setDescripcion(nuevaDescripcion);

        return plato;
    }

    // ==============================================
    //  MÉTODOS AUXILIARES
    // ==============================================
    public Plato buscarPorId(long id) {
        for (Plato p : listaPlatos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<Plato> listarPorRestaurante(long idRestaurante) {
        List<Plato> resultado = new ArrayList<>();
        for (Plato p : listaPlatos) {
            if (p.getIdRestaurante() == idRestaurante && p.isActivo())
                resultado.add(p);
        }
        return resultado;
    }
}