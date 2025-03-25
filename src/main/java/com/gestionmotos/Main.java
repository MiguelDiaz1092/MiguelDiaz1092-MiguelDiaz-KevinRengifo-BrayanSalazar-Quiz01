package com.gestionmotos;

import com.gestionmotos.ui.LoginFrame;

import javax.swing.*;

/**
 * Clase principal que inicia la aplicación.
 */
public class Main {
    
    /**
     * Método principal.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Usar el look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}