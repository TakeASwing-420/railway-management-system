package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JPanel {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JTable resultTable;

    public AdminPanel() {
        setLayout(new BorderLayout());
        
        // Create sidebar
        createSidebar();
        
        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Add initial empty panel
        contentPanel.add(new JPanel(), "EMPTY");
        
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.setBackground(new Color(240, 240, 240));
        
        // Create sidebar buttons
        JButton lastHourButton = createSidebarButton("Query Ticket Sales for Last Hour");
        JButton trainSalesButton = createSidebarButton("Query Ticket Sales for a Train");
        
        // Add buttons to sidebar
        sidebarPanel.add(lastHourButton);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(trainSalesButton);
        
        // Add action listeners
        lastHourButton.addActionListener(e -> showLastHourSales());
        trainSalesButton.addActionListener(e -> showTrainSales());
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setBackground(new Color(240, 240, 240));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));
                button.repaint();
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
                button.repaint();
            }
        });
        
        return button;
    }

    private void showLastHourSales() {
        // Create panel for last hour sales
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table for displaying results
        String[] columns = {"Time", "Train Number", "Train Name", "Tickets Sold", "Revenue"};
        Object[][] data = {}; // Will be populated from API
        resultTable = new JTable(data, columns);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLastHourSales());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        // Show the panel
        contentPanel.add(panel, "LAST_HOUR");
        cardLayout.show(contentPanel, "LAST_HOUR");
        
        // Initial data load
        refreshLastHourSales();
    }

    private void showTrainSales() {
        // Create panel for train sales
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search form
        JPanel searchForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField trainNumberField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTrainSales(trainNumberField.getText()));
        
        searchForm.add(new JLabel("Train Number:"));
        searchForm.add(trainNumberField);
        searchForm.add(searchButton);
        
        // Create table for displaying results
        String[] columns = {"Date", "Tickets Sold", "Revenue"};
        Object[][] data = {}; // Will be populated from API
        resultTable = new JTable(data, columns);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        
        panel.add(searchForm, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Show the panel
        contentPanel.add(panel, "TRAIN_SALES");
        cardLayout.show(contentPanel, "TRAIN_SALES");
    }

    private void refreshLastHourSales() {
        // TODO: Implement API call to get last hour sales
        // This will be connected to your backend service
        JOptionPane.showMessageDialog(this,
            "Refreshing last hour sales data...",
            "Loading",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchTrainSales(String trainNumber) {
        // TODO: Implement API call to get train sales
        // This will be connected to your backend service
        JOptionPane.showMessageDialog(this,
            "Searching sales data for train " + trainNumber,
            "Loading",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 