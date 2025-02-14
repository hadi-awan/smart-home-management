package com.github.hadi_awan.smarthome.smart_home_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="openings")
public class Opening {

    private static final int STATE_OPEN = 1;

    private static final int STATE_CLOSED = 0;

    private static final int STATE_LOCKED = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private Integer state = 0;

    @ManyToOne
    @JsonIgnoreProperties("openings")
    private Zone zone;

    private Boolean blocked;

    public Opening() {
        this.type = "door";
    }

    public Opening(String type, Zone zone) {
        this.type = type;
        this.zone = zone;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        // TODO: State Validation - constrain to one of the enums
        this.state = state;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
