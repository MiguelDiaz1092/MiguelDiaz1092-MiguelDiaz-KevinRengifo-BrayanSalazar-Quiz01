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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Motocicleta"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField(10);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);
        
        // Marca
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Marca:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMarca = new JTextField(20);
        formPanel.add(txtMarca, gbc);
        
        // Cilindraje
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Cilindraje:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCilindraje = new JTextField(10);
        formPanel.add(txtCilindraje, gbc);
        
        // Precio
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Precio:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPrecio = new JTextField(10);
        formPanel.add(txtPrecio, gbc);
        
        // Color
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Color:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtColor = new JTextField(10);
        formPanel.add(txtColor, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(this::onGuardarClicked);
        buttonPanel.add(btnGuardar);
        
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(this::onActualizarClicked);
        buttonPanel.add(btnActualizar);
        
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(this::onEliminarClicked);
        buttonPanel.add(btnEliminar);
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        buttonPanel.add(btnLimpiar);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::onBuscarClicked);
        buttonPanel.add(btnBuscar);
        
        // Panel de tabla
        String[] columnas = {"ID", "Marca", "Cilindraje", "Precio", "Color"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableMoto = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableMoto);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Motocicletas"));
        
        tableMoto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableMoto.getSelectedRow() != -1) {
                int row = tableMoto.getSelectedRow();
                mostrarMotocicletaSeleccionada(row);
            }
        });
        
        // Menú
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
        
        // Agregar componentes al panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
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
        
        if (txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El precio es obligatorio",
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