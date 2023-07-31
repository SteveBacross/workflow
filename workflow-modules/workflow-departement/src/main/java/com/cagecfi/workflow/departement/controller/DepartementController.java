/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.departement.controller;


import com.cagecfi.exception.RestDefaultException;
import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.departement.model.ResponseAPI;
import com.cagecfi.workflow.departement.repository.DepartementRepository;

import java.time.LocalDateTime;
import java.util.List;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author GEOFFREY MO
 */
@RestController
@RequestMapping("/departments")
public class DepartementController {

    @Autowired
    private DepartementRepository departementRepository;

    private Logger logger = LoggerFactory.getLogger(DepartementController.class);
    
    @PostMapping
    public ResponseEntity createDep(@RequestBody Departement dep){
        Optional<Departement> departement = departementRepository.findByLibelle(dep.getLibelle());

        if (departement.isPresent()){
            throw new RestDefaultException("Doublon libelle");
        }

        return new ResponseEntity(departementRepository.save(dep), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllDep(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("departments", departementRepository.findAllDepFull());

//        return new ResponseEntity(listDep, HttpStatus.OK);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Succes liste departements")
                    .data(jsonObject)
                    .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity getDepartement(@PathVariable("id") Long id){

        Optional<Departement> dep = departementRepository.findDepByIdFull(id);
        if (dep.isPresent()){
            Departement d = dep.get();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("departement", d);
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Succes find department")
                    .data(jsonObject)
                    .build()
            );
        }
        return null;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteDep(@PathVariable("id") Long id){
        Optional<Departement> d = departementRepository.findById(id);
        if (!departementRepository.existsById(id)){
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(404)
                            .message("Departement non trouvee")
                            .build()
            );
        }

        departementRepository.deleteById(id);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Demande supprime avec succes")
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity updateDep(@RequestBody Departement dep){
        if (departementRepository.findByLibelle(dep.getLibelle()).isPresent()){
            throw new RestDefaultException("Doulon");
        }
        return new ResponseEntity(departementRepository.save(dep), HttpStatus.OK);
    }


}
