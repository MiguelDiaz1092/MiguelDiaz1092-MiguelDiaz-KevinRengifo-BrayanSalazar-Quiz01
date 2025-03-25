package com.gestionmotos.ui;

import com.gestionmotos.model.Motocicleta;
import com.gestionmotos.repository.MotocicletaRepository;
import com.gestionmotos.repository.MotocicletaRepositoryImpl;
import com.gestionmotos.service.AuthService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Frame principal para la gestión de motocicletas.
 */
public class MainFrame extends JFrame {
    
    private final MotocicletaRepository motoRepository;
    private final AuthService authService;
    
    private JTable tableMoto;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtMarca, txtCilindraje, txtPrecio, txtColor;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnBuscar;
    
    /**
     * Constructor que configura los componentes de la ventana.
     */
    public MainFrame() {
        motoRepository = new MotocicletaRepositoryImpl();
        authService = AuthService.getInstance();
        
        if (!authService.isAuthenticated()) {
            JOptionPane.showMessageDialog(this,
                    "Debe iniciar sesión para acceder al sistema",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        initComponents();
        loadMotocicletas();
    }
    
    /**
     * Inicializa los componentes de la interfaz.
     */
    private void initComponents() {
        setTitle("Sistema de Gestión de Motocicletas - Usuario: " + authService.getCurrentUser().getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel contenedor principal con un BorderLayout global
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ===== Panel superior para el formulario =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos de la Motocicleta"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel lblId = new JLabel("ID:");
        lblId.setPreferredSize(new Dimension(80, 25));
        formPanel.add(lblId, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        txtId = new JTextField();
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);
        
        // Marca
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setPreferredSize(new Dimension(80, 25));
        formPanel.add(lblMarca, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        txtMarca = new JTextField();
        formPanel.add(txtMarca, gbc);
        
        // Cilindraje
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblCilindraje = new JLabel("Cilindraje:");
        lblCilindraje.setPreferredSize(new Dimension(80, 25));
        formPanel.add(lblCilindraje, gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        txtCilindraje = new JTextField();
        formPanel.add(txtCilindraje, gbc);
        
        // Precio
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setPreferredSize(new Dimension(80, 25));
        formPanel.add(lblPrecio, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        txtPrecio = new JTextField();
        formPanel.add(txtPrecio, gbc);
        
        // Color
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblColor = new JLabel("Color:");
        lblColor.setPreferredSize(new Dimension(80, 25));
        formPanel.add(lblColor, gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        txtColor = new JTextField();
        formPanel.add(txtColor, gbc);
        
        // ===== Panel de botones =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(120, 30));
        btnGuardar.addActionListener(this::onGuardarClicked);
        buttonPanel.add(btnGuardar);
        
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setPreferredSize(new Dimension(120, 30));
        btnActualizar.addActionListener(this::onActualizarClicked);
        buttonPanel.add(btnActualizar);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(new Dimension(120, 30));
        btnEliminar.addActionListener(this::onEliminarClicked);
        buttonPanel.add(btnEliminar);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(120, 30));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        buttonPanel.add(btnLimpiar);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(120, 30));
        btnBuscar.addActionListener(this::onBuscarClicked);
        buttonPanel.add(btnBuscar);
        
        // ===== Panel para la tabla =====
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Lista de Motocicletas"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Configuración de la tabla
        String[] columnas = {"ID", "Marca", "Cilindraje", "Precio", "Color"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) return Integer.class;
                if (columnIndex == 3) return Double.class;
                return String.class;
            }
        };
        
        tableMoto = new JTable(tableModel);
        tableMoto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMoto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableMoto.getTableHeader().setReorderingAllowed(false);
        
        // Configurar anchos de columna
        tableMoto.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableMoto.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableMoto.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableMoto.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableMoto.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // Escuchar selecciones en la tabla
        tableMoto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableMoto.getSelectedRow() != -1) {
                int row = tableMoto.getSelectedRow();
                mostrarMotocicletaSeleccionada(row);
            }
        });
        
        // Agregar la tabla a un JScrollPane para permitir el desplazamiento
        JScrollPane scrollPane = new JScrollPane(tableMoto);
        scrollPane.setPreferredSize(new Dimension(800, 350));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== Menú =====
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem menuSalir = new JMenuItem("Salir");
        menuSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(menuSalir);
        
        JMenu menuUsuario = new JMenu("Usuario");
        JMenuItem menuCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuCerrarSesion.addActionListener(e -> cerrarSesion());
        menuUsuario.add(menuCerrarSesion);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuUsuario);
        setJMenuBar(menuBar);
        
        // ===== Añadir componentes al panel principal =====
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(tablePanel, BorderLayout.SOUTH);
        
