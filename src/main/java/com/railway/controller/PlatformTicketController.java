package com.railway.controller;

import com.railway.model.PlatformTicket;
import com.railway.service.PlatformTicketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/platform-tickets")
public class PlatformTicketController {

    private final PlatformTicketService platformTicketService;
    private static final Logger logger = LoggerFactory.getLogger(PlatformTicketController.class);

    @Autowired
    public PlatformTicketController(PlatformTicketService platformTicketService) {
        this.platformTicketService = platformTicketService;
    }

    @GetMapping
    public ResponseEntity<List<PlatformTicket>> getAllPlatformTickets() {
        List<PlatformTicket> platformTickets = platformTicketService.getAllPlatformTickets();
        return ResponseEntity.ok(platformTickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformTicket> getPlatformTicketById(@PathVariable Long id) {
        return platformTicketService.getPlatformTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/train/{trainNumber}")
    public ResponseEntity<List<PlatformTicket>> getPlatformTicketsByTrainNumber(@PathVariable String trainNumber) {
        List<PlatformTicket> platformTickets = platformTicketService.getPlatformTicketsByTrainNumber(trainNumber);
        return ResponseEntity.ok(platformTickets);
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<PlatformTicket>> getPlatformTicketsInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<PlatformTicket> platformTickets = platformTicketService.getPlatformTicketsInTimeRange(startTime, endTime);
        return ResponseEntity.ok(platformTickets);
    }

    @PostMapping
    public ResponseEntity<PlatformTicket> createPlatformTicket(@Valid @RequestBody PlatformTicket request) {
        if (request.getIssueTime() == null) {
            request.setIssueTime(LocalDateTime.now());
        }

        PlatformTicket savedPlatformTicket = platformTicketService.savePlatformTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlatformTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatformTicket> updatePlatformTicket(@PathVariable Long id,
            @Valid @RequestBody PlatformTicket platformTicket) {
        if (!platformTicketService.getPlatformTicketById(id).isPresent()) {
            logger.error("PlatformTicketId " + id + " not found.");
            return ResponseEntity.notFound().build();
        }

        platformTicket.setId(id);
        PlatformTicket updatedPlatformTicket = platformTicketService.savePlatformTicket(platformTicket);
        return ResponseEntity.ok(updatedPlatformTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformTicket(@PathVariable Long id) {
        if (!platformTicketService.getPlatformTicketById(id).isPresent()) {
            logger.error("PlatformTicketId " + id + " not found.");
            return ResponseEntity.notFound().build();
        }

        platformTicketService.deletePlatformTicket(id);
        return ResponseEntity.noContent().build();
    }
}