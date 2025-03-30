package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;
import com.railway.model.PlatformTicket;
import com.railway.api.AdminApiHelper;

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
        sidebarPanel.setPreferredSize(new Dimension(150, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        
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
        try {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusHours(1);
            
            List<PlatformTicket> tickets = AdminApiHelper.getTicketsInTimeRange(startTime, endTime);
            displayTickets(tickets);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error fetching last hour sales: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTrainSales() {
        try {
            String trainNumber = JOptionPane.showInputDialog(
                this,
                "Enter Train Number:",
                "Train Sales Query",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (trainNumber != null && !trainNumber.trim().isEmpty()) {
                List<PlatformTicket> tickets = AdminApiHelper.getTicketsByTrainNumber(trainNumber.trim());
                displayTickets(tickets);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error fetching train sales: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTickets(List<PlatformTicket> tickets) {
        String[] columns = {"Ticket ID", "Passenger Name", "Train Number", "Issue Time", "Coach Type", "Seat Status"};
        Object[][] data = new Object[tickets.size()][columns.length];
        
        for (int i = 0; i < tickets.size(); i++) {
            PlatformTicket ticket = tickets.get(i);
            data[i] = new Object[]{
                ticket.getId(),
                ticket.getPassenger().getName(),
                ticket.getTrain().getTrainNumber(),
                ticket.getIssueTime(),
                ticket.getPassenger().getCoachType(),
                ticket.getPassenger().getSeatStatus()
            };
        }
        
        resultTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        
        // Create a new panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Show the table panel
        contentPanel.removeAll();
        contentPanel.add(tablePanel, "RESULTS");
        cardLayout.show(contentPanel, "RESULTS");
    }
} 