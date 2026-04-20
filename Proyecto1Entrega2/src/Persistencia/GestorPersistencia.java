package Proyecto1Entrega2.src.Persistencia;

import Proyecto1Entrega2.src.ModuloVenta.Venta;
import Proyecto1Entrega2.src.Usuario.Usuario;
import Proyecto1Entrega2.src.Usuario.Cliente;
import Proyecto1Entrega2.src.Usuario.Administrador;
import Proyecto1Entrega2.src.Usuario.Mesero;
import Proyecto1Entrega2.src.Usuario.Cocinero;
import Proyecto1Entrega2.src.World.Cafeteria;
import Proyecto1Entrega2.src.World.Juego;
import Proyecto1Entrega2.src.World.Prestamo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorPersistencia {
    
    private String rutaArchivos; 
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

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
        if (!archivo.exists()) return usuarios;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                String tipo = p[0];
                String login = p[1];
                String password = p[2];
                String nombre = p[3];
                String extra = p[4]; 
                
                if (tipo.equals("CLIENTE")) {
                    int puntos = Integer.parseInt(extra);
                    usuarios.add(new Cliente(login, password, nombre, puntos));
                } else if (tipo.equals("MESERO")) {
                    usuarios.add(new Mesero(login, password, nombre, extra)); // extra es el codigoDescuento
                } else if (tipo.equals("COCINERO")) {
                    usuarios.add(new Cocinero(login, password, nombre, extra));
                } else if (tipo.equals("ADMINISTRADOR")) {
                    usuarios.add(new Administrador(login, password, nombre));
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public void guardarUsuarios(List<Usuario> lista) {
        File archivo = new File(rutaArchivos + "usuarios.csv");
        archivo.getParentFile().mkdirs(); 

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Usuario u : lista) {
                String tipo = u.getClass().getSimpleName().toUpperCase();
                String extra = "";

                if (u instanceof Empleado) {
                    // Casteamos a Empleado para acceder al código
                    extra = ((Empleado) u).getCodigoDescuento();
                } else if (u instanceof Cliente) {
                    // Casteamos a Cliente para los puntos
                    extra = String.valueOf(((Cliente) u).getPuntosFidelidad());
                }

                // Formato: TIPO;login;password;nombre;extra
                pw.println(tipo + ";" + u.getLogin() + ";" + u.getPassword() + ";" + u.getNombre() + ";" + extra);
            }
        } catch (Exception e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }
    // 3. MÉTODOS DE JUEGOS

    public List<Juego> cargarJuegos() {
        List<Juego> juegos = new ArrayList<>();
        File archivo = new File(rutaArchivos + "juegos.csv");
        if (!archivo.exists()) return juegos;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
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
        archivo.getParentFile().mkdirs(); 

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Juego j : lista) {
                pw.println(j.getNombre() + ";" + j.getAnioPublicacion() + ";" + j.getEmpresaMatriz() + ";" +
                           j.getMinJugadores() + ";" + j.getMaxJugadores() + ";" + j.getEdadMinima() + ";" +
                           j.getCategoria() + ";" + j.isEsDificil());
            }
        } catch (Exception e) {
            System.err.println("Error guardando juegos: " + e.getMessage());
        }
    }

    // 4. MÉTODOS DE PRÉSTAMOS

    public List<Prestamo> cargarPrestamos(List<Usuario> usuariosTotales, List<Juego> juegosTotales) {
        List<Prestamo> prestamos = new ArrayList<>();
        File archivo = new File(rutaArchivos + "prestamos.csv");
        if (!archivo.exists()) return prestamos;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                String estado = p[0];
                Date fechaInicio = sdf.parse(p[1]);
                String loginUsuario = p[2]; 
                String nombreJuego = p[3];  

                Usuario usuarioSolicitante = null;
                for (Usuario u : usuariosTotales) {
                    if (u.getLogin().equals(loginUsuario)) {
                        usuarioSolicitante = u;
                        break;
                    }
                }

                Juego juegoAsociado = null;
                for (Juego j : juegosTotales) {
                    if (j.getNombre().equals(nombreJuego)) {
                        juegoAsociado = j;
                        break;
                    }
                }

                if (usuarioSolicitante != null && juegoAsociado != null) {
                    Prestamo prestamo = new Prestamo(estado, fechaInicio, usuarioSolicitante, juegoAsociado);
                    prestamos.add(prestamo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo préstamos: " + e.getMessage());
        }
        return prestamos;
    }

    public void guardarPrestamos(List<Prestamo> lista) {
        File archivo = new File(rutaArchivos + "prestamos.csv");
        archivo.getParentFile().mkdirs(); 

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Prestamo p : lista) {
                String fechaFormateada = sdf.format(p.getFechaHoraInicio());
                pw.println(p.getEstado() + ";" + fechaFormateada + ";" + 
                           p.getSolicitadoPor().getLogin() + ";" + p.getJuegoAsociado().getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error guardando préstamos: " + e.getMessage());
        }
    }

    // 5. MÉTODOS DE VENTAS

    public List<Venta> cargarVentas(List<Usuario> usuariosTotales) {
        List<Venta> ventas = new ArrayList<>();
        File archivo = new File(rutaArchivos + "ventas.csv");
        if (!archivo.exists()) return ventas;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                int idVenta = Integer.parseInt(p[0]);
                Date fecha = sdf.parse(p[1]);
                double total = Double.parseDouble(p[2]);
                String loginUsuario = p[3];

                Usuario realizadaPor = null;
                for (Usuario u : usuariosTotales) {
                    if (u.getLogin().equals(loginUsuario)) {
                        realizadaPor = u;
                        break;
                    }
                }

                if (realizadaPor != null) {
                    Venta v = new Venta(idVenta, fecha, total, realizadaPor);
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
        archivo.getParentFile().mkdirs(); 

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Venta v : lista) {
                String fechaFormateada = sdf.format(v.getFecha());
                pw.println(v.getIdVenta() + ";" + fechaFormateada + ";" + 
                           v.getTotal() + ";" + v.getRealizadaPor().getLogin());
            }
        } catch (Exception e) {
            System.err.println("Error guardando ventas: " + e.getMessage());
        }
    }
}