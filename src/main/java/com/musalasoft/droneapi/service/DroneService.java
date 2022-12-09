/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musalasoft.droneapi.service;

import com.musalasoft.droneapi.config.AppConstants;
import static com.musalasoft.droneapi.config.AppConstants.DRONE_OVERLOAD;
import com.musalasoft.droneapi.config.CustomMessage;
import com.musalasoft.droneapi.model.Drone;
import com.musalasoft.droneapi.model.Medication;
import com.musalasoft.droneapi.repository.DroneRepository;
import com.musalasoft.droneapi.repository.MedicationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("ServiceDrone")
public class DroneService implements IDroneService {

    @Autowired
    private DroneRepository dronerepo;
    @Autowired
    private MedicationRepository medsrepo;

    @Override
    public ResponseEntity<Drone> registerDrone(Drone d) {
        boolean exists = dronerepo.existsById(d.getId());
        if (exists) {
            return ResponseEntity.badRequest().build();
        } else {
            Drone ld = dronerepo.save(d);
            return new ResponseEntity<>(ld, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<?> loadDrone(Long d, List<Medication> medication) {
        boolean exists = dronerepo.existsById(d);
        if (!exists) {
//            return ResponseEntity.notFound().build();
            CustomMessage errorResponse = new CustomMessage();
            errorResponse.setMessage(AppConstants.DRONE_NOT_FOUND);
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            Optional<Drone> dron = dronerepo.findById(d);
            if(!dron.isPresent())
            { return ResponseEntity.notFound().build();}
            else{
            double medweight = medication.stream().collect(Collectors.summingDouble(Medication::getWeight));
            double curdroneweight = 0;
            if (dron.get().getMedications().size() > 0) {
                curdroneweight = dron.get().getMedications().stream().collect(Collectors.summingDouble(Medication::getWeight));
            }
            if ((dron.get().getWeightlimit() - curdroneweight) < medweight) {
                CustomMessage errorResponse = new CustomMessage();
                errorResponse.setMessage(DRONE_OVERLOAD);
                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            } else {
                medication.forEach(r -> {
                    r.setDrone(dron.get());
                });
                medsrepo.saveAll(medication);
                return new ResponseEntity<Drone>(dronerepo.findById(dron.get().getId()).get(), HttpStatus.OK);
            }}
        }
    }

    @Override
    public ResponseEntity<List<Medication>> listMedicationByDrone(Long d) {
        boolean exists = dronerepo.existsById(d);
        if (!exists) {
            return ResponseEntity.notFound().build();
        } else {
            Optional<Drone> dron = dronerepo.findById(d);
            System.err.println(dron.get().getMedications());
            return new ResponseEntity<>(dron.get().getMedications(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Drone>> available() {
        List<Drone> listAvailable = new ArrayList<>();
        List<Drone> listdrones = dronerepo.findAll();
        listdrones.forEach(d -> {
//            if (null != d.getMedications()) {
            if (d.getMedications().size() > 0) {
                List<Medication> lm = d.getMedications();
                double mm = 0;
                if (lm.size() > 0) {
                    mm = lm.stream().collect(Collectors.summingDouble(Medication::getWeight));
                }
                if (d.getWeightlimit() > mm) {
                    listAvailable.add(d);
                }
            } else {
                listAvailable.add(d);
            }
//            } else {
//                listAvailable.add(d);
//            }
        });
        return new ResponseEntity<>(listAvailable, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getBatteryLevelByDrone(Long id) {
        boolean exists = dronerepo.existsById(id);
        if (!exists) {
            return ResponseEntity.notFound().build();
        } else {
            Optional<Drone> d = dronerepo.findById(id);
            if(!d.isPresent()){
             return ResponseEntity.notFound().build();
            }else
            return new ResponseEntity<>("" + d.get().getBatterycapacity(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<Drone>> initialize() {
        List<Drone> d = new ArrayList<Drone>() {
            {
                add(Drone.builder().batterycapacity(10).weightlimit(5).serialnumber("123").build());
                add(Drone.builder().batterycapacity(20).weightlimit(6).serialnumber("124").build());
                add(Drone.builder().batterycapacity(30).weightlimit(5).serialnumber("125").build());
                add(Drone.builder().batterycapacity(10).weightlimit(6).serialnumber("126").build());
                add(Drone.builder().batterycapacity(17).weightlimit(5).serialnumber("127").build());
                add(Drone.builder().batterycapacity(23).weightlimit(6).serialnumber("128").build());
                add(Drone.builder().batterycapacity(35).weightlimit(5).serialnumber("129").build());
                add(Drone.builder().batterycapacity(19).weightlimit(6).serialnumber("130").build());
                add(Drone.builder().batterycapacity(24).weightlimit(5).serialnumber("131").build());
                add(Drone.builder().batterycapacity(17).weightlimit(6).serialnumber("132").build());
            }
        };
        dronerepo.saveAll(d);
        return new ResponseEntity<>(dronerepo.findAll(), HttpStatus.OK);
    }

    @Override
    public List<Drone> listAll() {
        return dronerepo.findAll();
    }
}
