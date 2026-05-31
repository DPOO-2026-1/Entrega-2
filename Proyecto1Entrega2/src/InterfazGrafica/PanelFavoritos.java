package InterfazGrafica;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Persistencia.GestorPersistencia;
import Usuario.Usuario;
import World.Cafeteria;
import World.Juego;

public class PanelFavoritos extends JPanel {

    private Cafeteria cafeteria;
    private Usuario usuarioActual;
    private GestorPersistencia persistencia;

    private JTable tablaCatalogo;
    private JTable tablaFavoritos;
    private DefaultTableModel modeloCatalogo;
    private DefaultTableModel modeloFavoritos;

    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnVolver;

    public PanelFavoritos() {
        setLayout(null);
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JLabel titulo = new JLabel("  Board Game Cafe - Favoritos");
        titulo.setOpaque(true);
        titulo.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(0, 0, 1280, 60);
        add(titulo);

        JLabel lblCatalogo = seccion("Catálogo de Juegos");
        lblCatalogo.setBounds(210, 85, 250, 35);
        add(lblCatalogo);

        modeloCatalogo = new DefaultTableModel(
                new Object[] { "Nombre", "Categoría", "Año", "Difícil", "Favorito" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCatalogo = tabla(modeloCatalogo);

        JScrollPane scrollCatalogo = new JScrollPane(tablaCatalogo);
        scrollCatalogo.setBounds(160, 140, 850, 140);
        add(scrollCatalogo);

        btnAgregar = boton("Agregar a Favoritos");
        btnAgregar.setBounds(330, 310, 190, 45);
        add(btnAgregar);

        btnQuitar = boton("Quitar de favoritos");
        btnQuitar.setBounds(610, 310, 190, 45);
        add(btnQuitar);

        JLabel lblFavoritos = seccion("Mis favoritos");
        lblFavoritos.setBounds(210, 380, 250, 35);
        add(lblFavoritos);

        modeloFavoritos = new DefaultTableModel(new Object[] { "Nombre", "Categoría" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaFavoritos = tabla(modeloFavoritos);

        JScrollPane scrollFavoritos = new JScrollPane(tablaFavoritos);
        scrollFavoritos.setBounds(210, 435, 290, 150);
        add(scrollFavoritos);

        btnVolver = boton("Volver");
        btnVolver.setBounds(20, 620, 110, 35);
        add(btnVolver);

        btnAgregar.addActionListener(e -> agregarFavorito());
        btnQuitar.addActionListener(e -> quitarFavorito());
    }

    private JLabel seccion(String texto) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    private JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(EstiloUI.COLOR_TEXTO_OSCURO);
        tabla.setRowHeight(35);
        tabla.getTableHeader().setBackground(EstiloUI.COLOR_RECTANGULO_TEXTO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        return tabla;
    }

    public void configurarContexto(Cafeteria cafeteria, Usuario usuarioActual, GestorPersistencia persistencia) {
        this.cafeteria = cafeteria;
        this.usuarioActual = usuarioActual;
        this.persistencia = persistencia;
        refrescar();
    }

    public void setAccionVolver(Runnable accionVolver) {
        for (java.awt.event.ActionListener listener : btnVolver.getActionListeners()) {
            btnVolver.removeActionListener(listener);
        }

        btnVolver.addActionListener(e -> accionVolver.run());
    }

    public void refrescar() {
        modeloCatalogo.setRowCount(0);
        modeloFavoritos.setRowCount(0);

        if (cafeteria == null || usuarioActual == null) {
            return;
        }

        for (Juego juego : cafeteria.getJuegos()) {
            boolean favorito = usuarioActual.getJuegosFavoritos().contains(juego);

            modeloCatalogo.addRow(new Object[] {
                    juego.getNombre(),
                    juego.getCategoria(),
                    juego.getAnioPublicacion(),
                    juego.isEsDificil() ? "Sí" : "-",
                    favorito ? "Sí" : "-"
            });
        }

        for (Juego juego : usuarioActual.getJuegosFavoritos()) {
            modeloFavoritos.addRow(new Object[] {
                    juego.getNombre(),
                    juego.getCategoria()
            });
        }
    }

    private void agregarFavorito() {
        int fila = tablaCatalogo.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego del catálogo.");
            return;
        }

        String nombreJuego = (String) modeloCatalogo.getValueAt(
                tablaCatalogo.convertRowIndexToModel(fila), 0);

        Juego juego = cafeteria.buscarJuego(nombreJuego);

        if (juego != null) {
            usuarioActual.agregarFavorito(juego);
            guardar();
            refrescar();
        }
    }

    private void quitarFavorito() {
        int fila = tablaFavoritos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un favorito para quitarlo.");
            return;
        }

        String nombreJuego = (String) modeloFavoritos.getValueAt(
                tablaFavoritos.convertRowIndexToModel(fila), 0);

        Juego juego = cafeteria.buscarJuego(nombreJuego);

        if (juego != null) {
            usuarioActual.eliminarFavorito(juego);
            guardar();
            refrescar();
        }
    }

    private void guardar() {
        if (persistencia != null && cafeteria != null) {
            persistencia.guardarTodo(cafeteria);
        }
    }
}