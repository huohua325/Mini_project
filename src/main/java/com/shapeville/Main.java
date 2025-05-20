package com.shapeville;

import javax.swing.*;
import java.util.Locale;
import com.shapeville.gui.UIManager;
import javax.swing.SwingUtilities;

/**
 * The main entry point for the Shapeville Geometry Learning Application.
 * This class initializes the GUI and sets up the system look and feel.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class Main {
    /**
     * The main method that starts the application.
     * It uses SwingUtilities.invokeLater to ensure all GUI operations
     * are performed on the Event Dispatch Thread (EDT).
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set default locale for all UI components
        Locale.setDefault(new Locale("en", "US"));
        JComponent.setDefaultLocale(new Locale("en", "US"));
        javax.swing.UIManager.put("OptionPane.yesButtonText", "Yes");
        javax.swing.UIManager.put("OptionPane.noButtonText", "No");
        javax.swing.UIManager.put("OptionPane.okButtonText", "OK");
        javax.swing.UIManager.put("OptionPane.cancelButtonText", "Cancel");
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Initialize and display GUI
            UIManager.getInstance().initialize();
        });
    }
}