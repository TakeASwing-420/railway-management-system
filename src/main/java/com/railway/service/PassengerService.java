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
    
    public List<Passenger> getPassengersByTrainNumber(String trainNumber) {
        return passengerRepository.findByTrainNumber(trainNumber);
    }
    
    public Passenger savePassenger(Passenger passenger) {
        logger.info(passenger.toString() + " saved successfully.");
        return passengerRepository.save(passenger);
    }
    
    public void deletePassenger(Long id) {
        logger.info("PassengerId " + id +" deleted successfully.");
        passengerRepository.deleteById(id);
    }
    
    public int getNextSerialNumber(String trainNumber) {
        List<Passenger> passengers = passengerRepository.findByTrainNumber(trainNumber);
        if (passengers.isEmpty()) {
            return 1;
        }
        
        int maxSerialNumber = 0;
        for (Passenger passenger : passengers) {
            if (passenger.getSerialNumber() > maxSerialNumber) {
                maxSerialNumber = passenger.getSerialNumber();
            }
        }
        
        return maxSerialNumber + 1;
    }
}