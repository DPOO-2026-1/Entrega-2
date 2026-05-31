package InterfazGrafica;

import javax.swing.*;
import java.awt.*;

public class PanelCrearTorneo extends JPanel {
    private JButton btnVolver;
    
    private JComboBox<String> cbJuegoAsociado;
    private JTextField txtDuracion;
    private JTextField txtCupoTotal;
    private JTextField txtFechaInicio; // NUEVO: Para ingresar la fecha real
    private JComboBox<String> cbTipoTorneo;
    
    private JLabel lblDinamico;
    private JTextField txtValorDinamico;
    private JButton btnValidarCrear;

    public PanelCrearTorneo() {
        setLayout(new BorderLayout());
        setBackground(new Color(243, 235, 225)); 

        // Panel Superior
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panelNorte.setOpaque(false);
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(176, 125, 85)); 
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        
        JLabel lblTitulo = new JLabel("Crear Torneo");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        
        panelNorte.add(btnVolver);
        panelNorte.add(lblTitulo);
        add(panelNorte, BorderLayout.NORTH);

        // Panel Central
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        cbJuegoAsociado = new JComboBox<>(); 
        txtDuracion = new JTextField(15);
        txtCupoTotal = new JTextField(15);
        txtFechaInicio = new JTextField(15); // Campo de fecha
        cbTipoTorneo = new JComboBox<>(new String[]{"Competitivo", "Amistoso"});
        
        lblDinamico = new JLabel("Tarifa de Ingreso ($):");
        lblDinamico.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtValorDinamico = new JTextField(15);

        cbTipoTorneo.addActionListener(e -> {
            if (cbTipoTorneo.getSelectedItem().equals("Amistoso")) {
                lblDinamico.setText("Valor del Bono ($):");
            } else {
                lblDinamico.setText("Tarifa de Ingreso ($):");
            }
        });

        Font fuenteLabel = new Font("SansSerif", Font.PLAIN, 16);

        agregarFila(panelCentro, gbc, 0, "Nombre del torneo (Juego):", cbJuegoAsociado, fuenteLabel);
        agregarFila(panelCentro, gbc, 1, "Duración en minutos:", txtDuracion, fuenteLabel);
        agregarFila(panelCentro, gbc, 2, "Cupo total:", txtCupoTotal, fuenteLabel);
        agregarFila(panelCentro, gbc, 3, "Fecha inicio (YYYY/MM/DD):", txtFechaInicio, fuenteLabel);
        agregarFila(panelCentro, gbc, 4, "Tipo de Torneo:", cbTipoTorneo, fuenteLabel);
        
        gbc.gridy = 5; gbc.gridx = 0;
        panelCentro.add(lblDinamico, gbc);
        gbc.gridx = 1;
        panelCentro.add(txtValorDinamico, gbc);

        // Botón Inferior
        btnValidarCrear = new JButton("Crear torneo");
        btnValidarCrear.setBackground(new Color(176, 125, 85));
        btnValidarCrear.setForeground(Color.WHITE);
        btnValidarCrear.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnValidarCrear.setPreferredSize(new Dimension(200, 40));
        
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0));
        panelBoton.add(btnValidarCrear);
        
        add(panelCentro, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String texto, JComponent componente, Font font) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        JLabel lbl = new JLabel(texto);
        lbl.setFont(font);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    // Getters
    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnValidarCrear() { return btnValidarCrear; }
    public JComboBox<String> getCbJuegoAsociado() { return cbJuegoAsociado; }
    public String getDuracion() { return txtDuracion.getText(); }
    public String getCupoTotal() { return txtCupoTotal.getText(); }
    public String getFechaInicio() { return txtFechaInicio.getText(); }
    public String getTipoTorneo() { return (String) cbTipoTorneo.getSelectedItem(); }
    public String getValorDinamico() { return txtValorDinamico.getText(); }

    public void limpiarCampos() {
        txtDuracion.setText("");
        txtCupoTotal.setText("");
        txtFechaInicio.setText("");
        txtValorDinamico.setText("");
        cbTipoTorneo.setSelectedIndex(0);
        cbJuegoAsociado.setSelectedIndex(0);
    }
}