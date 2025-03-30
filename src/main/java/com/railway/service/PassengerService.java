package com.railway.service;

import com.railway.model.Passenger;
import com.railway.repository.PassengerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private static final Logger logger = LoggerFactory.getLogger(PassengerService.class);

    @Autowired
    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    public Passenger savePassenger(Passenger passenger) {
        logger.info(passenger.toString() + " saved successfully.");
        return passengerRepository.save(passenger);
    }

    public void deletePassenger(Long id) {
        logger.info("PassengerId " + id + " deleted successfully.");
        passengerRepository.deleteById(id);
    }
}