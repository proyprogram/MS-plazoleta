package servicio;

import modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioServicio {
    private List<Usuario> usuarios = new ArrayList<>();
    private long siguienteId = 1;

    public Usuario crearUsuario(String nombre, String correo, String contrasena, String rol) {
        if (nombre == null || nombre.length() < 3)
            throw new IllegalArgumentException("Nombre muy corto");
        if (correo == null || !correo.contains("@"))
            throw new IllegalArgumentException("Correo invalido");
        if (contrasena == null || contrasena.length() < 6)
            throw new IllegalArgumentException("Contrasena muy corta");
        if (!rol.equals("PROPIETARIO") && !rol.equals("EMPLEADO") && !rol.equals("CLIENTE"))
            throw new IllegalArgumentException("Rol invalido");
        
        for (Usuario u : usuarios) {
            if (u.getCorreo().equals(correo))
                throw new IllegalArgumentException("Correo ya registrado");
        }
        
        Usuario nuevo = new Usuario(siguienteId++, nombre, correo, contrasena, rol);
        usuarios.add(nuevo);
        return nuevo;
    }

    public Usuario buscarPorId(long id) {
        for (Usuario u : usuarios) if (u.getId() == id) return u;
        return null;
    }
}