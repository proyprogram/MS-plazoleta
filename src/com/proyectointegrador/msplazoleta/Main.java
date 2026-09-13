package com.proyectointegrador.msplazoleta;

import com.proyectointegrador.msplazoleta.model.Plato;
import com.proyectointegrador.msplazoleta.model.Restaurante;
import com.proyectointegrador.msplazoleta.service.PlatoServicio;
import com.proyectointegrador.msplazoleta.service.RestauranteServicio;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== MS PLAZOLETA =====");
        RestauranteServicio rs = new RestauranteServicio();
        PlatoServicio ps = new PlatoServicio(rs);

        try {
            Restaurante r = rs.registrar("El Sabor Casero", "123456789", "Calle 10 #20-30", "6041234567", 1L);
            System.out.println("✅ Restaurante: " + r.getNombre());
        } catch (Exception e) { System.out.println("❌ " + e.getMessage()); }

        try {
            Plato p = ps.crearPlato("Bandeja Paisa", 18000, "Arroz, frijoles, carne, chicharrón, huevo", "https://ejemplo.com/img.jpg", "Plato fuerte", 1L, 1L);
            System.out.println("✅ Plato creado: " + p.getNombre() + " - Activo: " + p.isActivo());
        } catch (Exception e) { System.out.println("❌ " + e.getMessage()); }

        try {
            Plato p = ps.modificarPlato(1L, 20000, "Arroz, frijoles, carne, chicharrón, huevo, aguacate", 1L);
            System.out.println("✅ Plato modificado: $" + p.getPrecio());
        } catch (Exception e) { System.out.println("❌ " + e.getMessage()); }
    }
}
