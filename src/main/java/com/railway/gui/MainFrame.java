package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private UserPanel userPanel;
    private AdminPanel adminPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Railway Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Set Windows Task Manager-like theme
        setLookAndFeel();
        
        // Create menu bar
        createMenuBar();
        
        // Create main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        userPanel = new UserPanel();
        adminPanel = new AdminPanel();
        
        // Add panels to card layout
        mainPanel.add(userPanel, "USER");
        mainPanel.add(adminPanel, "ADMIN");
        
        // Set default view to user panel
        cardLayout.show(mainPanel, "USER");
        
        add(mainPanel);
    }

    private void setLookAndFeel() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Customize colors and fonts
            UIManager.put("Panel.background", new Color(240, 240, 240));
            UIManager.put("Panel.foreground", new Color(0, 0, 0));
            UIManager.put("MenuBar.background", new Color(240, 240, 240));
            UIManager.put("MenuBar.foreground", new Color(0, 0, 0));
            UIManager.put("Menu.background", new Color(240, 240, 240));
            UIManager.put("Menu.foreground", new Color(0, 0, 0));
            UIManager.put("MenuItem.background", new Color(240, 240, 240));
            UIManager.put("MenuItem.foreground", new Color(0, 0, 0));
            UIManager.put("MenuItem.selectionBackground", new Color(0, 120, 215));
            UIManager.put("MenuItem.selectionForeground", Color.WHITE);
            
            // Set default font
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
            UIManager.put("Menu.font", defaultFont);
            UIManager.put("MenuItem.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("Button.font", defaultFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setPreferredSize(new Dimension(150, 20));
        menuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuItem.setVerticalAlignment(SwingConstants.TOP);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // User Menu
        JMenu userMenu = new JMenu("User");
        userMenu.add(createMenuItem("User Panel", e -> cardLayout.show(mainPanel, "USER")));
        
        // Admin Menu
        JMenu adminMenu = new JMenu("Admin");
        adminMenu.add(createMenuItem("Admin Panel", e -> cardLayout.show(mainPanel, "ADMIN")));
        
        menuBar.add(userMenu);
        menuBar.add(adminMenu);
        
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
} 