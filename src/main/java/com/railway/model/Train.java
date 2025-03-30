package com.railway.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** @implNote I intentionally avoided using Lombok for this class 
    because I was having issues while deserilization of JSON data from the API
    
    @author Deep Mondal*/ 
@Entity
@Table(name = "trains")
@JsonIgnoreProperties({"platformTickets"})
public class Train {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "train", fetch = FetchType.LAZY)
    private List<PlatformTicket> platformTickets;

    @Column(name = "train_number", unique = true, nullable = false)
    private String trainNumber;
    
    @Column(name = "from_station", nullable = false)
    private String fromStation;
    
    @Column(name = "to_station", nullable = false)
    private String toStation;
    
    @Column(name = "departure_time", nullable = false)
    private String departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;
    
    @Column(name = "train_name", nullable = false)
    private String trainName;
    
    @Column(name = "sleeper_price", nullable = false)
    private double sleeperPrice;
    
    @Column(name = "ac_3tier_price", nullable = false)
    private double ac3TierPrice;
    
    @Column(name = "ac_2tier_price", nullable = false)
    private double ac2TierPrice;
    
    @Column(name = "ac_1tier_price", nullable = false)
    private double ac1TierPrice;
    
    // Default constructor required by JPA
    public Train() {
    }
    
    // Full constructor
    public Train(String trainNumber, String fromStation, String toStation, String departureTime, 
                String arrivalTime, String trainName, double sleeperPrice, double ac3TierPrice, 
                double ac2TierPrice, double ac1TierPrice) {
        this.trainNumber = trainNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainName = trainName;
        this.sleeperPrice = sleeperPrice;
        this.ac3TierPrice = ac3TierPrice;
        this.ac2TierPrice = ac2TierPrice;
        this.ac1TierPrice = ac1TierPrice;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTrainNumber() {
        return trainNumber;
    }
    
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }
    
    public String getFromStation() {
        return fromStation;
    }
    
    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }
    
    public String getToStation() {
        return toStation;
    }
    
    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
    
    public String getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public String getTrainName() {
        return trainName;
    }
    
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }
    
    public double getSleeperPrice() {
        return sleeperPrice;
    }
    
    public void setSleeperPrice(double sleeperPrice) {
        this.sleeperPrice = sleeperPrice;
    }
    
    public double getAc3TierPrice() {
        return ac3TierPrice;
    }
    
    public void setAc3TierPrice(double ac3TierPrice) {
        this.ac3TierPrice = ac3TierPrice;
    }
    
    public double getAc2TierPrice() {
        return ac2TierPrice;
    }
    
    public void setAc2TierPrice(double ac2TierPrice) {
        this.ac2TierPrice = ac2TierPrice;
    }
    
    public double getAc1TierPrice() {
        return ac1TierPrice;
    }
    
    public void setAc1TierPrice(double ac1TierPrice) {
        this.ac1TierPrice = ac1TierPrice;
    }
    
    public List<PlatformTicket> getPlatformTickets() {
        return platformTickets;
    }
    
    public void setPlatformTickets(List<PlatformTicket> platformTickets) {
        this.platformTickets = platformTickets;
    }
    
    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", trainNumber='" + trainNumber + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", trainName='" + trainName + '\'' +
                ", sleeperPrice=" + sleeperPrice +
                ", ac3TierPrice=" + ac3TierPrice +
                ", ac2TierPrice=" + ac2TierPrice +
                ", ac1TierPrice=" + ac1TierPrice +
                '}';
    }
}
