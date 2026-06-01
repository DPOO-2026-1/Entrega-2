package InterfazGrafica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelMenuAdmin extends JPanel {

    private JTable tablaMenu;
    private JTable tablaSugerencias;

    private DefaultTableModel modeloMenu;
    private DefaultTableModel modeloSugerencias;

    private JTextField txtNombreProducto;
    private JTextField txtPrecioProducto;
    private JComboBox<String> cbTipoProducto;
    private JTextField txtAlergenos;

    private JButton btnActualizar;
    private JButton btnAgregarProducto;
    private JButton btnAprobarSugerencia;
    private JButton btnRechazarSugerencia;

    public PanelMenuAdmin() {
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUI.COLOR_FONDO_BEIGE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión del Menú y Sugerencias");
        titulo.setFont(EstiloUI.FUENTE_TITULO);
        titulo.setForeground(EstiloUI.COLOR_TEXTO_OSCURO);
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 15));
        panelCentro.setOpaque(false);

        modeloMenu = new DefaultTableModel(new Object[]{"Nombre", "Precio", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMenu = new JTable(modeloMenu);
        tablaMenu.setRowHeight(24);

        JPanel panelMenu = new JPanel(new BorderLayout(8, 8));
        panelMenu.setOpaque(false);
        panelMenu.add(new JLabel("Menú actual"), BorderLayout.NORTH);
        panelMenu.add(new JScrollPane(tablaMenu), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 8, 8));
        panelFormulario.setOpaque(false);

        txtNombreProducto = new JTextField();
        txtPrecioProducto = new JTextField();
        cbTipoProducto = new JComboBox<>(new String[]{"Bebida", "Pasteleria"});
        txtAlergenos = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombreProducto);

        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecioProducto);

        panelFormulario.add(new JLabel("Tipo:"));
        panelFormulario.add(cbTipoProducto);

        panelFormulario.add(new JLabel("Alérgenos separados por coma:"));
        panelFormulario.add(txtAlergenos);

        btnAgregarProducto = crearBoton("Agregar producto");
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnAgregarProducto);

        panelMenu.add(panelFormulario, BorderLayout.SOUTH);

        modeloSugerencias = new DefaultTableModel(new Object[]{"#", "Descripción", "Empleado", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSugerencias = new JTable(modeloSugerencias);
        tablaSugerencias.setRowHeight(24);

        JPanel panelSugerencias = new JPanel(new BorderLayout(8, 8));
        panelSugerencias.setOpaque(false);
        panelSugerencias.add(new JLabel("Sugerencias de empleados"), BorderLayout.NORTH);
        panelSugerencias.add(new JScrollPane(tablaSugerencias), BorderLayout.CENTER);

        JPanel panelBotonesSugerencias = new JPanel(new GridLayout(1, 3, 8, 8));
        panelBotonesSugerencias.setOpaque(false);

        btnActualizar = crearBoton("Actualizar");
        btnAprobarSugerencia = crearBoton("Aprobar");
        btnRechazarSugerencia = crearBoton("Rechazar");

        panelBotonesSugerencias.add(btnActualizar);
        panelBotonesSugerencias.add(btnAprobarSugerencia);
        panelBotonesSugerencias.add(btnRechazarSugerencia);

        panelSugerencias.add(panelBotonesSugerencias, BorderLayout.SOUTH);

        panelCentro.add(panelMenu);
        panelCentro.add(panelSugerencias);

        add(panelCentro, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(EstiloUI.COLOR_COMPONENTE_CAFE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        return boton;
    }

    public JTable getTablaSugerencias() {
        return tablaSugerencias;
    }

    public DefaultTableModel getModeloMenu() {
        return modeloMenu;
    }

    public DefaultTableModel getModeloSugerencias() {
        return modeloSugerencias;
    }

    public String getNombreProducto() {
        return txtNombreProducto.getText().trim();
    }

    public double getPrecioProducto() {
        return Double.parseDouble(txtPrecioProducto.getText().trim());
    }

    public String getTipoProducto() {
        Object seleccionado = cbTipoProducto.getSelectedItem();
        return seleccionado == null ? "Bebida" : seleccionado.toString();
    }

    public String getAlergenos() {
        return txtAlergenos.getText().trim();
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnAgregarProducto() {
        return btnAgregarProducto;
    }

    public JButton getBtnAprobarSugerencia() {
        return btnAprobarSugerencia;
    }

    public JButton getBtnRechazarSugerencia() {
        return btnRechazarSugerencia;
    }

    public int getIndiceSugerenciaSeleccionada() {
        int fila = tablaSugerencias.getSelectedRow();

        if (fila < 0) {
            return -1;
        }

        int filaModelo = tablaSugerencias.convertRowIndexToModel(fila);
        Object valor = modeloSugerencias.getValueAt(filaModelo, 0);

        return Integer.parseInt(valor.toString());
    }

    public void limpiarFormularioProducto() {
        txtNombreProducto.setText("");
        txtPrecioProducto.setText("");
        txtAlergenos.setText("");
        cbTipoProducto.setSelectedIndex(0);
    }
}