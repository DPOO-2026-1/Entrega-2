package InterfazGrafica;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.*;

public class PanelRegistrarCliente extends JPanel {
    private JButton logiButton;
    private JTextField campoTextUsuario;
    private JPasswordField campoContrasenia;

    public PanelRegistrarCliente() {
        // Estabamos queriendo algo sofisticado en este panel; por lo que usamos border
        // layout y
        // Creamos diferentes layouts para cada componente, los cuales metimos luego
        // dentro de este.
        setLayout(new BorderLayout());
        // BorderLayout nos permite ordenar en NSWE y Center.
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);

        JPanel panelBannerBienvenidos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBannerBienvenidos.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        // Para meter el texto.
        JLabel textBienvenido = new JLabel("Registrar Cliente");
        textBienvenido.setFont(EstiloUI.FUENTE_TITULO);
        panelBannerBienvenidos.add(textBienvenido);

        // Como para este panel tenemos border layout, lo metemos en North.
        add(panelBannerBienvenidos, BorderLayout.NORTH);

        // Ahora usamos GridBackLayout para todo lo central (Foto de Game Cafe y el
        // apartado login).
        JPanel panelCentro = new JPanel(new GridBagLayout());
        // Queremos que se siga viendo el beige de fondo.
        panelCentro.setOpaque(false);
        // Cuando usamos Grid Back Layout, tenemos que tener un objeto para
        // configurarlo.
        GridBagConstraints gbc = new GridBagConstraints();

        // Formulario Log in

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbcFormulario = new GridBagConstraints();
        // Pone un espacio de margen para el componente.
        gbcFormulario.insets = new Insets(5, 5, 5, 5);
        // Anclamos al west.
        gbcFormulario.anchor = GridBagConstraints.WEST;

        // Metemos nuestras filas para el formulario Login

        gbcFormulario.gridy = 0;
        panelFormulario.add(new JLabel("Ingrese su usuario:"), gbcFormulario);

        // segunda fila
        gbcFormulario.gridy = 1;
        campoTextUsuario = new JTextField(20);
        panelFormulario.add(campoTextUsuario, gbcFormulario);

        // Texto para contraseña
        gbcFormulario.gridy = 2;
        // Los objetos se meten dentro del objeto panel; junto con la clase que contiene
        // las constraints.
        panelFormulario.add(new JLabel("Ponga su contraseña a continuación; "), gbcFormulario);

        gbcFormulario.gridy = 3;
        campoContrasenia = new JPasswordField(20);
        panelFormulario.add(campoContrasenia, gbcFormulario);

        gbcFormulario.gridy = 4;
        gbcFormulario.anchor = GridBagConstraints.EAST;
        logiButton = new JButton("Login");
        panelFormulario.add(logiButton, gbcFormulario);

        // Meter el formulario en la columna 0 del centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 50, 0, 50); // Margen grande para separarlo de la taza
        panelCentro.add(panelFormulario, gbc);

        // Metemos la imágen de la taza
        ImageIcon iconoTaza = new ImageIcon(
                "C:\\Users\\juand\\git\\Entrega-2\\Proyecto1Entrega2\\src\\InterfazGrafica\\gameCafe.png");
        JLabel labelImagen = new JLabel(iconoTaza);

        // Meter la imagen en la columna 1 del centro
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelCentro.add(labelImagen, gbc);

        // Pega todo el bloque central armado en el centro del BorderLayout
        add(panelCentro, BorderLayout.CENTER);
    }

    // Getters para la VentanaPrincipal
    public JButton getBotonLogin() {
        return logiButton;
    }

    public String getUsuario() {
        return campoTextUsuario.getText();
    }

    public String getContrasena() {
        return new String(campoContrasenia.getPassword());
    }
}
