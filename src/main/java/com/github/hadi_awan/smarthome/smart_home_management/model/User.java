package com.github.hadi_awan.smarthome.smart_home_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String name;

    private String role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    @JsonIgnoreProperties("users")
    private Home home;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    @JsonIgnoreProperties("users")
    private Zone zone;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PARENT = "ROLE_PARENT";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_CHILD = "ROLE_CHILD";
    private static final String ROLE_STRANGER = "ROLE_STRANGER";

    private transient PropertyChangeSupport support;

    public User() {
        this.registerObserver();
    }

    public User(Long id, String name, String email, String password) {
        this.registerObserver();
        this.id = id;
        this.email = email;
        this.username = email;
        this.name = name;
        this.password = password;
        this.role = ROLE_USER;
        this.active = true;
    }

    public User(String name, String email, String password) {
        this.registerObserver();
        this.email = email;
        this.username = email;
        this.name = name;
        this.password = password;
        this.role = ROLE_USER;
        this.active = true;
    }

    public User(String name, String email, String password, String role) {
        this.registerObserver();
        this.email = email;
        this.username = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.active = true;
    }

    public User(User user) {
        this.registerObserver();
        this.email = user.getEmail();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.id = user.getId();
        this.role = user.getRole();
        this.active = true;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.username = email;  // Keep username in sync with email
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Zone getZone() {
        if (zone == null) {
            Zone outside = new Zone("Outside");
            outside.setAppliances(Collections.emptyList());
            outside.setId(Long.valueOf(0));
            return outside;
        }
        return zone;
    }

    public void setZone(Zone zone) {
        this.support.firePropertyChange("zone", this.zone, zone);
        this.zone = zone;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    private void registerObserver() {
        this.support = new PropertyChangeSupport(this);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(this.role));
        return list;
    }
}