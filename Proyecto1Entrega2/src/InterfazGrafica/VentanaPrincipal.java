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
        // Configuramos los componentes
        configurarComponentes();
    }

    private void configurarComponentes() {
        PanelOpciones PanelOpciones = new PanelOpciones();
        PanelLogin PanelLogin = new PanelLogin();

        this.add(PanelOpciones, "PanelOpciones");
        this.add(PanelLogin, "PanelLogin");
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    }
}