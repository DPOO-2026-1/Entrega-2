package Persistencia;

import ModuloVenta.Venta;
import ModuloVenta.ItemVenta;
import ModuloVenta.ProductoVendible;
import ModuloVenta.ProductoComestible;
import ModuloVenta.Bebida;
import ModuloVenta.Pasteleria;
import ModuloVenta.CopiaVenta;
import Usuario.Usuario;
import Usuario.Cliente;
import Usuario.Administrador;
import Usuario.Mesero;
import Usuario.SolicitudTurno;
import Usuario.SugerenciaMenu;
import Usuario.Cocinero;
import Usuario.Empleado;
import World.Cafeteria;
import World.Juego;
import World.Prestamo;
import Torneo.BonoTorneoAmistoso;
import Torneo.EstadoTorneo;
import Torneo.GestorTorneo;
import Torneo.InscripcionTorneo;
import Torneo.Torneo;
import Torneo.TorneoAmistoso;
import Torneo.TorneoCompetitivo;
import Usuario.DiaSemana;
import Usuario.DiaTurno;

import java.util.Map;
import World.Mesa; // CAMBIO: necesario porque Prestamo usa Mesa
import World.CopiaPrestamo; // CAMBIO: necesario porque Prestamo guarda copias
import java.lang.reflect.Field; // CAMBIO: para restaurar fechaInicio sin modificar Prestamo

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//CAMBIO: imports necesarios para trabajar con Venta como está implementada actualmente
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        
        if (cafe == null) {
            throw new IllegalStateException(
                "La Cafeteria no ha sido inicializada. Llama a Cafeteria.getInstance(cap, nom, gu, gv) antes de cargarTodo().");
        }
        
        // Primero los independientes, luego los dependientes
        List<Usuario> usuarios = cargarUsuarios();
        List<Juego> juegos = cargarJuegos();
        // Cambios proyecto 3 PERSISTENCIA COMPLETA
        // Se cargan turnos, capacitaciones, menú, sugerencias,
        // solicitudes y ventas con items.
        cargarTurnosEmpleados(usuarios);
        cargarCapacitacionesMeseros(usuarios, juegos);
        List<ProductoComestible> menu = cargarMenuCafeteria();
        List<SugerenciaMenu> sugerencias = cargarSugerenciasMenu(usuarios);
        List<SolicitudTurno> solicitudes = cargarSolicitudesTurno(usuarios);
        List<Prestamo> prestamos = cargarPrestamos(usuarios, juegos);
        List<Venta> ventas = cargarVentas(usuarios);
        
        cafe.setUsuarios(usuarios);
        cafe.setJuegos(juegos);
        cafe.setMenuCafeteria(menu);
        cafe.setSugerencias(sugerencias);
        cafe.setSolicitudesTurno(solicitudes);
        cafe.setPrestamos(prestamos);
        cafe.setVentas(ventas);
        
        GestorTorneo gestorTorneo = cargarGestorTorneo(usuarios, juegos);
        cafe.setGestorTorneo(gestorTorneo);
        
        return cafe;
    }

    public void guardarTodo(Cafeteria cafe) {
        guardarUsuarios(cafe.getUsuarios());
        guardarJuegos(cafe.getJuegos());
        guardarPrestamos(cafe.getPrestamos());
        guardarVentas(cafe.getVentas());
        guardarGestorTorneo(cafe.getGestorTorneo());
        
        // Cambios proyecto 3
        guardarMenuCafeteria(cafe.getMenuCafeteria());
        guardarSugerenciasMenu(cafe.getSugerencias());
        guardarSolicitudesTurno(cafe.getSolicitudesTurno());
        guardarTurnosEmpleados(cafe.getUsuarios());
        guardarCapacitacionesMeseros(cafe.getUsuarios());
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
                    boolean esNino = p.length >= 5 && Boolean.parseBoolean(p[4]);
                    boolean esJoven = p.length >= 6 && Boolean.parseBoolean(p[5]);
                    Cliente c = new Cliente(login, password, nombre, esNino, esJoven);
                    if (p.length >= 7 && !p[6].trim().isEmpty()) {
                        try {
                            c.setPuntosFidelidad(Integer.parseInt(p[6].trim()));
                        } catch (NumberFormatException e) {
                            // si el campo está corrupto, se deja en 0
                        }
                    }
                    usuarios.add(c);
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
            	    pw.println("CLIENTE" + ";"
            	            + c.getLogin() + ";"
            	            + c.getPassword() + ";"
            	            + c.getNombre() + ";"
            	            + c.isEsNinio() + ";"
            	            + c.isEsJoven() + ";"
            	            + c.getPuntosFidelidad()); // columna nueva
            	}
                else if (u instanceof Mesero) {
                    Mesero m = (Mesero) u;
                    pw.println("MESERO" + ";"
                            + m.getLogin() + ";"
                            + m.getPassword() + ";"
                            + m.getNombre() + ";"
                            + m.getCodigoDescuento());
                } 
                else if (u instanceof Cocinero) {
                    Cocinero c = (Cocinero) u;
                    pw.println("COCINERO" + ";"
                            + c.getLogin() + ";"
                            + c.getPassword() + ";"
                            + c.getNombre() + ";"
                            + c.getCodigoDescuento());
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

                String[] p = linea.split(";", -1);

                // =====================================================
                // CAMBIO NUEVO PROYECTO 3 - PERSISTENCIA DETALLADA DE VENTAS
                // Formato nuevo:
                // idVenta;fecha;subtotal;impuestos;propina;total;descuento;puntos;loginUsuario;items
                // Formato viejo compatible:
                // idVenta;fecha;total;loginUsuario
                // =====================================================
                try {
                    if (p.length >= 10) {
                        Venta v = new Venta();

                        int idVenta = Integer.parseInt(p[0]);
                        LocalDateTime fecha = LocalDateTime.parse(p[1], dtf);
                        double subtotal = Double.parseDouble(p[2]);
                        double impuestos = Double.parseDouble(p[3]);
                        double propina = Double.parseDouble(p[4]);
                        double total = Double.parseDouble(p[5]);
                        double descuento = Double.parseDouble(p[6]);
                        int puntos = Integer.parseInt(p[7]);
                        Usuario realizadaPor = buscarUsuarioPorLogin(usuariosTotales, p[8]);
                        ItemVenta[] items = deserializarItemsVenta(p[9]);

                        v.setIdVenta(idVenta);
                        v.setFecha(fecha);
                        v.setSubtotal(subtotal);
                        v.setImpuestos(impuestos);
                        v.setPropinaValor(propina);
                        v.setTotal(total);
                        v.setDescuentoAplicado(descuento);
                        v.setPuntosGenerados(puntos);
                        v.setRealizadaPor(realizadaPor);
                        v.setItemsVenta(items);

                        ventas.add(v);
                    } else if (p.length >= 4) {
                        Venta v = new Venta();

                        v.setIdVenta(Integer.parseInt(p[0]));
                        v.setFecha(LocalDateTime.parse(p[1], dtf));
                        v.setTotal(Double.parseDouble(p[2]));
                        v.setRealizadaPor(buscarUsuarioPorLogin(usuariosTotales, p[3]));

                        ventas.add(v);
                    }
                } catch (Exception e) {
                    System.err.println("Línea de venta inválida: " + linea + " -> " + e.getMessage());
                }
                // =====================================================
                // FIN CAMBIO NUEVO PROYECTO 3
                // =====================================================
            }
        } catch (Exception e) {
            System.err.println("Error leyendo ventas: " + e.getMessage());
        }

        return ventas;
    }

    public void guardarVentas(List<Venta> lista) {
        File archivo = new File(rutaArchivos + "ventas.csv");

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            int siguienteId = calcularSiguienteIdVenta(lista);

            for (Venta v : lista) {
                if (v == null) {
                    continue;
                }

                // =====================================================
                // CAMBIO NUEVO PROYECTO 3 - PERSISTENCIA DETALLADA DE VENTAS
                // Si la venta no tenía id, se le asigna uno.
                // =====================================================
                if (v.getIdVenta() <= 0) {
                    v.setIdVenta(siguienteId++);
                }
                // =====================================================
                // FIN CAMBIO NUEVO PROYECTO 3
                // =====================================================

                String fechaFormateada = "";

                if (v.getFecha() != null) {
                    fechaFormateada = v.getFecha().format(dtf);
                }

                String loginUsuario = "";

                if (v.getRealizadaPor() != null) {
                    loginUsuario = v.getRealizadaPor().getLogin();
                }

                // =====================================================
                // CAMBIO NUEVO PROYECTO 3 - Ahora se guardan subtotal,
                // impuestos, propina, descuento, puntos e items.
                // =====================================================
                pw.println(v.getIdVenta() + ";"
                        + fechaFormateada + ";"
                        + v.getSubtotal() + ";"
                        + v.getImpuestos() + ";"
                        + v.getPropina() + ";"
                        + v.getTotal() + ";"
                        + v.getDescuentoAplicado() + ";"
                        + v.getPuntosGenerados() + ";"
                        + limpiarCampo(loginUsuario) + ";"
                        + serializarItemsVenta(v.getItemsVenta()));
                // =====================================================
                // FIN CAMBIO NUEVO PROYECTO 3
                // =====================================================
            }
        } catch (Exception e) {
            System.err.println("Error guardando ventas: " + e.getMessage());
        }
    }
    
    // =====================================================
    		// CAMBIO NUEVO PROYECTO 3 - Helpers de ventas detalladas.
    		// =====================================================
    		private int calcularSiguienteIdVenta(List<Venta> lista) {
    		    int max = 0;

    		    if (lista != null) {
    		        for (Venta v : lista) {
    		            if (v != null && v.getIdVenta() > max) {
    		                max = v.getIdVenta();
    		            }
    		        }
    		    }

    		    return max + 1;
    		}

    		private String serializarItemsVenta(ItemVenta[] items) {
    		    if (items == null || items.length == 0) {
    		        return "";
    		    }

    		    StringBuilder sb = new StringBuilder();

    		    for (ItemVenta item : items) {
    		        if (item == null || item.getProducto() == null) {
    		            continue;
    		        }

    		        if (sb.length() > 0) {
    		            sb.append("|");
    		        }

    		        ProductoVendible producto = item.getProducto();
    		        String tipo = "OTRO";
    		        String nombre = "Producto";

    		        if (producto instanceof Bebida) {
    		            tipo = "BEBIDA";
    		            nombre = ((Bebida) producto).getNombre();
    		        } else if (producto instanceof Pasteleria) {
    		            tipo = "PASTELERIA";
    		            nombre = ((Pasteleria) producto).getNombre();
    		        } else if (producto instanceof CopiaVenta) {
    		            tipo = "COPIA_VENTA";
    		            nombre = ((CopiaVenta) producto).getIdUnico();
    		        } else if (producto instanceof ProductoComestible) {
    		            tipo = "COMESTIBLE";
    		            nombre = ((ProductoComestible) producto).getNombre();
    		        }

    		        sb.append(limpiarItem(tipo)).append(",")
    		                .append(limpiarItem(nombre)).append(",")
    		                .append(item.getCantidad()).append(",")
    		                .append(item.getPrecioUnitario());
    		    }

    		    return sb.toString();
    		}

    		private ItemVenta[] deserializarItemsVenta(String texto) {
    		    if (texto == null || texto.trim().isEmpty()) {
    		        return new ItemVenta[0];
    		    }

    		    List<ItemVenta> items = new ArrayList<>();
    		    String[] partes = texto.split("\\|");

    		    for (String parte : partes) {
    		        try {
    		            String[] p = parte.split(",", -1);

    		            if (p.length < 4) {
    		                continue;
    		            }

    		            String tipo = p[0];
    		            String nombre = p[1];
    		            int cantidad = Integer.parseInt(p[2]);
    		            double precio = Double.parseDouble(p[3]);

    		            ProductoVendible producto;

    		            if ("BEBIDA".equals(tipo)) {
    		                producto = new Bebida(nombre, precio, false, false);
    		            } else if ("PASTELERIA".equals(tipo)) {
    		                producto = new Pasteleria(nombre, precio, new ArrayList<String>());
    		            } else if ("COPIA_VENTA".equals(tipo)) {
    		                producto = new CopiaVenta(nombre, precio);
    		            } else {
    		                producto = new Bebida(nombre, precio, false, false);
    		            }

    		            items.add(new ItemVenta(producto, cantidad, precio));
    		        } catch (Exception e) {
    		            System.err.println("No se pudo leer item de venta: " + parte);
    		        }
    		    }

    		    return items.toArray(new ItemVenta[0]);
    		}

    		private String limpiarItem(String texto) {
    		    if (texto == null) {
    		        return "";
    		    }

    		    return texto.replace(";", " ").replace("|", " ").replace(",", " ");
    		}
    		// =====================================================
    		// FIN CAMBIO NUEVO PROYECTO 3
    		// =====================================================
    		
    		// =====================================================
    		// CAMBIO NUEVO PROYECTO 3 - PERSISTENCIA DE MENÚ, SUGERENCIAS,
    		// SOLICITUDES DE TURNO, TURNOS Y CAPACITACIONES.
    		// =====================================================
    		public List<ProductoComestible> cargarMenuCafeteria() {
    		    List<ProductoComestible> menu = new ArrayList<>();
    		    File archivo = new File(rutaArchivos + "menu_cafeteria.csv");

    		    if (!archivo.exists()) {
    		        return menu;
    		    }

    		    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    		        String linea;

    		        while ((linea = br.readLine()) != null) {
    		            if (linea.trim().isEmpty()) {
    		                continue;
    		            }

    		            String[] p = linea.split(";", -1);

    		            if (p.length < 3) {
    		                continue;
    		            }

    		            String tipo = p[0];
    		            String nombre = p[1];
    		            double precio = Double.parseDouble(p[2]);

    		            if ("BEBIDA".equalsIgnoreCase(tipo)) {
    		                boolean caliente = p.length >= 4 && Boolean.parseBoolean(p[3]);
    		                boolean alcoholica = p.length >= 5 && Boolean.parseBoolean(p[4]);
    		                menu.add(new Bebida(nombre, precio, caliente, alcoholica));
    		            } else if ("PASTELERIA".equalsIgnoreCase(tipo)) {
    		                ArrayList<String> alergenos = new ArrayList<>();

    		                if (p.length >= 4 && !p[3].trim().isEmpty()) {
    		                    alergenos.addAll(Arrays.asList(p[3].split(",")));
    		                }

    		                menu.add(new Pasteleria(nombre, precio, alergenos));
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error leyendo menú de cafetería: " + e.getMessage());
    		    }

    		    return menu;
    		}

    		public void guardarMenuCafeteria(List<ProductoComestible> menu) {
    		    File archivo = new File(rutaArchivos + "menu_cafeteria.csv");

    		    if (archivo.getParentFile() != null) {
    		        archivo.getParentFile().mkdirs();
    		    }

    		    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
    		        if (menu == null) {
    		            return;
    		        }

    		        for (ProductoComestible producto : menu) {
    		            if (producto == null) {
    		                continue;
    		            }

    		            if (producto instanceof Bebida) {
    		                Bebida b = (Bebida) producto;
    		                pw.println("BEBIDA;" + limpiarCampo(b.getNombre()) + ";" + b.getPrecioBase() + ";"
    		                        + b.isEsCaliente() + ";" + b.isEsAlcoholica());
    		            } else if (producto instanceof Pasteleria) {
    		                Pasteleria p = (Pasteleria) producto;
    		                String alergenos = "";

    		                if (p.getAlergenos() != null) {
    		                    for (int i = 0; i < p.getAlergenos().size(); i++) {
    		                        if (i > 0) {
    		                            alergenos += ",";
    		                        }

    		                        alergenos += limpiarCampo(p.getAlergenos().get(i));
    		                    }
    		                }

    		                pw.println("PASTELERIA;" + limpiarCampo(p.getNombre()) + ";" + p.getPrecioBase() + ";" + alergenos);
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error guardando menú de cafetería: " + e.getMessage());
    		    }
    		}

    		public List<SugerenciaMenu> cargarSugerenciasMenu(List<Usuario> usuarios) {
    		    List<SugerenciaMenu> sugerencias = new ArrayList<>();
    		    File archivo = new File(rutaArchivos + "sugerencias_menu.csv");

    		    if (!archivo.exists()) {
    		        return sugerencias;
    		    }

    		    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    		        String linea;

    		        while ((linea = br.readLine()) != null) {
    		            if (linea.trim().isEmpty()) {
    		                continue;
    		            }

    		            String[] p = linea.split(";", -1);

    		            if (p.length < 3) {
    		                continue;
    		            }

    		            Usuario usuario = buscarUsuarioPorLogin(usuarios, p[2]);

    		            if (usuario instanceof Empleado) {
    		                sugerencias.add(new SugerenciaMenu(p[0], p[1], (Empleado) usuario));
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error leyendo sugerencias de menú: " + e.getMessage());
    		    }

    		    return sugerencias;
    		}

    		public void guardarSugerenciasMenu(List<SugerenciaMenu> sugerencias) {
    		    File archivo = new File(rutaArchivos + "sugerencias_menu.csv");

    		    if (archivo.getParentFile() != null) {
    		        archivo.getParentFile().mkdirs();
    		    }

    		    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
    		        if (sugerencias == null) {
    		            return;
    		        }

    		        for (SugerenciaMenu sugerencia : sugerencias) {
    		            if (sugerencia == null) {
    		                continue;
    		            }

    		            String login = "";

    		            if (sugerencia.getCreadoPor() != null) {
    		                login = sugerencia.getCreadoPor().getLogin();
    		            }

    		            pw.println(limpiarCampo(sugerencia.getDescripcion()) + ";"
    		                    + limpiarCampo(sugerencia.getEstado()) + ";"
    		                    + limpiarCampo(login));
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error guardando sugerencias de menú: " + e.getMessage());
    		    }
    		}

    		public List<SolicitudTurno> cargarSolicitudesTurno(List<Usuario> usuarios) {
    		    List<SolicitudTurno> solicitudes = new ArrayList<>();
    		    File archivo = new File(rutaArchivos + "solicitudes_turno.csv");

    		    if (!archivo.exists()) {
    		        return solicitudes;
    		    }

    		    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    		        String linea;

    		        while ((linea = br.readLine()) != null) {
    		            if (linea.trim().isEmpty()) {
    		                continue;
    		            }

    		            String[] p = linea.split(";", -1);

    		            if (p.length < 4) {
    		                continue;
    		            }

    		            Usuario usuario = buscarUsuarioPorLogin(usuarios, p[2]);

    		            if (usuario instanceof Empleado) {
    		                DiaSemana dia = DiaSemana.valueOf(p[0]);
    		                String estado = p[1];
    		                boolean intercambio = Boolean.parseBoolean(p[3]);
    		                solicitudes.add(new SolicitudTurno(dia, estado, (Empleado) usuario, intercambio));
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error leyendo solicitudes de turno: " + e.getMessage());
    		    }

    		    return solicitudes;
    		}

    		public void guardarSolicitudesTurno(List<SolicitudTurno> solicitudes) {
    		    File archivo = new File(rutaArchivos + "solicitudes_turno.csv");

    		    if (archivo.getParentFile() != null) {
    		        archivo.getParentFile().mkdirs();
    		    }

    		    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
    		        if (solicitudes == null) {
    		            return;
    		        }

    		        for (SolicitudTurno solicitud : solicitudes) {
    		            if (solicitud == null) {
    		                continue;
    		            }

    		            String login = "";

    		            if (solicitud.getSolicitadoPor() != null) {
    		                login = solicitud.getSolicitadoPor().getLogin();
    		            }

    		            pw.println(solicitud.getDia().name() + ";"
    		                    + limpiarCampo(solicitud.getEstado()) + ";"
    		                    + limpiarCampo(login) + ";"
    		                    + solicitud.isEsIntercambio());
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error guardando solicitudes de turno: " + e.getMessage());
    		    }
    		}

    		public void cargarTurnosEmpleados(List<Usuario> usuarios) {
    		    File archivo = new File(rutaArchivos + "turnos_empleados.csv");

    		    if (!archivo.exists()) {
    		        return;
    		    }

    		    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    		        String linea;

    		        while ((linea = br.readLine()) != null) {
    		            if (linea.trim().isEmpty()) {
    		                continue;
    		            }

    		            String[] p = linea.split(";", -1);

    		            if (p.length < 3) {
    		                continue;
    		            }

    		            Usuario usuario = buscarUsuarioPorLogin(usuarios, p[0]);

    		            if (usuario instanceof Empleado) {
    		                Empleado empleado = (Empleado) usuario;
    		                DiaSemana dia = DiaSemana.valueOf(p[1]);
    		                boolean aprobado = Boolean.parseBoolean(p[2]);
    		                empleado.consultarDiasAsignados().add(new DiaTurno(dia, aprobado));
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error leyendo turnos de empleados: " + e.getMessage());
    		    }
    		}

    		public void guardarTurnosEmpleados(List<Usuario> usuarios) {
    		    File archivo = new File(rutaArchivos + "turnos_empleados.csv");

    		    if (archivo.getParentFile() != null) {
    		        archivo.getParentFile().mkdirs();
    		    }

    		    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
    		        if (usuarios == null) {
    		            return;
    		        }

    		        for (Usuario usuario : usuarios) {
    		            if (!(usuario instanceof Empleado)) {
    		                continue;
    		            }

    		            Empleado empleado = (Empleado) usuario;

    		            if (empleado.consultarDiasAsignados() == null) {
    		                continue;
    		            }

    		            for (DiaTurno turno : empleado.consultarDiasAsignados()) {
    		                if (turno != null && turno.getDia() != null) {
    		                    pw.println(empleado.getLogin() + ";" + turno.getDia().name() + ";" + turno.isAprobado());
    		                }
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error guardando turnos de empleados: " + e.getMessage());
    		    }
    		}

    		public void cargarCapacitacionesMeseros(List<Usuario> usuarios, List<Juego> juegos) {
    		    File archivo = new File(rutaArchivos + "capacitaciones_meseros.csv");

    		    if (!archivo.exists()) {
    		        return;
    		    }

    		    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
    		        String linea;

    		        while ((linea = br.readLine()) != null) {
    		            if (linea.trim().isEmpty()) {
    		                continue;
    		            }

    		            String[] p = linea.split(";", -1);

    		            if (p.length < 2) {
    		                continue;
    		            }

    		            Usuario usuario = buscarUsuarioPorLogin(usuarios, p[0]);
    		            Juego juego = buscarJuegoPorNombre(juegos, p[1]);

    		            if (usuario instanceof Mesero && juego != null) {
    		                ((Mesero) usuario).agregarJuegoConocido(juego);
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error leyendo capacitaciones de meseros: " + e.getMessage());
    		    }
    		}

    		public void guardarCapacitacionesMeseros(List<Usuario> usuarios) {
    		    File archivo = new File(rutaArchivos + "capacitaciones_meseros.csv");

    		    if (archivo.getParentFile() != null) {
    		        archivo.getParentFile().mkdirs();
    		    }

    		    try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
    		        if (usuarios == null) {
    		            return;
    		        }

    		        for (Usuario usuario : usuarios) {
    		            if (!(usuario instanceof Mesero)) {
    		                continue;
    		            }

    		            Mesero mesero = (Mesero) usuario;

    		            if (mesero.getJuegosConocidos() == null) {
    		                continue;
    		            }

    		            for (Juego juego : mesero.getJuegosConocidos()) {
    		                if (juego != null) {
    		                    pw.println(limpiarCampo(mesero.getLogin()) + ";" + limpiarCampo(juego.getNombre()));
    		                }
    		            }
    		        }
    		    } catch (Exception e) {
    		        System.err.println("Error guardando capacitaciones de meseros: " + e.getMessage());
    		    }
    		}
    		// =====================================================
    		// FIN CAMBIO NUEVO PROYECTO 3
    		// =====================================================
    
    // PERSISTENCIA TORNEOS
    public GestorTorneo cargarGestorTorneo(List<Usuario> usuariosTotales, List<Juego> juegosTotales) {
        GestorTorneo gestor = new GestorTorneo();

        List<Torneo> torneos = cargarTorneos(juegosTotales);
        gestor.getCatalogoTorneos().addAll(torneos);

        cargarInscripcionesTorneos(gestor, usuariosTotales);
        cargarPagosTorneos(gestor);
        cargarBonosTorneos(gestor, usuariosTotales);

        return gestor;
    }

    public void guardarGestorTorneo(GestorTorneo gestorTorneo) {
        if (gestorTorneo == null) {
            return;
        }

        guardarTorneos(gestorTorneo.getCatalogoTorneos());
        guardarInscripcionesTorneos(gestorTorneo.getCatalogoTorneos());
        guardarPagosTorneos(gestorTorneo.getCatalogoTorneos());
        guardarBonosTorneos(gestorTorneo.getBonos());
    }

    private List<Torneo> cargarTorneos(List<Juego> juegosTotales) {
        List<Torneo> torneos = new ArrayList<Torneo>();
        File archivo = new File(rutaArchivos + "torneos.csv");

        if (!archivo.exists()) {
            return torneos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] p = linea.split(";", -1);

                // Formato:
                // tipo;id;dia;fechaInicio;duracionMin;estado;cupoTotal;cupoReservadoFanaticos;
                // cupoOcupadoReservado;cupoOcupadoRegular;fechaCreacion;nombre;nombreJuego;
                // valorBono;tarifaEntrada;porcentajePozoPremio

                if (p.length < 16) {
                    System.err.println("Línea de torneo inválida: " + linea);
                    continue;
                }

                String tipo = p[0];
                String idTorneo = p[1];
                DiaSemana dia = DiaSemana.valueOf(p[2]);
                Date fechaInicio = parsearFechaSegura(p[3]);
                int duracionMin = Integer.parseInt(p[4]);
                EstadoTorneo estado = EstadoTorneo.valueOf(p[5]);
                int cupoTotal = Integer.parseInt(p[6]);
                int cupoReservadoFanaticos = Integer.parseInt(p[7]);
                int cupoOcupadoReservado = Integer.parseInt(p[8]);
                int cupoOcupadoRegular = Integer.parseInt(p[9]);
                Date fechaCreacion = parsearFechaSegura(p[10]);
                String nombre = p[11];
                String nombreJuego = p[12];

                Juego juego = buscarJuegoPorNombre(juegosTotales, nombreJuego);

                if (juego == null) {
                    System.err.println("Juego no encontrado para torneo: " + nombreJuego);
                    continue;
                }

                if ("AMISTOSO".equals(tipo)) {
                    double valorBono = Double.parseDouble(p[13]);

                    TorneoAmistoso torneo = new TorneoAmistoso(
                            idTorneo,
                            dia,
                            fechaInicio,
                            duracionMin,
                            estado,
                            cupoTotal,
                            cupoReservadoFanaticos,
                            cupoOcupadoReservado,
                            cupoOcupadoRegular,
                            fechaCreacion,
                            nombre,
                            juego,
                            new ArrayList<InscripcionTorneo>(),
                            valorBono
                    );

                    torneos.add(torneo);
                } 
                else if ("COMPETITIVO".equals(tipo)) {
                    double tarifaEntrada = Double.parseDouble(p[14]);
                    double porcentajePozoPremio = Double.parseDouble(p[15]);

                    TorneoCompetitivo torneo = new TorneoCompetitivo(
                            idTorneo,
                            dia,
                            fechaInicio,
                            duracionMin,
                            estado,
                            cupoTotal,
                            cupoReservadoFanaticos,
                            cupoOcupadoReservado,
                            cupoOcupadoRegular,
                            fechaCreacion,
                            nombre,
                            juego,
                            new ArrayList<InscripcionTorneo>(),
                            tarifaEntrada
                    );

                    torneo.setPorcentajePozoPremio(porcentajePozoPremio);

                    torneos.add(torneo);
                } 
                else {
                    System.err.println("Tipo de torneo desconocido: " + tipo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo torneos: " + e.getMessage());
        }

        return torneos;
    }

    private void guardarTorneos(List<Torneo> torneos) {
        File archivo = new File(rutaArchivos + "torneos.csv");

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Torneo t : torneos) {
                String tipo = "";
                double valorBono = 0.0;
                double tarifaEntrada = 0.0;
                double porcentajePozoPremio = 0.0;

                if (t instanceof TorneoAmistoso) {
                    tipo = "AMISTOSO";
                    valorBono = ((TorneoAmistoso) t).getValorBono();
                } 
                else if (t instanceof TorneoCompetitivo) {
                    tipo = "COMPETITIVO";
                    TorneoCompetitivo tc = (TorneoCompetitivo) t;
                    tarifaEntrada = tc.getTarifaEntrada();
                    porcentajePozoPremio = tc.getPorcentajePozoPremio();
                } 
                else {
                    continue;
                }

                String nombreJuego = "";

                if (t.getJuegoTorneo() != null) {
                    nombreJuego = t.getJuegoTorneo().getNombre();
                }

                pw.println(tipo + ";"
                        + limpiarCampo(t.getIdTorneo()) + ";"
                        + t.getDia().name() + ";"
                        + formatearFechaSegura(t.getFechaInicio()) + ";"
                        + t.getDuracionMin() + ";"
                        + t.getEstado().name() + ";"
                        + t.getCupoTotal() + ";"
                        + t.getCupoReservadoFanaticos() + ";"
                        + t.getCupoOcupadoReservado() + ";"
                        + t.getCupoOcupadoRegular() + ";"
                        + formatearFechaSegura(t.getFechaCreacion()) + ";"
                        + limpiarCampo(t.getNombre()) + ";"
                        + limpiarCampo(nombreJuego) + ";"
                        + valorBono + ";"
                        + tarifaEntrada + ";"
                        + porcentajePozoPremio);
            }
        } catch (Exception e) {
            System.err.println("Error guardando torneos: " + e.getMessage());
        }
    }

    private void cargarInscripcionesTorneos(GestorTorneo gestor, List<Usuario> usuariosTotales) {
        File archivo = new File(rutaArchivos + "inscripciones_torneos.csv");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] p = linea.split(";", -1);

                // Formato:
                // idTorneo;idInscripcion;fecha;loginsUsuarios;cantidadCupos;cuposReservados;
                // cuposRegulares;esEmpleado;montoPagado;pagoConfirmado;elegiblePrecioMetalico

                if (p.length < 11) {
                    System.err.println("Línea de inscripción inválida: " + linea);
                    continue;
                }

                String idTorneo = p[0];
                Torneo torneo = gestor.buscarTorneo(idTorneo);

                if (torneo == null) {
                    System.err.println("Torneo no encontrado para inscripción: " + idTorneo);
                    continue;
                }

                String idInscripcion = p[1];
                Date fecha = parsearFechaSegura(p[2]);
                String logins = p[3];

                List<Usuario> usuariosInscritos = new ArrayList<Usuario>();

                if (!logins.trim().isEmpty()) {
                    String[] partesLogin = logins.split(",");

                    for (String login : partesLogin) {
                        Usuario u = buscarUsuarioPorLogin(usuariosTotales, login.trim());

                        if (u != null) {
                            usuariosInscritos.add(u);
                        }
                    }
                }

                int cantidadCupos = Integer.parseInt(p[4]);
                int cuposReservados = Integer.parseInt(p[5]);
                int cuposRegulares = Integer.parseInt(p[6]);
                boolean esEmpleado = Boolean.parseBoolean(p[7]);
                double montoPagado = Double.parseDouble(p[8]);
                boolean pagoConfirmado = Boolean.parseBoolean(p[9]);
                boolean elegiblePrecioMetalico = Boolean.parseBoolean(p[10]);

                InscripcionTorneo inscripcion = new InscripcionTorneo(
                        idInscripcion,
                        fecha,
                        usuariosInscritos,
                        cantidadCupos,
                        cuposReservados,
                        cuposRegulares,
                        esEmpleado,
                        montoPagado,
                        pagoConfirmado,
                        elegiblePrecioMetalico
                );

                torneo.getInscripciones().add(inscripcion);
            }
        } catch (Exception e) {
            System.err.println("Error leyendo inscripciones de torneos: " + e.getMessage());
        }
    }

    private void guardarInscripcionesTorneos(List<Torneo> torneos) {
        File archivo = new File(rutaArchivos + "inscripciones_torneos.csv");

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Torneo torneo : torneos) {
                for (InscripcionTorneo inscripcion : torneo.getInscripciones()) {
                    String logins = "";

                    for (int i = 0; i < inscripcion.getUsuarios().size(); i++) {
                        Usuario u = inscripcion.getUsuarios().get(i);

                        if (u != null) {
                            if (!logins.isEmpty()) {
                                logins += ",";
                            }

                            logins += u.getLogin();
                        }
                    }

                    pw.println(torneo.getIdTorneo() + ";"
                            + limpiarCampo(inscripcion.getIdInscripcion()) + ";"
                            + formatearFechaSegura(inscripcion.getFecha()) + ";"
                            + limpiarCampo(logins) + ";"
                            + inscripcion.getCantidadCupos() + ";"
                            + inscripcion.getCuposReservados() + ";"
                            + inscripcion.getCuposRegulares() + ";"
                            + inscripcion.isEsEmpleado() + ";"
                            + inscripcion.getMontoPagado() + ";"
                            + inscripcion.isPagoConfirmado() + ";"
                            + inscripcion.isElegiblePrecioMetalico());
                }
            }
        } catch (Exception e) {
            System.err.println("Error guardando inscripciones de torneos: " + e.getMessage());
        }
    }

    private void cargarPagosTorneos(GestorTorneo gestor) {
        File archivo = new File(rutaArchivos + "pagos_torneos.csv");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] p = linea.split(";", -1);

                // Formato:
                // idTorneo;loginUsuario;monto

                if (p.length < 3) {
                    System.err.println("Línea de pago de torneo inválida: " + linea);
                    continue;
                }

                Torneo torneo = gestor.buscarTorneo(p[0]);

                if (torneo instanceof TorneoCompetitivo) {
                    TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;
                    competitivo.getPagosPorUsuario().put(p[1], Double.parseDouble(p[2]));
                    competitivo.calcularPozo();
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo pagos de torneos: " + e.getMessage());
        }
    }

    private void guardarPagosTorneos(List<Torneo> torneos) {
        File archivo = new File(rutaArchivos + "pagos_torneos.csv");

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Torneo torneo : torneos) {
                if (torneo instanceof TorneoCompetitivo) {
                    TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;

                    for (Map.Entry<String, Double> entrada : competitivo.getPagosPorUsuario().entrySet()) {
                        pw.println(torneo.getIdTorneo() + ";"
                                + limpiarCampo(entrada.getKey()) + ";"
                                + entrada.getValue());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error guardando pagos de torneos: " + e.getMessage());
        }
    }

    private void cargarBonosTorneos(GestorTorneo gestor, List<Usuario> usuariosTotales) {
        File archivo = new File(rutaArchivos + "bonos_torneos.csv");

        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] p = linea.split(";", -1);

                // Formato:
                // codigo;valor;estado;fechaOtorgado;fechaUsado;loginGanador;idTorneo

                if (p.length < 7) {
                    System.err.println("Línea de bono de torneo inválida: " + linea);
                    continue;
                }

                String codigo = p[0];
                double valor = Double.parseDouble(p[1]);
                String estado = p[2];
                Date fechaOtorgado = parsearFechaSegura(p[3]);
                Date fechaUsado = parsearFechaSegura(p[4]);
                Usuario ganador = buscarUsuarioPorLogin(usuariosTotales, p[5]);
                Torneo torneo = gestor.buscarTorneo(p[6]);

                if (ganador == null || torneo == null) {
                    System.err.println("No se pudo reconstruir bono: " + codigo);
                    continue;
                }

                BonoTorneoAmistoso bono = new BonoTorneoAmistoso(codigo, valor, ganador, torneo);
                bono.setEstado(estado);

                restaurarFechaBono(bono, "fechaOtorgado", fechaOtorgado);
                restaurarFechaBono(bono, "fechaUsado", fechaUsado);

                gestor.getBonos().add(bono);

                if (torneo instanceof TorneoAmistoso) {
                    TorneoAmistoso amistoso = (TorneoAmistoso) torneo;
                    amistoso.getBonos().add(bono);
                    marcarBonoOtorgado(amistoso);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo bonos de torneos: " + e.getMessage());
        }
    }

    private void guardarBonosTorneos(List<BonoTorneoAmistoso> bonos) {
        File archivo = new File(rutaArchivos + "bonos_torneos.csv");

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (BonoTorneoAmistoso bono : bonos) {
                String loginGanador = "";
                String idTorneo = "";

                if (bono.getGanador() != null) {
                    loginGanador = bono.getGanador().getLogin();
                }

                if (bono.getTorneo() != null) {
                    idTorneo = bono.getTorneo().getIdTorneo();
                }

                pw.println(limpiarCampo(bono.getCodigo()) + ";"
                        + bono.getValor() + ";"
                        + limpiarCampo(bono.getEstado()) + ";"
                        + formatearFechaSegura(bono.getFechaOtorgado()) + ";"
                        + formatearFechaSegura(bono.getFechaUsado()) + ";"
                        + limpiarCampo(loginGanador) + ";"
                        + limpiarCampo(idTorneo));
            }
        } catch (Exception e) {
            System.err.println("Error guardando bonos de torneos: " + e.getMessage());
        }
    }

    private Juego buscarJuegoPorNombre(List<Juego> juegos, String nombre) {
        if (juegos == null || nombre == null) {
            return null;
        }

        for (Juego j : juegos) {
            if (j != null && j.getNombre() != null && j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }

        return null;
    }

    private Date parsearFechaSegura(String texto) {
        try {
            if (texto == null || texto.trim().isEmpty()) {
                return null;
            }

            return sdf.parse(texto);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatearFechaSegura(Date fecha) {
        if (fecha == null) {
            return "";
        }

        return sdf.format(fecha);
    }

    private String limpiarCampo(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace(";", ",");
    }

    private void restaurarFechaBono(BonoTorneoAmistoso bono, String nombreCampo, Date fecha) {
        try {
            Field campo = BonoTorneoAmistoso.class.getDeclaredField(nombreCampo);
            campo.setAccessible(true);
            campo.set(bono, fecha);
        } catch (Exception e) {
            System.err.println("No se pudo restaurar " + nombreCampo + " del bono: " + e.getMessage());
        }
    }

    private void marcarBonoOtorgado(TorneoAmistoso torneo) {
        try {
            Field campo = TorneoAmistoso.class.getDeclaredField("bonoOtorgado");
            campo.setAccessible(true);
            campo.set(torneo, true);
        } catch (Exception e) {
            System.err.println("No se pudo restaurar bonoOtorgado del torneo amistoso: " + e.getMessage());
        }
    }
}