package com.railway.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import com.railway.model.Train;
import com.railway.api.TrainApiHelper;
import com.railway.model.Passenger;
import com.railway.api.PassengerApiHelper;

public class UserPanel extends JPanel {
    private JTextField sourceStationField;
    private JTextField destinationStationField;
    private JTable trainTable;
    private JPanel searchPanel;
    private JPanel ticketPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;

    public UserPanel() {
        setLayout(new BorderLayout());
        
        // Create sidebar
        createSidebar();
        
        // Create main content area with card layout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        // Create panels
        searchPanel = createSearchPanel();
        ticketPanel = createTicketPanel();
        
        // Add panels to card layout
        mainContentPanel.add(searchPanel, "SEARCH");
        mainContentPanel.add(ticketPanel, "TICKET");
        
        // Add components to main panel
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(150, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton bookTicketBtn = new JButton("Book Ticket");
        JButton showTicketBtn = new JButton("Show Ticket");
        
        // Style the buttons
        bookTicketBtn.setMaximumSize(new Dimension(150, 40));
        showTicketBtn.setMaximumSize(new Dimension(150, 40));
        
        // Add action listeners
        bookTicketBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "SEARCH"));
        showTicketBtn.addActionListener(e -> cardLayout.show(mainContentPanel, "TICKET"));
        
        // Add buttons to sidebar
        sidebarPanel.add(bookTicketBtn);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(showTicketBtn);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search form
        JPanel searchForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Source Station
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchForm.add(new JLabel("Source Station:"), gbc);
        
        gbc.gridx = 1;
        sourceStationField = new JTextField(20);
        searchForm.add(sourceStationField, gbc);
        
        // Destination Station
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchForm.add(new JLabel("Destination Station:"), gbc);
        
        gbc.gridx = 1;
        destinationStationField = new JTextField(20);
        searchForm.add(destinationStationField, gbc);
        
        // Search Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton searchButton = new JButton("Search Trains");
        searchButton.addActionListener(e -> searchTrains());
        searchForm.add(searchButton, gbc);
        
        panel.add(searchForm, BorderLayout.NORTH);
        
        // Create table for displaying trains
        String[] columns = {"Train Number", "Train Name", "Departure Time", "Arrival Time", "Sleeper Price", "AC3 Price", "AC2 Price", "AC1 Price"};
        Object[][] data = {}; // Will be populated from API
        trainTable = new JTable(data, columns);
        trainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add right-click context menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem bookTicketItem = new JMenuItem("Book Ticket");
        bookTicketItem.addActionListener(e -> showBookingDialog());
        popupMenu.add(bookTicketItem);
        
        trainTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = trainTable.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        trainTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = trainTable.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        trainTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(trainTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createTicketPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create ticket display area
        JPanel ticketDisplay = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add ticket information labels
        String[] labels = {"Ticket ID:", "Passenger Name:", "Age:",   
                          "Gender:", "Train Number:", "Coach Type:", "Seat Status:"};
        
        // Create arrays to store labels and values
        JLabel[] valueLabels = new JLabel[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            ticketDisplay.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            valueLabels[i] = new JLabel("---");
            ticketDisplay.add(valueLabels[i], gbc);
        }
        
        // Add refresh button
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        JButton refreshButton = new JButton("Refresh Ticket");
        refreshButton.addActionListener(e -> {
            try {
                // Get ticket ID from user
                String ticketIdStr = JOptionPane.showInputDialog(
                    this,
                    "Enter your ticket ID:",
                    "Refresh Ticket",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (ticketIdStr == null || ticketIdStr.trim().isEmpty()) {
                    return; // User cancelled or entered empty string
                }
                
                // Convert ticket ID to Long and fetch passenger details
                Long ticketId = Long.parseLong(ticketIdStr.trim());
                Passenger passenger = PassengerApiHelper.getPassengerDetails(ticketId);
                
                // Update the labels with passenger details
                valueLabels[0].setText(String.valueOf(passenger.getId()));
                valueLabels[1].setText(String.valueOf(passenger.getSerialNumber()));
                valueLabels[2].setText(passenger.getName());
                valueLabels[3].setText(String.valueOf(passenger.getAge()));
                valueLabels[4].setText(passenger.getGender()); 
                valueLabels[5].setText(passenger.getCoachType());
                valueLabels[6].setText(passenger.getSeatStatus());                
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid ticket ID number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error fetching ticket details: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        ticketDisplay.add(refreshButton, gbc);
        
        panel.add(ticketDisplay, BorderLayout.CENTER);
        
        return panel;
    }

    private void searchTrains() {
        try {
            String source = sourceStationField.getText().trim();
            String destination = destinationStationField.getText().trim();
            
            if (source.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Both source and destination stations are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use the API helper to search trains
            List<Train> trains = TrainApiHelper.searchTrains(source, destination);
            updateTrainTable(trains);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error searching trains: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTrainTable(List<Train> trains) {
        String[] columns = {"Train Number", "Train Name", "Departure Time", "Arrival Time", "Sleeper Price", "AC3 Price", "AC2 Price", "AC1 Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        // Populate the model with data
        for (Train train : trains) {
            model.addRow(new Object[]{
                train.getTrainNumber(),
                train.getTrainName(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getSleeperPrice(),
                train.getAc3TierPrice(),
                train.getAc2TierPrice(),
                train.getAc1TierPrice()
            });
        }
        
        trainTable.setModel(model);
        
        // Add custom renderer for row colors
        trainTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                // Get sleeper price from the model (column index 4 is Sleeper Price)
                double sleeperPrice = Double.parseDouble(table.getModel().getValueAt(row, 4).toString());
                
                if (sleeperPrice >= 300 && sleeperPrice < 400) {
                    c.setBackground(Color.decode("#EFE39A"));
                } else if (sleeperPrice >= 400 && sleeperPrice < 500) {
                    c.setBackground(Color.decode("#F5E368"));
                } else {
                    c.setBackground(Color.decode("#F2D620"));
                }
                
                // Set text color to black for better readability
                c.setForeground(Color.BLACK);
                
                return c;
            }
        });
    }

    private void showBookingDialog() {
        // Create custom panel for the dialog
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create text fields
        JTextField ticketCountField = new JTextField(10);
        JTextField usernameField = new JTextField(10);
        JTextField ageField = new JTextField(10);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        JComboBox<String> coachTypeCombo = new JComboBox<>(new String[]{"AC 3 tier", "AC 2 tier", "AC 1 tier", "Sleeper"});
        
        // Get selected train number
        int selectedRow = trainTable.getSelectedRow();
        String trainNumber = trainTable.getValueAt(selectedRow, 0).toString();
        
        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Ticket Count:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(ticketCountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(ageField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(genderCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Coach Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(coachTypeCombo, gbc);
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Book Ticket",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate inputs
                int ticketCount = Integer.parseInt(ticketCountField.getText().trim());
                String username = usernameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = (String) genderCombo.getSelectedItem();
                String coachType = (String) coachTypeCombo.getSelectedItem();
                
                if (username.isEmpty()) {
                    throw new IllegalArgumentException("Username cannot be empty");
                }
                
                // Use the API helper to book ticket
                TrainApiHelper.bookTicket(trainNumber, ticketCount, coachType, username, gender, age);
                
                JOptionPane.showMessageDialog(
                    this,
                    "Ticket booked successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers for ticket count and age",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error booking ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
} 