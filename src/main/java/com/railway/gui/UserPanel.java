package com.railway.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import com.railway.model.Train;

public class UserPanel extends JPanel {
    private JTextField sourceStationField;
    private JTextField destinationStationField;
    private JTable trainTable;
    private JPanel searchPanel;
    private JPanel bookingPanel;
    private CardLayout cardLayout;

    public UserPanel() {
        setLayout(new BorderLayout());
        
        // Create card layout for switching between search and booking
        cardLayout = new CardLayout();
        JPanel mainContentPanel = new JPanel(cardLayout);
        
        // Create search panel
        searchPanel = createSearchPanel();
        mainContentPanel.add(searchPanel, "SEARCH");
        
        // Create booking panel
        bookingPanel = createBookingPanel();
        mainContentPanel.add(bookingPanel, "BOOKING");
        
        add(mainContentPanel, BorderLayout.CENTER);
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
        String[] columns = {"Train Number", "Train Name", "Departure Time", "Arrival Time", "Available Seats"};
        Object[][] data = {}; // Will be populated from API
        trainTable = new JTable(data, columns);
        trainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cardLayout.show(getParent(), "BOOKING");
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(trainTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create booking form
        JPanel bookingForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Passenger Details
        String[] labels = {"Name:", "Age:", "Gender:", "Phone:", "Email:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            bookingForm.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            bookingForm.add(fields[i], gbc);
        }
        
        // Back Button
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        JButton backButton = new JButton("Back to Search");
        backButton.addActionListener(e -> cardLayout.show(getParent(), "SEARCH"));
        bookingForm.add(backButton, gbc);
        
        // Book Button
        gbc.gridy = labels.length + 1;
        JButton bookButton = new JButton("Book Ticket");
        bookButton.addActionListener(e -> bookTicket());
        bookingForm.add(bookButton, gbc);
        
        panel.add(bookingForm, BorderLayout.CENTER);
        
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

            // Create HTTP client and request
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            String url = String.format("http://localhost:5000/api/trains/search?fromStation=%s&toStation=%s",
                                     source, destination);
            
            Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

            // Execute the request
            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                // Parse JSON response
                String jsonResponse = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                List<Train> trains = mapper.readValue(jsonResponse, new TypeReference<List<Train>>() {});
                
                // Update the table with results
                updateTrainTable(trains);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error searching trains: " + response.code(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error searching trains: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTrainTable(List<Train> trains) {
        // Create table model with the new data
        String[] columns = {"Train Number", "Train Name", "Departure Time", "Arrival Time", "Sleeper Price", "AC3 Price", "AC2 Price", "AC1 Price"};
        Object[][] data = new Object[trains.size()][columns.length];
        
        for (int i = 0; i < trains.size(); i++) {
            Train train = trains.get(i);
            data[i] = new Object[]{
                train.getTrainNumber(),
                train.getTrainName(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getSleeperPrice(),
                train.getAc3TierPrice(),
                train.getAc2TierPrice(),
                train.getAc1TierPrice()
            };
        }
        
        trainTable.setModel(new DefaultTableModel(data, columns));
    }

    private void bookTicket() {
        // TODO: Implement API call to book ticket
        // This will be connected to your backend service
        JOptionPane.showMessageDialog(this,
            "Ticket booked successfully!",
            "Booking Confirmation",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Return to search panel
        cardLayout.show(getParent(), "SEARCH");
    }
} 