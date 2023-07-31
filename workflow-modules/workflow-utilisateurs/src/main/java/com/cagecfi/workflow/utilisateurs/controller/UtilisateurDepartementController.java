/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.controller;

import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.departement.model.ResponseAPI;
import com.cagecfi.workflow.departement.repository.DepartementRepository;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import com.cagecfi.workflow.utilisateurs.entity.UtilisateurDepartement;
import com.cagecfi.workflow.utilisateurs.model.UtilisateurDepartementMod;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurDepartementRepository;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author USER
 */
@RestController
@RequestMapping("/usrdep")
public class UtilisateurDepartementController {

    @Autowired
    private UtilisateurDepartementRepository usrDepRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Transactional
    @PostMapping
    public ResponseEntity saveUsrDep(@RequestBody UtilisateurDepartementMod usrDepMod){

//        Optional<UtilisateurDepartement> ud = usrDepRepository.findByIdUtilisateur(usrDep.getIdUtilisateur());
        List<UtilisateurDepartement> ud = usrDepRepository.findByIdDepartementFull(usrDepMod.getDepartement().getId());
        if (ud != null){
            List<Long> idUdList = new LinkedList<>();

            ud.stream().forEach(s -> idUdList.add(s.getId()));
            usrDepRepository.deleteAllById(idUdList);

            List<Utilisateur> usrList = usrDepMod.getUtilisateurList();
            usrList.stream().forEach(s -> { usrDepRepository
                    .save(new UtilisateurDepartement(usrDepMod.getDepartement(), s));
            });

            /*usrDep.setId(ud.get().getId());
            usrDep = usrDepRepository.save(usrDep);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userDepartment", usrDep);*/
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.CREATED)
                    .statusCode(201)
                    .message("Succes creation utilisateurDepartement")
                    .build()
            );

        }
        return null;

    }

    @GetMapping("/dep/{id}")
    public ResponseEntity getUserDepByDep(@PathVariable("id") Long id){
        List<UtilisateurDepartement> udList = usrDepRepository.findByIdDepartementFull(id);

        List<Utilisateur> usrList = new LinkedList<>();
        for (UtilisateurDepartement ud : udList){
            Utilisateur u = ud.getUtilisateur();
            usrList.add(u);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("utilisateurs", udList);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Succes liste utilisateurs by departement")
                .data(jsonObject)
                .build()
        );
    }

    @Transactional
    @DeleteMapping("/dep/{id}")
    public ResponseEntity deleteAllUsrDepByDep(@PathVariable("id") Long id){
        List<UtilisateurDepartement> udAllList = usrDepRepository.findUdAllFull();
//        List<UtilisateurDepartement> udDep = new LinkedList<>();
        List<Long> idUdList = new LinkedList<>();
        for (UtilisateurDepartement ud : udAllList){
            if (ud.getDepartement().getId().equals(id)){
                idUdList.add(ud.getId());
            }
        }

        if (idUdList != null){
            usrDepRepository.deleteAllById(idUdList);

            return new ResponseEntity("All User of department deleted", HttpStatus.ACCEPTED);
        }

        /*Optional<Departement> d = departementRepository.findById(id);
        if (d.isPresent()){
            usrDepRepository.deleteUDep(d.get());
            return new ResponseEntity("All deleted", HttpStatus.ACCEPTED);
        }*/

        return null;
    }

    @GetMapping("/noDep")
    public ResponseEntity getUserWithoutDep(){
        List<UtilisateurDepartement> udList = usrDepRepository.findUdAllFull();
        List<Utilisateur> uList;
        List<Long> uIdList = new LinkedList<>();
        if(udList != null) {
            /*for (UtilisateurDepartement ud : udList){

                uIdList.add(ud.getUtilisateur().getId());
            }*/

            udList.stream().forEach(s -> uIdList.add(s.getUtilisateur().getId()));

            uList = utilisateurRepository.findAllUsrWithNoDep(uIdList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userDepartment", uList);
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .data(jsonObject)
                    .message("Succes list user with no dep")
                    .build()
            );
        }

        return null;
    }

    @GetMapping("/dep")
    public ResponseEntity findAllUserByDep(@RequestParam("id") Long id){
        List<Utilisateur> uList;
        List<UtilisateurDepartement> udList = usrDepRepository.findByIdDepartementFull(id);
        List<Long> uIdList = new LinkedList<>();
        if (udList != null){
            udList.stream().forEach(s -> uIdList.add(s.getUtilisateur().getId()));

            uList = utilisateurRepository.findAllUsrByDep(uIdList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userDepartment", uList);
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.OK)
                            .statusCode(200)
                            .data(jsonObject)
                            .message("Succes list user with dep")
                            .build()
            );
        }
        return null;

    }

}
