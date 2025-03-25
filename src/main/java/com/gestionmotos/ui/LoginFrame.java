package com.gestionmotos.ui;

import com.gestionmotos.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Frame para la pantalla de inicio de sesión.
 */
public class LoginFrame extends JFrame {
    
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    
    /**
     * Constructor que configura los componentes de la ventana.
     */
    public LoginFrame() {
        setTitle("Iniciar Sesión - Sistema de Gestión de Motocicletas");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtUsername = new JTextField(20);
        panel.add(txtUsername, gbc);
        
        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);
        
        // Botón de login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(this::onLoginClicked);
        panel.add(btnLogin, gbc);
        
       
        
        add(panel);
    }
    
    /**
     * Maneja el evento de clic en el botón de login.
     * @param e Evento de acción
     */
    private void onLoginClicked(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese usuario y contraseña", 
                    "Error de validación", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AuthService authService = AuthService.getInstance();
        
        if (authService.login(username, password)) {
            JOptionPane.showMessageDialog(this, 
                    "Bienvenido, " + authService.getCurrentUser().getNombre(), 
                    "Inicio de sesión exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir la ventana principal y cerrar esta
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos", 
                    "Error de autenticación", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}