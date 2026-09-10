package servicio;

import modelo.Restaurante;
import modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class RestauranteServicio {
    private List<Restaurante> restaurantes = new ArrayList<>();
    private long siguienteId = 1;
    private UsuarioServicio usuarioServicio;

    public RestauranteServicio(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    public Restaurante crearRestaurante(String nombre, String nit, String direccion, String telefono, long idPropietario) {
        if (nombre == null || nombre.length() < 3) throw new IllegalArgumentException("Nombre inválido");
        if (nit == null || nit.length() < 5) throw new IllegalArgumentException("NIT inválido");
        if (direccion == null || direccion.isEmpty()) throw new IllegalArgumentException("Dirección obligatoria");
        if (telefono == null || telefono.length() < 7) throw new IllegalArgumentException("Teléfono inválido");

        Usuario prop = usuarioServicio.buscarPorId(idPropietario);
        if (prop == null) throw new IllegalArgumentException("Propietario no existe");
        if (!prop.getRol().equals("PROPIETARIO")) throw new IllegalArgumentException("No es propietario");

        Restaurante r = new Restaurante(siguienteId++, nombre, nit, direccion, telefono, idPropietario);
        restaurantes.add(r);
        return r;
    }

    public Restaurante buscarPorId(long id) {
        for (Restaurante r : restaurantes) if (r.getId() == id) return r;
        return null;
    }
}