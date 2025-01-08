package com.github.hadi_awan.smarthome.smart_home_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    @Column(nullable = false)
    private String name;

    @JsonProperty("type")
    @Column(nullable = false)
    private String type;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("location")
    private String location;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name="user_id")
    @JsonProperty("owner")
    private User owner;
}
