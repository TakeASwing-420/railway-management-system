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
import java.util.ArrayList;
import java.util.List;

/** @implNote I am intentionally hard coding data for the sample data...
    I will be using the API to get the data in the future.
    @author Deep Mondal*/ 

@Service
public class DataInitializationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);
    
    @Value("${app.data.dir}")
    private String dataDirectory;
    
    private final TrainService trainService;
    private final PassengerService passengerService;
    private final PlatformTicketService ticketService;
    
    @Autowired
    public DataInitializationService(TrainService trainService, PassengerService passengerService, PlatformTicketService platformTicketService) {
        this.trainService = trainService;
        this.passengerService = passengerService;
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
        createSamplePassengerData();
        createSampleTicketData();
        
        logger.info("Data initialization completed.");
    }
    
    private void createSamplePassengerData() {
        // Skip creating sample passenger data if we already have passengers
        if (!passengerService.getAllPassengers().isEmpty()) {
            logger.info("Passenger data already exists. Skipping sample data creation.");
            return;
        }
        
        List<Passenger> samplePassengers = new ArrayList<>();
        
        // Only add if train exists
        if (trainService.trainExistsByNumber("12345")) {
            samplePassengers.add(new Passenger(1, "12345", "Rahul Sharma", 28, "Male", "Confirmed", "Sleeper"));
            samplePassengers.add(new Passenger(2, "12345", "Priya Singh", 25, "Female", "Confirmed", "Sleeper"));
        }
        
        if (trainService.trainExistsByNumber("23456")) {
            samplePassengers.add(new Passenger(3, "23456", "Amit Kumar", 35, "Male", "Confirmed", "Sleeper"));
            samplePassengers.add(new Passenger(4, "23456", "Sneha Patel", 30, "Female", "Confirmed", "AC3"));
        }
        
        for (Passenger passenger : samplePassengers) {
            passengerService.savePassenger(passenger);
        }
        
        logger.info("Created {} sample passengers", samplePassengers.size());
    }
    
    private void createSampleTrainData() {
        // Skip creating sample train data if we already have trains
        if (!trainService.getAllTrains().isEmpty()) {
            logger.info("Train data already exists. Skipping sample data creation.");
            return;
        }
        
        List<Train> sampleTrains = new ArrayList<>();
        
        sampleTrains.add(new Train("12345", "Delhi", "Mumbai", "08:00", "16:00", "Rajdhani Express", 500.0, 1200.0, 1800.0, 2500.0));
        sampleTrains.add(new Train("23456", "Chennai", "Bangalore", "09:30", "12:30", "Shatabdi Express", 400.0, 1000.0, 1500.0, 2000.0));
        sampleTrains.add(new Train("34567", "Kolkata", "Delhi", "19:00", "08:00", "Duronto Express", 600.0, 1400.0, 2000.0, 2800.0));
        sampleTrains.add(new Train("45678", "Mumbai", "Goa", "07:15", "15:45", "Jan Shatabdi Express", 350.0, 900.0, 1300.0, 1800.0));
        sampleTrains.add(new Train("56789", "Hyderabad", "Chennai", "13:00", "20:30", "Charminar Express", 450.0, 1100.0, 1600.0, 2200.0));
        sampleTrains.add(new Train("67890", "Ahmedabad", "Jaipur", "06:30", "14:00", "Gujarat Mail", 400.0, 1000.0, 1500.0, 2000.0));
        sampleTrains.add(new Train("78901", "Pune", "Nagpur", "10:00", "18:00", "Vidarbha Express", 450.0, 1100.0, 1600.0, 2200.0));
        sampleTrains.add(new Train("89012", "Lucknow", "Varanasi", "11:00", "19:00", "Ganga Express", 350.0, 900.0, 1300.0, 1800.0));
        sampleTrains.add(new Train("90123", "Bhopal", "Indore", "15:00", "17:30", "Malwa Express", 300.0, 800.0, 1200.0, 1600.0));
        sampleTrains.add(new Train("01234", "Patna", "Ranchi", "14:00", "20:00", "Karnataka Express", 400.0, 1000.0, 1500.0, 2000.0));
        sampleTrains.add(new Train("13579", "Surat", "Vadodara", "12:00", "14:30", "Gujarat Express", 300.0, 800.0, 1200.0, 1600.0));
        sampleTrains.add(new Train("24680", "Delhi", "Mumbai", "16:30", "23:45", "Mumbai Superfast", 350.0, 950.0, 1400.0, 2200.0));
        sampleTrains.add(new Train("35791", "Delhi", "Mumbai", "22:15", "06:30", "Mumbai Night Express", 450.0, 1100.0, 1700.0, 2400.0));
        sampleTrains.add(new Train("46802", "Delhi", "Mumbai", "05:30", "13:45", "Mumbai Morning Express", 380.0, 1000.0, 1500.0, 2300.0));

        trainService.saveAllTrains(sampleTrains);
        logger.info("Created {} sample trains", sampleTrains.size());
    }

    private void createSampleTicketData() {
        // Skip creating sample ticket data if we already have tickets
        if (!ticketService.getAllPlatformTickets().isEmpty()) {
            logger.info("Ticket data already exists. Skipping sample data creation.");
            return;
        }

        List<PlatformTicket> sampleTickets = new ArrayList<>();

        // Create sample tickets only if corresponding train exists
        if (trainService.trainExistsByNumber("12345")) {
            PlatformTicket ticket1 = new PlatformTicket(1, "12345", 2, "Sleeper");
            sampleTickets.add(ticket1);
        }

        if (trainService.trainExistsByNumber("23456")) {
            PlatformTicket ticket2 = new PlatformTicket(2, "23456", 2, "AC3");
            sampleTickets.add(ticket2);
        }

        for (PlatformTicket ticket : sampleTickets) {
            ticketService.savePlatformTicket(ticket);
        }

        logger.info("Created {} sample tickets", sampleTickets.size());
    }
}