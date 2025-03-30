package com.railway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "passengers")
@JsonIgnoreProperties({"platformTicket"})
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "passenger")
    private PlatformTicket platformTicket;

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

    // Default constructor
    public Passenger() {}

    // Full constructor
    public Passenger(String name, int age, String gender, String seatStatus, String coachType) {
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

    public PlatformTicket getPlatformTicket() {
        return platformTicket;
    }

    public void setPlatformTicket(PlatformTicket platformTicket) {
        this.platformTicket = platformTicket;
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

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", seatStatus='" + seatStatus + '\'' +
                ", coachType='" + coachType + '\'' +
                '}';
    }
}
