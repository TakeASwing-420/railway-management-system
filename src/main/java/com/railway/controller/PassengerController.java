package com.railway.controller;

import com.railway.model.Passenger;
import com.railway.service.PassengerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;
    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        return passengerService.getPassengerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger) {
        Passenger savedPassenger = passengerService.savePassenger(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPassenger);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @Valid @RequestBody Passenger passenger) {
        if (!passengerService.getPassengerById(id).isPresent()) {
            logger.error("PassengerId " + id + " not found.");
            return ResponseEntity.notFound().build();
        }

        passenger.setId(id);
        Passenger updatedPassenger = passengerService.savePassenger(passenger);
        return ResponseEntity.ok(updatedPassenger);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        if (!passengerService.getPassengerById(id).isPresent()) {
            logger.error("PassengerId " + id + " not found.");
            return ResponseEntity.notFound().build();
        }

        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}