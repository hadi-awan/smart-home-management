package com.github.hadi_awan.smarthome.smart_home_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;

    @JsonProperty("username")
    @Column(unique=true, nullable=false)
    private String username;

    @JsonProperty("password")
    @Column(nullable=false)
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("active")
    private boolean active = true;
}