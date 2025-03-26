package com.railway.service;

import com.railway.model.PlatformTicket;
import com.railway.repository.PlatformTicketRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlatformTicketService {
    
    private int maxcount = 1000;
    private final PlatformTicketRepository platformTicketRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlatformTicketService.class);

    @Autowired
    public PlatformTicketService(PlatformTicketRepository platformTicketRepository) {
        this.platformTicketRepository = platformTicketRepository;
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
    
    public PlatformTicket savePlatformTicket(PlatformTicket platformTicket) {
        logger.info(platformTicket.toString() + " saved successfully.");
        return platformTicketRepository.save(platformTicket);
    }
    
    public void deletePlatformTicket(Long id) {
        logger.info("PlatformTicketId " + id +" deleted successfully.");
        platformTicketRepository.deleteById(id);
    }
    
    public int getNextSerialNumber() {
        
        List<PlatformTicket> platformTickets = platformTicketRepository.findAll();
        if (platformTickets.isEmpty()) {
            return 1;
        }
        
        int maxSerialNumber = platformTickets.stream()
                .mapToInt(PlatformTicket::getSerialNumber)
                .max()
                .orElse(0);

        if (maxSerialNumber >= maxcount) {
            return -1;
        } 
        return maxSerialNumber + 1;
    }
}