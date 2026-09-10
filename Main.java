import modelo.Plato;
import modelo.Restaurante;
import servicio.PlatoServicio;
import servicio.RestauranteServicio;
import servicio.UsuarioServicio;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== PRUEBA DE CREAR Y MODIFICAR PLATO — Assael =====");

        UsuarioServicio usuarioServicio = new UsuarioServicio();
        RestauranteServicio restauranteServicio = new RestauranteServicio(usuarioServicio);
        PlatoServicio platoServicio = new PlatoServicio(restauranteServicio);

        try {
            usuarioServicio.crearUsuario("Maria Lopez", "maria@correo.com", "123456", "PROPIETARIO");
            System.out.println("✅ Propietario creado");

            Restaurante rest1 = restauranteServicio.crearRestaurante(
                    "La Sazon Casera", "890.123.456-7",
                    "Calle 45 #12-34", "3001234567", 1
            );
            System.out.println("✅ Restaurante creado — ID: " + rest1.getId());

            System.out.println("\n--- CREANDO PLATO ---");
            Plato miPlato = platoServicio.crearPlato(
                    "Arroz con Pollo", 15000,
                    "Arroz blanco, pollo guisado, ensalada", rest1.getId()
            );
            System.out.println("✅ PLATO CREADO! ID: " + miPlato.getId() + " — " + miPlato.getNombre());

            System.out.println("\n--- MODIFICANDO PLATO ---");
            Plato modificado = platoServicio.modificarPlato(
                    miPlato.getId(),
                    "Arroz con Pollo Especial",
                    18000,
                    "Arroz, pollo, aguacate, ensalada"
            );
            System.out.println("✅ PLATO MODIFICADO! Nuevo nombre: " + modificado.getNombre());

            System.out.println("\n📋 Platos del restaurante:");
            for (Plato p : platoServicio.listarPorRestaurante(rest1.getId())) {
                System.out.println("   • " + p.getNombre() + " — $" + p.getPrecio());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("✅ Validacion: " + e.getMessage());
        }
        System.out.println("\n===== FIN =====");
    }
}