        // Establecer el panel principal
        setContentPane(contentPane);
    }
    
    /**
     * Carga las motocicletas desde la base de datos a la tabla.
     */
    private void loadMotocicletas() {
        tableModel.setRowCount(0);
        
        List<Motocicleta> motos = motoRepository.findAll();
        
        for (Motocicleta moto : motos) {
            Object[] row = {
                moto.getId(),
                moto.getMarca(),
                moto.getCilindraje(),
                moto.getPrecio(),
                moto.getColor()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Muestra los datos de la motocicleta seleccionada en el formulario.
     * @param row Índice de la fila seleccionada
     */
    private void mostrarMotocicletaSeleccionada(int row) {
        txtId.setText(tableMoto.getValueAt(row, 0).toString());
        txtMarca.setText(tableMoto.getValueAt(row, 1).toString());
        txtCilindraje.setText(tableMoto.getValueAt(row, 2).toString());
        txtPrecio.setText(tableMoto.getValueAt(row, 3).toString());
        txtColor.setText(tableMoto.getValueAt(row, 4).toString());
    }
    
    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        txtId.setText("");
        txtMarca.setText("");
        txtCilindraje.setText("");
        txtPrecio.setText("");
        txtColor.setText("");
        tableMoto.clearSelection();
    }
    
    /**
     * Evento para el botón guardar.
     * @param e Evento de acción
     */
    private void onGuardarClicked(ActionEvent e) {
        try {
            if (!validarFormulario()) {
                return;
            }
            
            String marca = txtMarca.getText();
            int cilindraje = Integer.parseInt(txtCilindraje.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            String color = txtColor.getText();
            
            Motocicleta moto = new Motocicleta(marca, cilindraje, precio, color);
            motoRepository.save(moto);
            
            JOptionPane.showMessageDialog(this,
                    "Motocicleta guardada correctamente",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            
            limpiarFormulario();
            loadMotocicletas();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en los datos numéricos. Cilindraje debe ser un número entero y Precio un número decimal.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Evento para el botón actualizar.
     * @param e Evento de acción
     */
    private void onActualizarClicked(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una motocicleta para actualizar",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!validarFormulario()) {
                return;
            }
            
            int id = Integer.parseInt(txtId.getText());
            String marca = txtMarca.getText();
            int cilindraje = Integer.parseInt(txtCilindraje.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            String color = txtColor.getText();
            
            Motocicleta moto = new Motocicleta(id, marca, cilindraje, precio, color);
            
            if (motoRepository.update(moto)) {
                JOptionPane.showMessageDialog(this,
                        "Motocicleta actualizada correctamente",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                
                limpiarFormulario();
                loadMotocicletas();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar la motocicleta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en los datos numéricos. Cilindraje debe ser un número entero y Precio un número decimal.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Evento para el botón eliminar.
     * @param e Evento de acción
     */
    private void onEliminarClicked(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una motocicleta para eliminar",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(txtId.getText());
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar esta motocicleta?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (motoRepository.deleteById(id)) {
                    JOptionPane.showMessageDialog(this,
                            "Motocicleta eliminada correctamente",
                            "Operación exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    limpiarFormulario();
                    loadMotocicletas();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo eliminar la motocicleta",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Evento para el botón buscar.
     * @param e Evento de acción
     */
    private void onBuscarClicked(ActionEvent e) {
        String busqueda = JOptionPane.showInputDialog(this,
                "Ingrese la marca a buscar:",
                "Buscar motocicleta",
                JOptionPane.QUESTION_MESSAGE);
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            tableModel.setRowCount(0);
            
            List<Motocicleta> motos = motoRepository.findByMarca(busqueda);
            
            if (motos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron motocicletas con la marca: " + busqueda,
                        "Búsqueda sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                loadMotocicletas();
            } else {
                for (Motocicleta moto : motos) {
                    Object[] row = {
                        moto.getId(),
                        moto.getMarca(),
                        moto.getCilindraje(),
                        moto.getPrecio(),
                        moto.getColor()
                    };
                    tableModel.addRow(row);
                }
            }
        } else if (busqueda != null) {
            loadMotocicletas();
        }
    }
    
    /**
     * Valida los campos del formulario.
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validarFormulario() {
        if (txtMarca.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La marca es obligatoria",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtMarca.requestFocus();
            return false;
        }
        
        if (txtCilindraje.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El cilindraje es obligatorio",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtCilindraje.requestFocus();
            return false;
        }
        
        try {
            int cilindraje = Integer.parseInt(txtCilindraje.getText().trim());
            if (cilindraje <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El cilindraje debe ser un número positivo",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                txtCilindraje.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El cilindraje debe ser un número entero",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtCilindraje.requestFocus();
            return false;
        }
        
        if (txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El precio es obligatorio",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return false;
        }
        
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio debe ser un número positivo",
                        "Error de validación",
                        JOptionPane.WARNING_MESSAGE);
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número decimal",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return false;
        }
        
        if (txtColor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El color es obligatorio",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            txtColor.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Cierra la sesión actual y vuelve a la pantalla de login.
     */
    private void cerrarSesion() {
        authService.logout();
        dispose();
        new LoginFrame().setVisible(true);
    }
}