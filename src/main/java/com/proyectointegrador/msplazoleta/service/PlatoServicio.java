package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.model.Plato;
import com.proyectointegrador.msplazoleta.Restaurante;
import java.util.ArrayList;
import java.util.List;

public class PlatoServicio {
    private List<Plato> listaPlatos = new ArrayList<>();
    private long siguienteId = 1;
    private RestauranteServicio restauranteServicio;

    public PlatoServicio(RestauranteServicio restauranteServicio) {
        this.restauranteServicio = restauranteServicio;
    }

    public Plato crearPlato(String nombre, int precio, String descripcion, String urlImagen, String categoria, long idRestaurante, long idPropietario) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() < 2)
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        if (descripcion == null || descripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción es obligatoria");
        if (urlImagen == null || urlImagen.trim().isEmpty())
            throw new IllegalArgumentException("La URL de la imagen es obligatoria");
        if (categoria == null || categoria.trim().isEmpty())
            throw new IllegalArgumentException("La categoría es obligatoria");

        Restaurante restaurante = restauranteServicio.buscarPorId(idRestaurante);
        if (restaurante == null)
            throw new IllegalArgumentException("El restaurante no existe");
        if (restaurante.getIdPropietario() != idPropietario)
            throw new IllegalArgumentException("Solo el propietario del restaurante puede crear platos");

        Plato plato = new Plato();
        plato.setId(siguienteId++);
        plato.setNombre(nombre);
        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);
        plato.setUrlImagen(urlImagen);
        plato.setCategoria(categoria);
        plato.setIdRestaurante(idRestaurante);
        listaPlatos.add(plato);
        return plato;
    }

    public Plato modificarPlato(long idPlato, int nuevoPrecio, String nuevaDescripcion, long idPropietario) {
        Plato plato = buscarPorId(idPlato);
        if (plato == null) throw new IllegalArgumentException("El plato no existe");
        if (!plato.isActivo()) throw new IllegalArgumentException("No se puede modificar un plato inactivo");

        Restaurante restaurante = restauranteServicio.buscarPorId(plato.getIdRestaurante());
        if (restaurante == null) throw new IllegalArgumentException("El restaurante del plato no existe");
        if (restaurante.getIdPropietario() != idPropietario)
            throw new IllegalArgumentException("Solo el propietario del restaurante puede modificar platos");

        if (nuevoPrecio <= 0) throw new IllegalArgumentException("El precio debe ser mayor a cero");
        if (nuevaDescripcion == null || nuevaDescripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción es obligatoria");

        plato.setPrecio(nuevoPrecio);
        plato.setDescripcion(nuevaDescripcion);
        return plato;
    }

    public Plato buscarPorId(long id) {
        for (Plato p : listaPlatos) {
            if (p.getId() == id && p.isActivo()) return p;
        }
        return null;
    }

    public List<Plato> listarTodos() {
        List<Plato> activos = new ArrayList<>();
        for (Plato p : listaPlatos) {
            if (p.isActivo()) activos.add(p);
        }
        return activos;
    }
}
