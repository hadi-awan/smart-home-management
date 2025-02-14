package com.github.hadi_awan.smarthome.smart_home_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer temperature = 21;

    private Boolean overridden;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Opening> openings;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Appliance> appliances;

    @ManyToOne
    @JoinColumn(name = "home_id")
    public Home home;

    @ManyToOne
    @JoinColumn(name = "group_id")
    public Group group;

    @OneToMany(mappedBy = "zone")
    @JsonIgnoreProperties({"home", "zone"})
    private List<User> users;

    public Zone() {
        this.name = "Generic Zone";
    }

    public Zone(String name, Home home) {
        this.name = name;
        this.home = home;
    }

    public Zone(String name) {
        this.name = name;
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

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public List<Opening> getOpenings() {
        return openings;
    }

    public void setOpenings(List<Opening> opening) {
        this.openings = opening;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Boolean getOverridden() {
        return overridden;
    }

    public void setOverridden(Boolean overridden) {
        this.overridden = overridden;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
