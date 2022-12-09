/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musalasoft.droneapi.rest;

import com.musalasoft.droneapi.model.Drone;
import com.musalasoft.droneapi.model.Medication;
import com.musalasoft.droneapi.repository.DroneRepository;
import com.musalasoft.droneapi.service.IDroneService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user
 */
@RestController
@RequestMapping(value = "/drones", produces = "application/json")
public class RestDrone {

    @Autowired
    private IDroneService servicedrone;
    
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<Drone> Register(@RequestBody Drone d) {
        return servicedrone.registerDrone(d);
    }

    @PostMapping(value = "/load/{id}", consumes = "application/json")
    public ResponseEntity<?> Register(@PathVariable("id") Long id, @RequestBody List<Medication> ld) {
        System.err.println(ld);
        return servicedrone.loadDrone(id, ld);
    }

    @GetMapping("/{id}/batterylevel")
    public ResponseEntity<String> getLevel(@PathVariable("id") Long id) {
        return servicedrone.getBatteryLevelByDrone(id);
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<?> getMedications(@PathVariable("id") Long id) {
        return servicedrone.listMedicationByDrone(id);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Drone>> listAvailable() {
        return servicedrone.available();
    }

}
