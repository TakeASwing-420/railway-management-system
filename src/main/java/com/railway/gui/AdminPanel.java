package com.railway.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.railway.model.PlatformTicket;
import com.railway.api.AdminApiHelper;

public class AdminPanel extends JPanel {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JTable lastHourTable;
    private JTable trainSalesTable;
    private DefaultTableModel lastHourModel;
    private DefaultTableModel trainSalesModel;

    // Add color constants for different coach types
    private static final Color AC1_COLOR = new Color(93, 107, 230);    // Indigo
    private static final Color AC2_COLOR = new Color(116, 147, 232);   // Medium blue
    private static final Color AC3_COLOR = new Color(145, 185, 234);   // Light blue
    private static final Color SLEEPER_COLOR = new Color(187, 214, 237); // Very light blue

    public AdminPanel() {
        setLayout(new BorderLayout());
        
        // Create sidebar
        createSidebar();
        
        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Create panels
        JPanel lastHourPanel = createLastHourPanel();
        JPanel trainSalesPanel = createTrainSalesPanel();
        
        // Add panels to card layout
        contentPanel.add(lastHourPanel, "LAST_HOUR");
        contentPanel.add(trainSalesPanel, "TRAIN_SALES");
        
        // Show last hour panel by default
        cardLayout.show(contentPanel, "LAST_HOUR");
        
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(235, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton lastHourButton = new JButton("Last Hour Report");
        JButton trainSalesButton = new JButton("Sales for Train");
        
        // Style the buttons
        lastHourButton.setMaximumSize(new Dimension(150, 40));
        trainSalesButton.setMaximumSize(new Dimension(150, 40));
        
        // Add action listeners
        lastHourButton.addActionListener(e -> cardLayout.show(contentPanel, "LAST_HOUR"));
        trainSalesButton.addActionListener(e -> cardLayout.show(contentPanel, "TRAIN_SALES"));
        
        // Add buttons to sidebar
        sidebarPanel.add(lastHourButton);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(trainSalesButton);
    }

    private JPanel createLastHourPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table for displaying results
        String[] columns = {"Time", "Train Number", "Train Name", "Passenger Name", "Coach Type"};
        lastHourModel = new DefaultTableModel(columns, 0);
        lastHourTable = new JTable(lastHourModel);
        
        // Add custom renderer for row colors
        lastHourTable.setDefaultRenderer(Object.class, new CoachTypeColorRenderer());
        
        JScrollPane scrollPane = new JScrollPane(lastHourTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLastHourSales());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createTrainSalesPanel() {
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
        String[] columns = {"Time", "Train Number", "Train Name", "Passenger Name", "Coach Type"};
        trainSalesModel = new DefaultTableModel(columns, 0);
        trainSalesTable = new JTable(trainSalesModel);
        
        // Add custom renderer for row colors
        trainSalesTable.setDefaultRenderer(Object.class, new CoachTypeColorRenderer());
        
        JScrollPane scrollPane = new JScrollPane(trainSalesTable);
        
        panel.add(searchForm, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshLastHourSales() {
        try {
            // Clear existing table data
            lastHourModel.setRowCount(0);
            
            // Fetch tickets from last hour
            List<PlatformTicket> tickets = AdminApiHelper.getTicketsInLastHour();
            
            // Format for displaying time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            // Add tickets to table
            for (PlatformTicket ticket : tickets) {
                lastHourModel.addRow(new Object[]{
                    ticket.getIssueTime().format(formatter),
                    ticket.getTrain().getTrainNumber(),
                    ticket.getTrain().getTrainName(),
                    ticket.getPassenger().getName(),
                    ticket.getPassenger().getCoachType()
                });
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error fetching ticket data: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTrainSales(String trainNumber) {
        if (trainNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a train number",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Clear existing table data
            trainSalesModel.setRowCount(0);
            
            // Fetch tickets for the specified train
            List<PlatformTicket> tickets = AdminApiHelper.getTicketsByTrainNumber(trainNumber);
            
            // Format for displaying time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            // Add tickets to table
            for (PlatformTicket ticket : tickets) {
                trainSalesModel.addRow(new Object[]{
                    ticket.getIssueTime().format(formatter),
                    ticket.getTrain().getTrainNumber(),
                    ticket.getTrain().getTrainName(),
                    ticket.getPassenger().getName(),
                    ticket.getPassenger().getCoachType()
                });
            }
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error fetching ticket data: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add custom renderer class
    private class CoachTypeColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {  // Only color when row is not selected
                // Get coach type from the last column (Coach Type)
                String coachType = (String) table.getModel().getValueAt(row, 4);
                
                switch (coachType) {
                    case "AC 1 tier":
                        c.setBackground(AC1_COLOR);
                        break;
                    case "AC 2 tier":
                        c.setBackground(AC2_COLOR);
                        break;
                    case "AC 3 tier":
                        c.setBackground(AC3_COLOR);
                        break;
                    case "Sleeper":
                        c.setBackground(SLEEPER_COLOR);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
                
                // Set text color to white for darker backgrounds
                if (coachType.equals("AC 1 tier") || coachType.equals("AC 2 tier")) {
                    c.setForeground(Color.WHITE);
                } else {
                    c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
} 