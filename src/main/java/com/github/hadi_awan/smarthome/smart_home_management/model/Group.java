package com.github.hadi_awan.smarthome.smart_home_management.model;

import jakarta.persistence.*;

@Entity
@Table(name="home_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // Default temperature
    private Integer temperature;

    private Integer temperature_winter;

    private Integer temperature_summer;

    // If true then heating, if false then cooling
    private boolean heating;

    @ManyToOne
    @JoinColumn(name = "home_id")
    public Home home;

    public Group(){
        this.name = "default_group";
        this.temperature = 20;
        this.temperature_winter = 21;
        this.temperature_summer = 18;
        this.heating = true;
    }

    public Group(String name, Home home, int temperature, int temperatureWinter, int temperatureSummer, boolean heating){
        this.name = name;
        this.home = home;
        this.temperature = temperature;
        this.temperature_winter = temperatureWinter;
        this.temperature_summer = temperatureSummer;
        this.heating = heating;
    }

    public Group(String name, int temperature, int temperatureWinter, int temperatureSummer, boolean heating, Home home){
        this.name = name;
        this.temperature = temperature;
        this.temperature_winter = temperatureWinter;
        this.temperature_summer = temperatureSummer;
        this.heating = heating;
        this.home = home;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperatureWinter() {
        return temperature_winter;
    }

    public void setTemperatureWinter(int temperatureWinter) {
        this.temperature_winter = temperatureWinter;
    }

    public int getTemperatureSummer() {
        return temperature_summer;
    }

    public void setTemperatureSummer(int temperatureSummer) {
        this.temperature_summer = temperatureSummer;
    }

    public boolean getHeating() {
        return heating;
    }

    public void setHeating(boolean heating) {
        this.heating = heating;
    }

    public Home getHome(){
        return this.home;
    }

    public void setHome(Home home){
        this.home = home;
    }
}
