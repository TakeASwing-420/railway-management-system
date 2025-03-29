package com.railway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** @implNote I intentionally avoided using Lombok for this class 
    because I was having issues while deserilization of JSON data from the API
    
    @author Deep Mondal*/ 

@Entity
@Table(name = "platform_tickets")
public class PlatformTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "serial_number")
    private int serialNumber;
    
    @Column(name = "train_number", nullable = false)
    private String trainNumber;
    
    @Column(name = "tickets_count", nullable = false)
    private int ticketsCount;
    
    @Column(name = "coach_type", nullable = false)
    private String coachType;
    
    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;
    
    // Default constructor required by JPA
    public PlatformTicket() {
    }
    
    // Full constructor
    public PlatformTicket(Long id, int serialNumber, String trainNumber, int ticketsCount, 
                         String coachType, LocalDateTime issueTime) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.ticketsCount = ticketsCount;
        this.coachType = coachType;
        this.issueTime = issueTime;
    }
    
    // Constructor without id for easier creation
    public PlatformTicket(int serialNumber, String trainNumber, int ticketsCount, String coachType) {
        this.serialNumber = serialNumber;
        this.trainNumber = trainNumber;
        this.ticketsCount = ticketsCount;
        this.coachType = coachType;
        this.issueTime = LocalDateTime.now();
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
    
    public int getTicketsCount() {
        return ticketsCount;
    }
    
    public void setTicketsCount(int ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
    
    public String getCoachType() {
        return coachType;
    }
    
    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }
    
    public LocalDateTime getIssueTime() {
        return issueTime;
    }
    
    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }
    
    @Override
    public String toString() {
        return "Platform Ticket #" + serialNumber +
               " - Train Number: " + trainNumber +
               ", Tickets Count: " + ticketsCount +
               ", Coach Type: " + coachType +
               ", Issue Time: " + issueTime;
    }
}