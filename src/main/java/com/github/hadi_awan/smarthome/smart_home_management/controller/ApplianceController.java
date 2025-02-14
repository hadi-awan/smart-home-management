package com.github.hadi_awan.smarthome.smart_home_management.controller;

import com.github.hadi_awan.smarthome.smart_home_management.exception.UnauthorizedActionException;
import com.github.hadi_awan.smarthome.smart_home_management.model.Appliance;
import com.github.hadi_awan.smarthome.smart_home_management.model.Home;
import com.github.hadi_awan.smarthome.smart_home_management.model.User;
import com.github.hadi_awan.smarthome.smart_home_management.model.Zone;
import com.github.hadi_awan.smarthome.smart_home_management.service.ApplianceService;
import com.github.hadi_awan.smarthome.smart_home_management.service.HomeService;
import com.github.hadi_awan.smarthome.smart_home_management.service.ZoneService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApplianceController {

    @Autowired
    ApplianceService applianceService;

    @Autowired
    HomeService homeService;

    @Autowired
    ZoneService zoneService;

    private Map<String, String> response;

    public ApplianceController() {
        this.response = new HashMap<String, String>();
        this.response.put("success", "true");
    }

    /**
     * @param homeId The Home who's appliances should be returned
     * @param zoneId The Zone who's appliances should be returned
     * @return
     */
    @GetMapping("/appliances")
    public List<Appliance> index(@RequestParam(value = "home_id", required = false) Long homeId,
                                 @RequestParam(value = "zone_id", required = false) Long zoneId) {
        if (zoneId != null) {
            return applianceService.findByZone(zoneId);
        }

        if (homeId != null) {
            return applianceService.findByHome(homeId);
        }

        return Collections.emptyList();
    }

    /**
     * Create an appliance by passing in the name, type of appliance, as well as, optionally, the home or zone id it
     * should belong to. If just a zone is passed the setter should resolve the home automatically.
     *
     * @param request JSON request body parsed into a map, can contain: type, name, home_id, zone_id
     * @return JSONObject
     */
    @PostMapping("/appliances/store")
    public JSONObject store(@RequestBody Map<String, String> request) {
        Appliance appliance = new Appliance(request.get("type"), request.get("name"));

        if (request.get("home_id") != null) {
            Long homeId = Long.parseLong(request.get("home_id"));
            Home home = homeService.findById(homeId);
            appliance.setHome(home);
        }

        if (request.get("zone_id") != null) {
            Long zoneId = Long.parseLong(request.get("zone_id"));
            Zone zone = zoneService.findById(zoneId);
            appliance.setZone(zone);
        }

        applianceService.save(appliance);
        return new JSONObject(this.response);
    }

    /**
     * Modify the properties of a given appliance and presist changes to db
     * @param applianceId ID of the appliance in question
     * @param state State to change the appliance to: 0/1 for off/on, or some int to represent a temperature
     * @param name New name to idenify the appliance by
     * @param zoneId ID of the zone to move the appliance too
     * @return JSONObject
     */
    @PostMapping("/appliances/update")
    public JSONObject update(@RequestParam(value = "appliance_id") Long applianceId,
                             @RequestParam(value = "state", required = false) Integer state,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "zone_id", required = false) Long zoneId) {

        User current = new UserController().currentUser();
        if (current != null && (current.getZone().getId().equals(zoneId) || current.getRole().equals(User.ROLE_ADMIN))) {
            this.response.put("success", "true");
            this.response.put("message", "You do not have permission to perform that action");
            return new JSONObject(this.response);
        }

        if(current != null && current.getRole().equals(User.ROLE_CHILD)){
            throw new UnauthorizedActionException();
        }

        Appliance appliance = applianceService.findById(applianceId);

        if (state != null) {
            appliance.setState(state);
        }

        if (name != null) {
            appliance.setName(name);
        }

        if (zoneId != null) {
            Zone zone = zoneService.findById(zoneId);
            appliance.setZone(zone);
        }

        applianceService.save(appliance);
        return new JSONObject(this.response);
    }
}
