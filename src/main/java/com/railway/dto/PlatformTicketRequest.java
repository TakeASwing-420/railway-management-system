package com.railway.dto;

import com.railway.model.PlatformTicket;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PlatformTicketRequest {
    
    @NotNull
    private PlatformTicket platformTicket;

    @NotBlank
    private String username;

    @NotBlank
    private String gender;

    @NotNull
    private Integer age;

    // Getters and Setters
    public PlatformTicket getPlatformTicket() {
        return platformTicket;
    }

    public void setPlatformTicket(PlatformTicket platformTicket) {
        this.platformTicket = platformTicket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}