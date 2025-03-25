package com.gestionmotos.util;

import com.gestionmotos.repository.UsuarioRepositoryImpl;

/**
 * Utility class to reset the admin password.
 * Run this class directly when you need to reset the admin password.
 */
public class ResetAdminPassword {
    public static void main(String[] args) {
        try {
            System.out.println("Attempting to reset admin password...");
            
            UsuarioRepositoryImpl repo = new UsuarioRepositoryImpl();
            int adminId = 1; // Assuming admin has ID 1
            boolean success = repo.updatePassword(adminId, "admin123");
            
            if (success) {
                System.out.println("✓ Admin password reset successfully to 'admin123'");
                System.out.println("You can now log in with username 'admin' and password 'admin123'");
            } else {
                System.out.println("✗ Failed to reset admin password");
                System.out.println("Check database connection and make sure the admin user with ID 1 exists");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while resetting password:");
            e.printStackTrace();
        }
    }
}