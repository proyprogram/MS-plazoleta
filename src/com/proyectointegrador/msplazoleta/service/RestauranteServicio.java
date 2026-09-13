package com.proyectointegrador.msplazoleta.service;

import com.proyectointegrador.msplazoleta.Restaurante;
import java.util.ArrayList;
import java.util.List;

public class RestauranteServicio {
    private List<Restaurante> lista = new ArrayList<>();
    private long siguienteId = 1;

    public Restaurante registrar(String nombre, String nit, String direccion, String telefono, long idPropietario) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() < 2)
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        if (nit == null || nit.trim().isEmpty())
            throw new IllegalArgumentException("El NIT es obligatorio");
        if (direccion == null || direccion.trim().isEmpty())
            throw new IllegalArgumentException("La dirección es obligatoria");
        if (telefono == null || telefono.trim().isEmpty())
            throw new IllegalArgumentException("El teléfono es obligatorio");
        if (idPropietario <= 0)
            throw new IllegalArgumentException("ID de propietario inválido");

        Restaurante r = new Restaurante();
        r.setId(siguienteId++);
        r.setNombre(nombre);
        r.setNit(nit);
        r.setDireccion(direccion);
        r.setTelefono(telefono);
        r.setIdPropietario(idPropietario);
        lista.add(r);
        return r;
    }

    public Restaurante buscarPorId(long id) {
        for (Restaurante r : lista) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public List<Restaurante> listarTodos() {
        return new ArrayList<>(lista);
    }
}
