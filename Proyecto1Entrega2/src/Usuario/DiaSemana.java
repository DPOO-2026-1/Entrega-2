package Usuario;

// ===== CAMBIO HECHO =====
// Antes probablemente solo tenías un formato de días.
// Agregué ambas versiones para que compile tanto con el UML como con pruebas/código viejo:
// LUNES / Lunes, MARTES / Martes, etc.
// También agregué métodos de normalización para comparar días sin importar mayúsculas/minúsculas.
// ===== FIN CAMBIO =====
public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO,

    // ===== CAMBIO HECHO =====
    // Compatibilidad con el estilo del UML o pruebas que usen Lunes, Martes, etc.
    // ===== FIN CAMBIO =====
    Lunes,
    Martes,
    Miercoles,
    Jueves,
    Viernes,
    Sabado,
    Domingo;

    // ===== CAMBIO HECHO =====
    // Método auxiliar para comparar dos días sin depender de si están escritos en mayúscula o capitalizados.
    // ===== FIN CAMBIO =====
    public boolean mismoDia(DiaSemana otro) {
        if (otro == null) {
            return false;
        }
        return normalizar(this).equals(normalizar(otro));
    }

    // ===== CAMBIO HECHO =====
    // Método usado por Torneo y GestorTorneos para comparar días de manera segura.
    // ===== FIN CAMBIO =====
    public static String normalizar(DiaSemana dia) {
        if (dia == null) {
            return "";
        }
        return dia.name().toUpperCase();
    }
}