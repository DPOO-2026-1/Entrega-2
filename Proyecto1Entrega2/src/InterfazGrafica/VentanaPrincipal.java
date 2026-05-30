package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    // Atributo de clase de card layout que estamos usando ahora.
    private CardLayout controlLayout;

    public VentanaPrincipal() {
        super("Ventana Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        // Configuración de layout
        controlLayout = new CardLayout();
        setLayout(controlLayout);
        this.getContentPane().setBackground(Color.decode("#541A1A"));
        // Configuramos los componentes
        configurarComponentes();
    }

    private void configurarComponentes() {

        // ==========================================
        // PASO 1: CREAR TUS PÁGINAS (PANLES)
        // ==========================================
        JPanel paginaInicio = new JPanel();
        JPanel paginaDestino = new JPanel();
        // Añade aquí más paneles si necesitas más páginas...

        // ==========================================
        // PASO 2: AÑADIR LOS BOTONES A LOS PANALES
        // ==========================================
        JButton botonIrADestino = new JButton("Cambiar de página");
        paginaInicio.add(botonIrADestino); // Agregamos el botón al panel de inicio

        // Para añadir otro botón en el futuro:
        // JButton miNuevoBoton = new JButton("Texto");
        // tuPanel.add(miNuevoBoton);

        // ==========================================
        // PASO 3: REGISTRAR LAS PÁGINAS EN LA VENTANA
        // ==========================================
        add(paginaInicio, "NombrePantallaInicio");
        add(paginaDestino, "NombrePantallaDestino");
        // Para registrar más páginas:
        // add(nombreDelPanel, "TextoIdentificadorUnico");

        // ==========================================
        // PASO 4: CONECTAR LOS BOTONES CON LOS PANALES
        // ==========================================

        // Al presionar este botón, se muestra el panel registrado como
        // "NombrePantallaDestino"
        botonIrADestino.addActionListener(e -> controlLayout.show(this.getContentPane(), "NombrePantallaDestino"));

        /*
         * * PLANTILLA PARA CONECTAR MÁS BOTONES:
         * * nombreDeTuBoton.addActionListener(e -> {
         * controlLayout.show(this.getContentPane(),
         * "NombreIdentificadorDeLaPaginaALaQueQuieresIr");
         * });
         */
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}