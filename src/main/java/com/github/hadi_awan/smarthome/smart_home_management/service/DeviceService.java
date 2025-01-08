package com.github.hadi_awan.smarthome.smart_home_management.service;

import com.github.hadi_awan.smarthome.smart_home_management.model.Device;
import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.repository.DeviceRepository;
import com.github.hadi_awan.smarthome.smart_home_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(long id) {
        return deviceRepository.findById(id);
    }

    public List<Device> getDevicesByOwner(User owner) {
        return deviceRepository.findByOwner(owner);
    }

    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long id, Device deviceDetails) {
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));

        // Update device fields
        if (deviceDetails.getName() != null) {
            existingDevice.setName(deviceDetails.getName());
        }
        if (deviceDetails.getType() != null) {
            existingDevice.setType(deviceDetails.getType());
        }

        existingDevice.setStatus(deviceDetails.isStatus());

        if (deviceDetails.getLocation() != null) {
            existingDevice.setLocation(deviceDetails.getLocation());
        }

        // Update owner if a new owner is provided
        if (deviceDetails.getOwner() != null) {
            User newOwner = userRepository.findById(deviceDetails.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingDevice.setOwner(newOwner);
        }

        return deviceRepository.save(existingDevice);
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
        deviceRepository.delete(device);
    }
}
