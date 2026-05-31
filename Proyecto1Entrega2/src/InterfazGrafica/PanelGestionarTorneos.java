package InterfazGrafica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelGestionarTorneos extends JPanel {
    
    private JTable tablaTorneos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnFinalizar;

    public PanelGestionarTorneos() {
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Superior de Búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setOpaque(false);
        panelSuperior.add(new JLabel("Buscar por Día:"));
        
        
        txtBuscar = new JTextField(10);
        panelSuperior.add(txtBuscar);
        
        
        btnBuscar = new JButton("Buscar");
        aplicarEstiloBoton(btnBuscar);
        panelSuperior.add(btnBuscar);
        
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla Central
        // CAMBIO IMPLEMENTADO: tabla con más datos útiles para finalizar torneos 
        modeloTabla = new DefaultTableModel(new Object[]{
                "ID", "Nombre", "Día", "Fecha", "Estado", "Cupos", "Inscritos"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTorneos = new JTable(modeloTabla);
        add(new JScrollPane(tablaTorneos), BorderLayout.CENTER);

        // Botón Inferior
      
        btnFinalizar = new JButton("Finalizar Torneo y Registrar Ganador");
        aplicarEstiloBoton(btnFinalizar);
        add(btnFinalizar, BorderLayout.SOUTH);

    }
    
    // CAMBIO IMPLEMENTADO: estilo uniforme para esta pantalla 
    private void aplicarEstiloBoton(JButton boton) {
        boton.setBackground(EstiloUI.COLOR_BANNER_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
    }
    
    // Getters para el controlador 
    public String getBusqueda() { return txtBuscar.getText(); }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnFinalizar() { return btnFinalizar; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTable getTablaTorneos() { return tablaTorneos; }
    
    // CAMBIO IMPLEMENTADO: método helper para saber qué torneo seleccionó el admin
    public String getIdTorneoSeleccionado() {
        int fila = tablaTorneos.getSelectedRow();

        if (fila < 0) {
            return null;
        }

        int filaModelo = tablaTorneos.convertRowIndexToModel(fila);
        Object valor = modeloTabla.getValueAt(filaModelo, 0);

        return valor != null ? valor.toString() : null;
    }
}