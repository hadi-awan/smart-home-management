package com.github.hadi_awan.smarthome.smart_home_management.controller;

import com.github.hadi_awan.smarthome.smart_home_management.model.Device;
import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.service.DeviceService;
import com.github.hadi_awan.smarthome.smart_home_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable long id) {
        return deviceService.getDeviceById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    @GetMapping("/owner/{userId}")
    public List<Device> getDeviceByOwnerId(@PathVariable Long userId) {
        User owner = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return deviceService.getDevicesByOwner(owner);
    }

    @PostMapping
    public Device createDevice(@Valid @RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @PutMapping("/{id}")
    public Device updateDevice(@PathVariable long id, @Valid @RequestBody Device deviceDetails) {
        return deviceService.updateDevice(id, deviceDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable long id) {
        deviceService.deleteDevice(id);
    }
}
