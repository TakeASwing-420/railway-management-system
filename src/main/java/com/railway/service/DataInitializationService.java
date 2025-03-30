package com.railway.service;

import com.railway.model.Passenger;
import com.railway.model.Train;
import com.railway.model.PlatformTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataInitializationService {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Value("${app.data.dir}")
    private String dataDirectory;

    private final TrainService trainService;
    private final PlatformTicketService ticketService;

    @Autowired
    public DataInitializationService(TrainService trainService, PlatformTicketService platformTicketService) {
        this.trainService = trainService;
        this.ticketService = platformTicketService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void initializeData() {
        logger.info("Initializing sample data asynchronously...");

        File dataDir = new File(dataDirectory);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            logger.info("Created data directory: {}", dataDirectory);
        }

        createSampleTrainData();
        createSamplePassengerAndTicketData();

        logger.info("Data initialization completed.");
    }

    private void createSampleTrainData() {
        if (!trainService.getAllTrains().isEmpty()) {
            logger.info("Train data already exists. Skipping sample data creation.");
            return;
        }

        List<Train> sampleTrains = new ArrayList<>();

        // Existing trains
        sampleTrains.add(new Train("12345", "Delhi", "Mumbai", "08:00", "16:00", "Rajdhani Express", 500.0, 1200.0, 1800.0, 2500.0));
        sampleTrains.add(new Train("23456", "Chennai", "Bangalore", "09:30", "12:30", "Shatabdi Express", 400.0, 1000.0, 1500.0, 2000.0));

        // Additional Delhi to Mumbai trains
        sampleTrains.add(new Train("12346", "Delhi", "Mumbai", "09:00", "17:00", "Duronto Express", 550.0, 1250.0, 1850.0, 2550.0));
        sampleTrains.add(new Train("12347", "Delhi", "Mumbai", "10:00", "18:00", "Garib Rath", 300.0, 1000.0, 1500.0, 2000.0));
        sampleTrains.add(new Train("12348", "Delhi", "Mumbai", "11:00", "19:00", "Jan Shatabdi", 450.0, 1100.0, 1600.0, 2100.0));
        sampleTrains.add(new Train("12349", "Delhi", "Mumbai", "12:00", "20:00", "Superfast Express", 500.0, 1200.0, 1700.0, 2200.0));
        sampleTrains.add(new Train("12350", "Delhi", "Mumbai", "13:00", "21:00", "Sampark Kranti", 480.0, 1150.0, 1650.0, 2150.0));
        sampleTrains.add(new Train("12351", "Delhi", "Mumbai", "14:00", "22:00", "Intercity Express", 470.0, 1120.0, 1620.0, 2120.0));
        sampleTrains.add(new Train("12352", "Delhi", "Mumbai", "15:00", "23:00", "Express Train", 460.0, 1100.0, 1600.0, 2100.0));
        sampleTrains.add(new Train("12353", "Delhi", "Mumbai", "16:00", "00:00", "Mail Express", 450.0, 1080.0, 1580.0, 2080.0));
        sampleTrains.add(new Train("12354", "Delhi", "Mumbai", "17:00", "01:00", "Double Decker", 340.0, 1050.0, 1550.0, 2050.0));
        sampleTrains.add(new Train("12355", "Delhi", "Mumbai", "18:00", "02:00", "Passenger Train", 330.0, 1020.0, 1520.0, 2020.0));

        trainService.saveAllTrains(sampleTrains);
        logger.info("Created {} sample trains", sampleTrains.size());
    }

    private void createSamplePassengerAndTicketData() {
        if (!ticketService.getAllPlatformTickets().isEmpty()) {
            logger.info("Ticket data already exists. Skipping sample data creation.");
            return;
        }

        List<PlatformTicket> sampleTickets = new ArrayList<>();
        long serialNumberCounter = 1; // Start serial number counter

        // Create passengers and tickets for Train 12345
        Train train1 = trainService.getTrainByNumber("12345").orElse(null);
        if (train1 != null) {
            Passenger passenger1 = new Passenger("Rahul Sharma", 28, "Male", "Confirmed", "Sleeper");
            Passenger passenger2 = new Passenger("Priya Singh", 25, "Female", "Confirmed", "Sleeper");

            PlatformTicket ticket1 = new PlatformTicket(serialNumberCounter++, passenger1, train1, LocalDateTime.now());
            PlatformTicket ticket2 = new PlatformTicket(serialNumberCounter++, passenger2, train1, LocalDateTime.now());

            sampleTickets.add(ticket1);
            sampleTickets.add(ticket2);
        }

        // Create passengers and tickets for Train 23456
        Train train2 = trainService.getTrainByNumber("23456").orElse(null);
        if (train2 != null) {
            Passenger passenger3 = new Passenger("Amit Kumar", 35, "Male", "Confirmed", "AC3");
            Passenger passenger4 = new Passenger("Sneha Patel", 30, "Female", "Confirmed", "AC3");

            PlatformTicket ticket3 = new PlatformTicket(serialNumberCounter++, passenger3, train2, LocalDateTime.now());
            PlatformTicket ticket4 = new PlatformTicket(serialNumberCounter++, passenger4, train2, LocalDateTime.now());

            sampleTickets.add(ticket3);
            sampleTickets.add(ticket4);
        }

        for (PlatformTicket ticket : sampleTickets) {
            ticketService.savePlatformTicket(ticket); // Save tickets
            logger.info("{} saved successfully.", ticket);
        }

        logger.info("Created {} sample tickets", sampleTickets.size());
    }
}