package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelRegistrarEmpleado extends JPanel {
    private JTextField txtNombre;
    private JTextField txtDocumento;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JTextField txtCodigoDescuento;
    private JComboBox<String> cbTipo;
    private JButton btnGuardar;

    public PanelRegistrarEmpleado() {
        setLayout(new GridBagLayout());
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registrar Nuevo Empleado");
        lblTitulo.setFont(EstiloUI.FUENTE_TITULO);
        lblTitulo.setForeground(EstiloUI.COLOR_TEXTO_OSCURO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(18);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Documento:"), gbc);
        txtDocumento = new JTextField(18);
        gbc.gridx = 1;
        add(txtDocumento, gbc);

        // CAMBIO IMPLEMENTADO: campos obligatorios de autenticación del empleado
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Login:"), gbc);
        txtLogin = new JTextField(18);
        gbc.gridx = 1;
        add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(18);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Código descuento:"), gbc);
        txtCodigoDescuento = new JTextField(18);
        gbc.gridx = 1;
        add(txtCodigoDescuento, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Tipo de Rol:"), gbc);
        cbTipo = new JComboBox<>(new String[]{"Mesero", "Cocinero"});
        gbc.gridx = 1;
        add(cbTipo, gbc);

        btnGuardar = new JButton("Registrar Empleado");
        btnGuardar.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(btnGuardar, gbc);
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getDocumento() {
        return txtDocumento.getText().trim();
    }

    public String getTipoRol() {
        return (String) cbTipo.getSelectedItem();
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    // CAMBIO IMPLEMENTADO: getters nuevos para conectar el controlador
    public String getLoginEmpleado() {
        return txtLogin.getText().trim();
    }

    public String getPasswordEmpleado() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getCodigoDescuento() {
        return txtCodigoDescuento.getText().trim();
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtDocumento.setText("");
        txtLogin.setText("");
        txtPassword.setText("");
        txtCodigoDescuento.setText("");
        cbTipo.setSelectedIndex(0);
    }
}