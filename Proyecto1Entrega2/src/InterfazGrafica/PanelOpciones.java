package InterfazGrafica;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class PanelOpciones extends JPanel {
    private JButton btnEmpleado;
    private JButton btnAdministrador;
    private JButton btnCliente;

    public PanelOpciones() {
        // Layout principal para separar el banner superior del contenido central
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        // 1. Banner Superior
        JPanel panelBanner = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBanner.setBackground(EstiloUI.COLOR_BANNER_CAFE);

        JLabel textBanner = new JLabel("Board Game Cafe; nos alegra verte de nuevo");
        textBanner.setFont(EstiloUI.FUENTE_TITULO);
        panelBanner.add(textBanner);

        add(panelBanner, BorderLayout.NORTH);

        // Usamos layouts anidados para ordenar toda la visual
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Usamos otro GridBackLayout para poder meter nuestras otras classes de panels
        // acá dentro
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        // Queremos que esté transparente
        panelIzquierdo.setOpaque(false);
        GridBagConstraints gbcIzquierda = new GridBagConstraints();
        gbcIzquierda.insets = new Insets(10, 5, 10, 5);
        gbcIzquierda.anchor = GridBagConstraints.WEST;

        // Etiqueta de Eres.
        gbcIzquierda.gridy = 0;
        JLabel lblEres = new JLabel("Eres...");
        lblEres.setFont(EstiloUI.FUENTE_TITULO);
        panelIzquierdo.add(lblEres, gbcIzquierda);

        // Metemos el panel de los botones en la siguiente fila
        gbcIzquierda.gridy = 1;
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 0));
        panelBotones.setOpaque(false);

        btnEmpleado = new JButton("Empleado");
        btnAdministrador = new JButton("Administrador");
        btnCliente = new JButton("Cliente");
        
        // Cambio porque me molestaba que estuvieran mal los colores
        
        aplicarEstiloBotonRol(btnEmpleado);
        aplicarEstiloBotonRol(btnAdministrador);
        aplicarEstiloBotonRol(btnCliente);
        
        panelBotones.add(btnEmpleado);
        panelBotones.add(btnAdministrador);
        panelBotones.add(btnCliente);

        panelIzquierdo.add(panelBotones, gbcIzquierda);

        // Ponemos nuestros botones en la parte izquierda del trabajo.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 50, 0, 50);
        panelCentro.add(panelIzquierdo, gbc);

        // En el bloque derecho central metemos de nuevo la imágen que estamos usando de
        // logo.
        gbc.gridx = 1;
        gbc.gridy = 0;
        ImageIcon iconoTaza = new ImageIcon(
                "src\\InterfazGrafica\\gameCafe.png");
        JLabel labelImagen = new JLabel(iconoTaza);
        panelCentro.add(labelImagen, gbc);

        add(panelCentro, BorderLayout.CENTER);
    }
    
    // método auxiliar para no repetir estilo
    private void aplicarEstiloBotonRol(JButton boton) {
        boton.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFont(EstiloUI.FUENTE_ETIQUETA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
    }

    // Usamos getters y setters para poder hacer el redigimiento con action
    // listeners en Ventana Principal
    public JButton getBtnEmpleado() {
        return btnEmpleado;
    }

    public JButton getBtnAdministrador() {
        return btnAdministrador;
    }

    public JButton getBtnCliente() {
        return btnCliente;
    }
}