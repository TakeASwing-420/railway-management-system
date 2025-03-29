package com.railway.controller;

import com.railway.model.Train;
import com.railway.service.TrainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;
    private static final Logger logger = LoggerFactory.getLogger(TrainController.class);

    @Autowired
    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }
    
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/{trainNumber}")
    public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
        Optional<Train> train = trainService.getTrainByNumber(trainNumber);
        return train.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchTrains(
            @RequestParam(required = true) String fromStation,
            @RequestParam(required = true) String toStation) {
        try {
            // Input validation
            if (fromStation.trim().isEmpty() || toStation.trim().isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body("Both From Station and To Station must not be empty");
            }

            // Get trains matching the route
            List<Train> trains = trainService.getTrainsByFromStationAndToStation(fromStation, toStation);
            
            // Return empty list if no trains found
            if (trains.isEmpty()) {
                return ResponseEntity
                    .ok()
                    .body(List.of()); // Returns empty list instead of 404 to maintain consistency
            }

            return ResponseEntity.ok(trains);
        } catch (Exception e) {
            logger.error("Error searching trains: " + e.getMessage());
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error occurred while searching trains");
        }
    }
    
    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<Train>> getTrainsByDestination(@PathVariable String destination) {
        List<Train> trains = trainService.getTrainsByDestination(destination);
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/search/destination")
    public ResponseEntity<List<Train>> searchTrainsByDestination(@RequestParam String destination) {
        List<Train> trains = trainService.getTrainsByDestination(destination);
        return ResponseEntity.ok(trains);
    }
    
    @PostMapping
    public ResponseEntity<Train> createTrain(@Valid @RequestBody Train train) {
        if (trainService.trainExistsByNumber(train.getTrainNumber())) {
            logger.error("TrainNumber " + train.getTrainNumber() + " already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Train savedTrain = trainService.saveTrain(train);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrain);
    }
    
    @PutMapping("/{trainNumber}")
    public ResponseEntity<Train> updateTrain(@PathVariable String trainNumber, @Valid @RequestBody Train train) {
        if (!trainService.trainExistsByNumber(trainNumber)) {
            logger.error("TrainNumber " + trainNumber + " not found.");
            return ResponseEntity.notFound().build();
        }
        train.setTrainNumber(trainNumber);
        Train updatedTrain = trainService.saveTrain(train);
        return ResponseEntity.ok(updatedTrain);
    }
    
    @DeleteMapping("/{trainNumber}")
    public ResponseEntity<Void> deleteTrain(@PathVariable String trainNumber) {
        if (!trainService.trainExistsByNumber(trainNumber)) {
            logger.error("TrainNumber " + trainNumber + " not found.");
            return ResponseEntity.notFound().build();
        }
        // Get the train first to know its ID
        Optional<Train> trainOptional = trainService.getTrainByNumber(trainNumber);
        if (trainOptional.isPresent()) {
            trainService.deleteTrain(trainOptional.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}