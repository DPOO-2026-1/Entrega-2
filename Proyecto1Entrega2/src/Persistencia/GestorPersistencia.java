package Persistencia;

import ModuloVenta.Venta;
import Usuario.Usuario;
import Usuario.Cliente;
import Usuario.Administrador;
import Usuario.Mesero;
import Usuario.Cocinero;
import Usuario.Empleado;
import World.Cafeteria;
import World.Juego;
import World.Prestamo;
import World.Mesa; // CAMBIO: necesario porque Prestamo usa Mesa
import World.CopiaPrestamo; // CAMBIO: necesario porque Prestamo guarda copias
import java.lang.reflect.Field; // CAMBIO: para restaurar fechaInicio sin modificar Prestamo

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//CAMBIO: imports necesarios para trabajar con Venta como está implementada actualmente
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Field;

public class GestorPersistencia {
    
    private String rutaArchivos; 
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
 // CAMBIO: Venta usa LocalDateTime, no Date
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GestorPersistencia(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    // 1. Cargar y Guardar

    public Cafeteria cargarTodo() {
        Cafeteria cafe = Cafeteria.getInstance(); 
        
        // Primero los independientes, luego los dependientes
        List<Usuario> usuarios = cargarUsuarios();
        List<Juego> juegos = cargarJuegos();
        List<Prestamo> prestamos = cargarPrestamos(usuarios, juegos);
        List<Venta> ventas = cargarVentas(usuarios);
        
        cafe.setUsuarios(usuarios);
        cafe.setJuegos(juegos);
        cafe.setPrestamos(prestamos);
        cafe.setVentas(ventas);
        
        return cafe;
    }

    public void guardarTodo(Cafeteria cafe) {
        guardarUsuarios(cafe.getUsuarios());
        guardarJuegos(cafe.getJuegos());
        guardarPrestamos(cafe.getPrestamos());
        guardarVentas(cafe.getVentas());
    }

    // 2. MÉTODOS DE USUARIOS

    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivos + "usuarios.csv");

