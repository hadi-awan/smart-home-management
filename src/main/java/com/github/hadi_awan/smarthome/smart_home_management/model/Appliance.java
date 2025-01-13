package com.github.hadi_awan.smarthome.smart_home_management.model;

import jakarta.persistence.*;

@Entity
@Table(name="appliances")
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private String name;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    public Appliance() {
        this.type = "default Appliance";
        this.name = "Anonymous Appliance";
    }

    public Appliance(String type, String name) {
        this.type = type;
        this.name = name;
        this.state = 0;

    }

    public Appliance(Zone zone, String type) {
        this.zone = zone;
        this.home = zone.home;
        this.type = type;
        this.name = "Default " + type;
        this.state = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public void setZone(Zone zone) {
        this.zone = zone;

        if (this.home == null) {
            this.setHome(zone.home);
        }
    }
}
