package com.railway.model;

import jakarta.persistence.*;

/** @implNote I intentionally avoided using Lombok for this class 
    because I was having issues while deserilization of JSON data from the API
    
    @author Deep Mondal*/ 

@Entity
@Table(name = "passengers")
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "serial_number",unique = true, nullable = false)
    private int serialNumber;
    
    @Column(name = "train_number", nullable = false)
    private String trainNumber;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "age", nullable = false)
    private int age;
    
    @Column(name = "gender", nullable = false)
    private String gender;
    
    @Column(name = "seat_status", nullable = false)
    private String seatStatus;
    
    @Column(name = "coach_type", nullable = false)
    private String coachType;
        
        // Default constructor required by JPA
        public Passenger() {
        }
        
        // Full constructor
        public Passenger(Long id, int serialNumber, String trainNumber, String name, int age, String gender, String seatStatus, String coachType) {
            this.id = id;
            this.serialNumber = serialNumber;
            this.trainNumber = trainNumber;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.seatStatus = seatStatus;
            this.coachType = coachType;
    }
    
    // Constructor without id for easier creation
    public Passenger(int serialNumber, String trainNumber, String name, int age, String gender, String seatStatus, String coachType) {
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatStatus = seatStatus;
        this.coachType = coachType;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    } 
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getSeatStatus() {
        return seatStatus;
    }
    
    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }
    
    @Override
    public String toString() {
        return "Passenger #" + serialNumber +
               " - Name: " + name +
               ", Age: " + age +
               ", Gender: " + gender +
               ", Seat Status: " + seatStatus +
               ", Coach Type: " + coachType;
    }
}