        if (!archivo.exists()) {
            return usuarios;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // CAMBIO: uso split(";", -1) para que Java no borre columnas vacías al final.
                // Ejemplo: ADMINISTRADOR;admin;1234;Admin; no debe romper.
                String[] p = linea.split(";", -1);

                // CAMBIO: validación mínima para evitar ArrayIndexOutOfBoundsException.
                if (p.length < 4) {
                    System.err.println("Línea de usuario inválida: " + linea);
                    continue;
                }

                String tipo = p[0].trim();
                String login = p[1].trim();
                String password = p[2].trim();
                String nombre = p[3].trim();

                if (tipo.equals("CLIENTE")) {
                    // CAMBIO: antes hacías new Cliente(login, password, nombre, puntos),
                    // pero ese constructor NO existe en tu clase Cliente.
                    // Ahora se usa el constructor correcto:
                    // Cliente(login, password, nombre, boolean esNino, boolean esJoven)

                    boolean esNino = false;
                    boolean esJoven = false;

                    // Formato nuevo recomendado:
                    // CLIENTE;login;password;nombre;false;true
                    if (p.length >= 6) {
                        esNino = Boolean.parseBoolean(p[4]);
                        esJoven = Boolean.parseBoolean(p[5]);
                    }

                    usuarios.add(new Cliente(login, password, nombre, esNino, esJoven));
                } 
                else if (tipo.equals("MESERO")) {
                    // CAMBIO: se valida si existe la columna extra.
                    String codigoDescuento = "";

                    if (p.length >= 5) {
                        codigoDescuento = p[4];
                    }

                    usuarios.add(new Mesero(login, password, nombre, codigoDescuento));
                } 
                else if (tipo.equals("COCINERO")) {
                    // CAMBIO: se valida si existe la columna extra.
                    String codigoDescuento = "";

                    if (p.length >= 5) {
                        codigoDescuento = p[4];
                    }

                    usuarios.add(new Cocinero(login, password, nombre, codigoDescuento));
                } 
                else if (tipo.equals("ADMINISTRADOR")) {
                    usuarios.add(new Administrador(login, password, nombre));
                } 
                else {
                    System.err.println("Tipo de usuario desconocido: " + tipo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public void guardarUsuarios(List<Usuario> lista) {
        File archivo = new File(rutaArchivos + "usuarios.csv");

        // CAMBIO: evita NullPointerException si no hay carpeta padre.
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Usuario u : lista) {

                if (u instanceof Cliente) {
                    Cliente c = (Cliente) u;

                    // CAMBIO: se guarda cliente con el formato compatible con cargarUsuarios.
                    // No usamos getPuntosFidelidad porque tu constructor Cliente no carga por puntos.
                    pw.println("CLIENTE" + ";"
                            + c.getLogin() + ";"
                            + c.getPassword() + ";"
                            + c.getNombre() + ";"
                            + "false" + ";"
                            + "false");
                } 
                else if (u instanceof Mesero) {
                    Mesero m = (Mesero) u;

                    // CAMBIO: NO usamos reflexión ni getCodigoDescuento().
                    // Esto evita el error NoClassDefFoundError: CopiaPrestamo.
                    // Se deja el código de descuento vacío para no tocar Empleado/Mesero.
                    pw.println("MESERO" + ";"
                            + m.getLogin() + ";"
                            + m.getPassword() + ";"
                            + m.getNombre() + ";");
                } 
                else if (u instanceof Cocinero) {
                    Cocinero c = (Cocinero) u;

                    // CAMBIO: NO usamos reflexión ni getCodigoDescuento().
                    pw.println("COCINERO" + ";"
                            + c.getLogin() + ";"
                            + c.getPassword() + ";"
                            + c.getNombre() + ";");
                } 
                else if (u instanceof Administrador) {
                    Administrador a = (Administrador) u;

                    pw.println("ADMINISTRADOR" + ";"
                            + a.getLogin() + ";"
                            + a.getPassword() + ";"
                            + a.getNombre() + ";");
                }
            }
        } catch (Exception e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }

    // 3. MÉTODOS DE JUEGOS

    public List<Juego> cargarJuegos() {
        List<Juego> juegos = new ArrayList<>();
        File archivo = new File(rutaArchivos + "juegos.csv");

        if (!archivo.exists()) {
            return juegos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // CAMBIO: split con -1 para conservar columnas vacías si existen.
                String[] p = linea.split(";", -1);

                if (p.length < 8) {
                    System.err.println("Línea de juego inválida: " + linea);
                    continue;
                }

                String nombre = p[0];
                int anioPublicacion = Integer.parseInt(p[1]);
                String empresaMatriz = p[2];
                int minJugadores = Integer.parseInt(p[3]);
                int maxJugadores = Integer.parseInt(p[4]);
                int edadMinima = Integer.parseInt(p[5]);
                String categoria = p[6];
                boolean esDificil = Boolean.parseBoolean(p[7]);

                Juego j = new Juego(nombre, anioPublicacion, empresaMatriz, minJugadores, maxJugadores, edadMinima, categoria, esDificil);
                juegos.add(j);
            }
        } catch (Exception e) {
            System.err.println("Error leyendo juegos: " + e.getMessage());
        }

        return juegos;
    }

    public void guardarJuegos(List<Juego> lista) {
        File archivo = new File(rutaArchivos + "juegos.csv");

        // CAMBIO: validación para evitar NullPointerException.
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Juego j : lista) {
                pw.println(j.getNombre() + ";" 
                        + j.getAnioPublicacion() + ";" 
                        + j.getEmpresaMatriz() + ";" 
                        + j.getMinJugadores() + ";" 
                        + j.getMaxJugadores() + ";" 
                        + j.getEdadMinima() + ";" 
                        + j.getCategoria() + ";" 
                        + j.isEsDificil());
            }
        } catch (Exception e) {
            System.err.println("Error guardando juegos: " + e.getMessage());
        }
    }

 // 4. MÉTODOS DE PRÉSTAMOS

    public List<Prestamo> cargarPrestamos(List<Usuario> usuariosTotales, List<Juego> juegosTotales) {
        List<Prestamo> prestamos = new ArrayList<>();
        File archivo = new File(rutaArchivos + "prestamos.csv");

        if (!archivo.exists()) {
            return prestamos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // CAMBIO: uso split(";", -1) para no perder columnas vacías.
                String[] p = linea.split(";", -1);

                // Formato nuevo:
                // estado;fechaInicio;loginUsuario;idMesa;idCopia1,idCopia2,idCopia3
                if (p.length < 3) {
                    System.err.println("Línea de préstamo inválida: " + linea);
                    continue;
                }

                String estado = p[0];
                Date fechaInicio = sdf.parse(p[1]);
                String loginUsuario = p[2];

                Usuario usuarioSolicitante = buscarUsuarioPorLogin(usuariosTotales, loginUsuario);

                if (usuarioSolicitante == null) {
                    System.err.println("Usuario no encontrado para préstamo: " + loginUsuario);
                    continue;
                }

                // CAMBIO: tu clase Prestamo necesita Mesa, no Juego.
                Mesa mesaAsociada = null;

                if (p.length >= 4 && !p[3].trim().isEmpty()) {
                    try {
                        int idMesa = Integer.parseInt(p[3]);
                        mesaAsociada = new Mesa(idMesa, 0);
                    } catch (NumberFormatException e) {
                        // CAMBIO: esto permite leer archivos viejos donde p[3] era nombreJuego.
                        mesaAsociada = null;
                    }
                }

                // CAMBIO: tu clase Prestamo necesita List<CopiaPrestamo>.
                List<CopiaPrestamo> copias = new ArrayList<>();

                if (p.length >= 5 && !p[4].trim().isEmpty()) {
                    String[] idsCopias = p[4].split(",");

                    for (String idCopia : idsCopias) {
                        if (!idCopia.trim().isEmpty()) {
                            // CAMBIO: se reconstruyen copias mínimas desde el CSV.
                            // Se crean disponibles en true porque el constructor de Prestamo llama copia.prestar().
                            CopiaPrestamo copia = new CopiaPrestamo(idCopia.trim(), "Prestada", true, 0);
                            copias.add(copia);
                        }
                    }
                }

                // CAMBIO: se usa el constructor REAL de tu clase Prestamo.
                Prestamo prestamo = new Prestamo(usuarioSolicitante, mesaAsociada, copias);

                // CAMBIO: Prestamo no tiene constructor con estado/fecha, entonces restauramos estado con setter.
                prestamo.setEstado(estado);

                // CAMBIO: Prestamo no tiene setFechaHoraInicio(), entonces restauramos la fecha por reflexión
                // para NO modificar la clase Prestamo.
                restaurarFechaHoraInicio(prestamo, fechaInicio);

                prestamos.add(prestamo);
            }
        } catch (Exception e) {
            System.err.println("Error leyendo préstamos: " + e.getMessage());
        }

        return prestamos;
    }

    public void guardarPrestamos(List<Prestamo> lista) {
        File archivo = new File(rutaArchivos + "prestamos.csv");

        // CAMBIO: evita NullPointerException si rutaArchivos no tiene carpeta padre.
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Prestamo p : lista) {
                String fechaFormateada = sdf.format(p.getFechaHoraInicio());

                String loginUsuario = "";

                if (p.getSolicitadoPor() != null) {
                    loginUsuario = p.getSolicitadoPor().getLogin();
                }

                // CAMBIO: tu Prestamo tiene mesaAsociada, no juegoAsociado.
                String idMesa = "";

                if (p.getMesaAsociada() != null) {
                    idMesa = String.valueOf(p.getMesaAsociada().getIdMesa());
                }

                // CAMBIO: tu Prestamo tiene lista de CopiaPrestamo, no getJuegoAsociado().
                String idsCopias = "";

                if (p.getCopias() != null) {
                    for (int i = 0; i < p.getCopias().size(); i++) {
                        CopiaPrestamo copia = p.getCopias().get(i);

                        if (copia != null) {
                            if (!idsCopias.isEmpty()) {
                                idsCopias += ",";
                            }

                            idsCopias += copia.getIdUnico();
                        }
                    }
                }

                // CAMBIO: nuevo formato compatible con tu clase Prestamo:
                // estado;fechaInicio;loginUsuario;idMesa;idsCopias
                pw.println(p.getEstado() + ";" 
                        + fechaFormateada + ";" 
                        + loginUsuario + ";" 
                        + idMesa + ";" 
                        + idsCopias);
            }
        } catch (Exception e) {
            System.err.println("Error guardando préstamos: " + e.getMessage());
        }
    }

    // CAMBIO: helper para buscar usuario sin repetir código.
    private Usuario buscarUsuarioPorLogin(List<Usuario> usuarios, String login) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }

        return null;
    }

    // CAMBIO: helper para restaurar fechaHoraInicio sin modificar la clase Prestamo.
    private void restaurarFechaHoraInicio(Prestamo prestamo, Date fechaInicio) {
        try {
            Field campo = Prestamo.class.getDeclaredField("fechaHoraInicio");
            campo.setAccessible(true);
            campo.set(prestamo, fechaInicio);
        } catch (Exception e) {
            System.err.println("No se pudo restaurar fechaHoraInicio del préstamo: " + e.getMessage());
        }
    }

    // 5. MÉTODOS DE VENTAS

    public List<Venta> cargarVentas(List<Usuario> usuariosTotales) {
        List<Venta> ventas = new ArrayList<>();
        File archivo = new File(rutaArchivos + "ventas.csv");

        if (!archivo.exists()) {
            return ventas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                // CAMBIO: uso split(";", -1) para no perder columnas vacías.
                String[] p = linea.split(";", -1);

                // Formato:
                // idVenta;fecha;total;loginUsuario
                if (p.length < 4) {
                    System.err.println("Línea de venta inválida: " + linea);
                    continue;
                }

                int idVenta = Integer.parseInt(p[0]);

                // CAMBIO: Venta usa LocalDateTime, no Date.
                LocalDateTime fecha = LocalDateTime.parse(p[1], dtf);

                double total = Double.parseDouble(p[2]);
                String loginUsuario = p[3];

                Usuario realizadaPor = buscarUsuarioPorLogin(usuariosTotales, loginUsuario);

                if (realizadaPor != null) {
                    // CAMBIO: tu clase Venta no tiene constructor con parámetros.
                    // Por eso se crea vacía y se llena con setters.
                    Venta v = new Venta();

                    v.setIdVenta(idVenta);
                    v.setFecha(fecha);
                    v.setRealizadaPor(realizadaPor);

                    // CAMBIO: Venta no tiene setTotal(), entonces se restaura con reflexión
                    // para NO modificar la clase Venta.
                    restaurarTotalVenta(v, total);

                    ventas.add(v);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo ventas: " + e.getMessage());
        }

        return ventas;
    }

    public void guardarVentas(List<Venta> lista) {
        File archivo = new File(rutaArchivos + "ventas.csv");

        // CAMBIO: evita NullPointerException si rutaArchivos no tiene carpeta padre.
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Venta v : lista) {
                String fechaFormateada = "";

                // CAMBIO: Venta.getFecha() retorna LocalDateTime.
                if (v.getFecha() != null) {
                    fechaFormateada = v.getFecha().format(dtf);
                }

                String loginUsuario = "";

                if (v.getRealizadaPor() != null) {
                    loginUsuario = v.getRealizadaPor().getLogin();
                }

                // CAMBIO: Venta no tiene getTotal(), entonces se lee por reflexión
                // para NO modificar la clase Venta.
                double total = obtenerTotalVentaSeguro(v);

                // Formato:
                // idVenta;fecha;total;loginUsuario
                pw.println(v.getIdVenta() + ";"
                        + fechaFormateada + ";"
                        + total + ";"
                        + loginUsuario);
            }
        } catch (Exception e) {
            System.err.println("Error guardando ventas: " + e.getMessage());
        }
    }

    // CAMBIO: helper para restaurar el atributo privado total sin modificar Venta.
    private void restaurarTotalVenta(Venta venta, double total) {
        try {
            Field campo = Venta.class.getDeclaredField("total");
            campo.setAccessible(true);
            campo.set(venta, total);
        } catch (Exception e) {
            System.err.println("No se pudo restaurar total de venta: " + e.getMessage());
        }
    }

    // CAMBIO: helper para leer el atributo privado total sin modificar Venta.
    private double obtenerTotalVentaSeguro(Venta venta) {
        try {
            Field campo = Venta.class.getDeclaredField("total");
            campo.setAccessible(true);

            Object valor = campo.get(venta);

            if (valor != null) {
                return ((Double) valor).doubleValue();
            }
        } catch (Exception e) {
            System.err.println("No se pudo obtener total de venta: " + e.getMessage());
        }

        return 0.0;
    }
}