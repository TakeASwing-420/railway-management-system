package com.railway.service;

import com.railway.model.PlatformTicket;
import com.railway.model.Train;
import com.railway.repository.PlatformTicketRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlatformTicketService {
    
    private final Long maxCount = 10000L;
    private final PlatformTicketRepository platformTicketRepository;
    private final TrainService trainService;
    private static final Logger logger = LoggerFactory.getLogger(PlatformTicketService.class);

    @Autowired
    public PlatformTicketService(PlatformTicketRepository platformTicketRepository, TrainService trainService) {
        this.platformTicketRepository = platformTicketRepository;
        this.trainService = trainService;
    }

    public List<PlatformTicket> getAllPlatformTickets() {
        return platformTicketRepository.findAll();
    }

    public Optional<PlatformTicket> getPlatformTicketById(Long id) {
        return platformTicketRepository.findById(id);
    }

    public List<PlatformTicket> getPlatformTicketsByTrainNumber(String trainNumber) {
        return platformTicketRepository.findByTrainNumber(trainNumber);
    }

    public List<PlatformTicket> getPlatformTicketsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return platformTicketRepository.findTicketsInTimeRange(startTime, endTime);
    }

    @Transactional
    public PlatformTicket savePlatformTicket(PlatformTicket platformTicket) throws IllegalStateException {
        try {
            // First find or save the train by train number
            Train train = platformTicket.getTrain();
            if (train != null) {
                Optional<Train> existingTrain = trainService.getTrainByNumber(train.getTrainNumber());
                if (existingTrain.isPresent()) {
                    platformTicket.setTrain(existingTrain.get());
                } else {
                    train = trainService.saveTrain(train);
                    platformTicket.setTrain(train);
                }
            }
            
            platformTicket.setSerialNumber(getNextSerialNumber());
            logger.info(platformTicket.toString() + " saved successfully.");
            return platformTicketRepository.save(platformTicket);
        } catch (IllegalStateException e) {
            logger.error("Error generating serial number: " + e.getMessage());
            throw e;
        }
    }

    public void deletePlatformTicket(Long id) {
        logger.info("PlatformTicketId " + id + " deleted successfully.");
        platformTicketRepository.deleteById(id);
    }

    private long getNextSerialNumber() throws IllegalStateException {
        List<PlatformTicket> tickets = platformTicketRepository.findAll();
        if (tickets.isEmpty()) {
            return 1;
        }

        long maxSerialNumber = tickets.stream()
                .mapToLong(PlatformTicket::getSerialNumber)
                .max()
                .orElse(0);

        if (maxSerialNumber >= maxCount) {
            throw new IllegalStateException("Maximum serial number limit reached: " + maxCount);
        }
        return maxSerialNumber + 1;
    }
}