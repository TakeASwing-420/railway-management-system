package com.railway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** @implNote I intentionally avoided using Lombok for this class 
    because I was having issues while deserialization of JSON data from the API
    
    @author Deep Mondal*/ 

@Entity
@Table(name = "platform_tickets")
public class PlatformTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", unique = true, nullable = false)
    private long serialNumber;

    @OneToOne(cascade = CascadeType.PERSIST) // Automatically persist Passenger when PlatformTicket is saved
    @JoinColumn(name = "passenger_id", referencedColumnName = "id", unique = true)
    private Passenger passenger;

    @ManyToOne // No cascade here because Train is pre-existing
    @JoinColumn(name = "train_id", referencedColumnName = "id")
    private Train train;

    @Column(name = "issue_time", nullable = false)
    private LocalDateTime issueTime;

    // Default constructor required by JPA
    public PlatformTicket() {}

    // Full constructor
    public PlatformTicket(long serialNumber, Passenger passenger, Train train, LocalDateTime issueTime) {
        this.passenger = passenger;
        this.train = train;
        this.issueTime = issueTime;
        this.serialNumber = serialNumber;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "PlatformTicket{" +
                "id=" + id +
                ", serialNumber=" + serialNumber +
                ", passenger=" + passenger +
                ", train=" + train +
                ", issueTime=" + issueTime +
                '}';
    }
}