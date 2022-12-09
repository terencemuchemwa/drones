/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musalasoft.droneapi.repository;

import com.musalasoft.droneapi.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
//    List<Medication> findByfkdrone(Long fkdrone);
}
