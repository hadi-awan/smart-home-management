package com.github.hadi_awan.smarthome.smart_home_management.controller;

import com.github.hadi_awan.smarthome.smart_home_management.model.Appliance;
import com.github.hadi_awan.smarthome.smart_home_management.model.Group;
import com.github.hadi_awan.smarthome.smart_home_management.model.Opening;
import com.github.hadi_awan.smarthome.smart_home_management.model.Zone;
import com.github.hadi_awan.smarthome.smart_home_management.service.*;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@RestController
public class ZoneController {

    @Autowired
    HomeService homeService;

    @Autowired
    ZoneService zoneService;

    @Autowired
    OpeningService openingService;

    @Autowired
    ApplianceService applianceService;

    @Autowired
    GroupService groupService;

    private Map<String, String> response;

    public ZoneController() {
        this.response = new HashMap<String, String>();
        this.response.put("success", "true");
    }

    /**
     * @param homeId
     * @return
     */
    @GetMapping("/zones")
    public List<Zone> index(@RequestParam(value = "home_id") Long homeId) {
        return zoneService.findByHome(homeId);
    }

    @PostMapping("zone/update")
    public JSONObject update(@RequestParam(value = "zone_id") Long zoneId,
                             @RequestParam(value = "group_id", required = false) Long groupId,
                             @RequestParam(value = "temperature", required = false) Integer temperature,
                             @RequestParam(value = "overridden", required = false) Boolean overridden) {


        Zone zone = zoneService.findById(zoneId);

        if(groupId != null && groupService.findById(groupId) != null){
            Group group = groupService.findById(groupId);
            zone.setGroup(group);
        }

        if(overridden != null){
            zone.setOverridden(overridden);
        }

        if(temperature != null){
            zone.setTemperature(temperature);
        }

        zoneService.save(zone);
        return new JSONObject(this.response);
    }

    /**
     * Accepts a JSON file and converts it to zones and opening
     * @param layout
     * @param homeId
     * @return JSONObject
     */
    @PostMapping("/zones/load")
    public JSONObject load(@RequestParam(value = "layout") MultipartFile layout, @RequestParam(value = "home_id", required = false) Long homeId) {
        if (!layout.getContentType().equals("application/json")) {
            this.response.put("success", "false");
            this.response.put("message", "File Supplied is not a JSON text file. File type supplied: ".concat(layout.getContentType()));
            return new JSONObject(this.response);
        }

        InputStream encoded;
        try {
            encoded = layout.getInputStream();
        } catch (IOException e) {
            this.response.put("success", "false");
            this.response.put("message", "File Store failure.");
            this.response.put("exception", e.toString());
            return new JSONObject(this.response);
        }

        JSONParser loaded = new JSONParser("");
        JSONArray parsed = new JSONArray();
        try {
            parsed = (JSONArray) loaded.parse();
        } catch (ParseException e) {
            this.response.put("success", "false");
            this.response.put("message", "Parsing Failure");
            this.response.put("exception", e.toString());
        }

        // Build zones + openings
        for (Object z : parsed) {
            Zone zone;
            if (homeId != null && homeService.exists(homeId)) {
                zone = new Zone(handleGet(z, "name"), homeService.findById(homeId));
            } else {
                zone = new Zone(handleGet(z, "name"));
            }
            zoneService.save(zone);

            int wCount = Integer.parseInt(handleGet(z, "windows"));
            for (int i = 0; i < wCount; i++) {
                openingService.save(new Opening("window", zone));
            }

            int dCount = Integer.parseInt(handleGet(z, "doors"));
            for (int j = 0; j < dCount; j++) {
                openingService.save(new Opening("door", zone));
            }
            applianceService.save(new Appliance(zone, "light"));
        }

        this.response.put("layout", parsed.toString());
        return new JSONObject(this.response);
    }

    private String handleGet(Object obj, String field) {
        return new JSONObject((HashMap<String, String>) obj).get(field).toString();
    }
}
