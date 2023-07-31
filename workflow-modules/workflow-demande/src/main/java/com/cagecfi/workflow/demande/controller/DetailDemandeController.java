/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.controller;

import com.cagecfi.workflow.demande.entity.DetailDemande;
import com.cagecfi.workflow.demande.enumeration.Etat;
import com.cagecfi.workflow.demande.repository.DemandeRepository;
import com.cagecfi.workflow.demande.repository.DetailDemandeRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dell
 */
@RestController
public class DetailDemandeController {

    @Autowired
    private DetailDemandeRepository detailDemandeRepository;

    private ResponseEntity notFound = new ResponseEntity("Element non trouvé", HttpStatus.NOT_FOUND);

    //Liste de toutes les details demandes
    @GetMapping(path = "/detailDemandes/all")
    public ResponseEntity<DetailDemande> getAll() {
        List<DetailDemande> detailDemandes = detailDemandeRepository.findAll();
        return new ResponseEntity(detailDemandes, HttpStatus.OK);
    }

    //Recuperation d'un detail demande par id
    @GetMapping(path = "/detailDemande/id")
    public ResponseEntity<DetailDemande> getDetailDemandeById(@PathVariable("id") Long id) {
        Optional<DetailDemande> detailDemande = detailDemandeRepository.findById(id);
        return new ResponseEntity(detailDemande, HttpStatus.OK);
    }

    //Liste des details liés à une demande
    @GetMapping(path = "detailDemande/detailsByDemande/{idDemande}")
    public ResponseEntity<DetailDemande> getDetailsDemandeByIdDemande(@PathVariable("idDemande") Long idDemande) {
        List<DetailDemande> detailDemandes = detailDemandeRepository.findAllByIdDemande(idDemande);
        if (!detailDemandes.isEmpty()) {
            return new ResponseEntity(detailDemandes, HttpStatus.OK);
        }
        return notFound;
    }
    
     //Liste des details appprouvées liés à une demande
    @GetMapping(path = "detailDemande/detailsAcceptByDemande/{idDemande}")
    public ResponseEntity<DetailDemande> getDetailsAcceptByDemande(@PathVariable("idDemande") Long idDemande) {
        List<DetailDemande> detailDemandes = detailDemandeRepository.findAllByIdDemande(idDemande);
         List<DetailDemande> detailsApprouve = new LinkedList();
        if (!detailDemandes.isEmpty()) {
            for(DetailDemande detailDemande : detailDemandes){
                if (detailDemande.getEtat() == Etat.Approuvé){
                    detailsApprouve.add(detailDemande);
                }
            }
            return new ResponseEntity(detailsApprouve, HttpStatus.OK);
        }
        return notFound;
    }

    //Ajout d'un detail
    @PostMapping(path = "/detailDemande/add")
    public ResponseEntity<DetailDemande> addDetailDemande(@RequestBody DetailDemande detailDemande) {

        detailDemandeRepository.save(detailDemande);

        return new ResponseEntity(detailDemandeRepository.save(detailDemande), HttpStatus.CREATED);
    }

    //Ajout d'une liste de detail
    @PostMapping(path = "/detailDemande/addMany")
    @Transactional
    public ResponseEntity<DetailDemande> addDetailDemandeMany(@RequestBody List<DetailDemande> detailDemandes) {
        List<DetailDemande> detailDemandeSaved = new LinkedList();
        if (!detailDemandes.isEmpty()) {
            detailDemandeSaved = detailDemandeRepository.saveAll(detailDemandes);
            return new ResponseEntity(detailDemandeSaved, HttpStatus.CREATED);
        }

        return new ResponseEntity(notFound, HttpStatus.NOT_FOUND);
    }

    //Modification d'un detail
    @PutMapping(path = "/detailDemande/id")
    @Transactional
    public ResponseEntity<DetailDemande> updateDetailDemande(@PathVariable("id") Long id, @RequestBody DetailDemande detailDemande) {

        Optional<DetailDemande> detailDemandeUpdate = detailDemandeRepository.findById(id);
        if (detailDemandeUpdate.isPresent()) {
            detailDemande.setId(detailDemandeUpdate.get().getId());
            detailDemande.setIdDemande(detailDemandeUpdate.get().getIdDemande());

            detailDemandeRepository.save(detailDemande);
            return new ResponseEntity(detailDemande, HttpStatus.OK);
        }
        return notFound;
    }

    //validation detail
    @PutMapping(path = "/detailDemande/valideDetail/{id}")
    @Transactional
    public ResponseEntity<DetailDemande> valideDetailDemande(@PathVariable("id") Long id, @RequestBody DetailDemande detailDemande) {

        Optional<DetailDemande> detailDemandeUpdate = detailDemandeRepository.findById(id);
        if (detailDemandeUpdate.isPresent()) {
            detailDemande.setId(detailDemandeUpdate.get().getId());
            detailDemande.setIdDemande(detailDemandeUpdate.get().getIdDemande());
            detailDemande.setEtat(Etat.Approuvé);
            detailDemandeRepository.save(detailDemande);
            return new ResponseEntity(detailDemande, HttpStatus.OK);
        }
        return notFound;
    }

    //Refus detail
    @PutMapping(path = "/detailDemande/refusDetail/{id}")
    @Transactional
    public ResponseEntity<DetailDemande> refusDetailDemande(@PathVariable("id") Long id) {

        Optional<DetailDemande> detailDemandeUpdate = detailDemandeRepository.findById(id);
        if (detailDemandeUpdate.isPresent()) {
            DetailDemande detailDemande = detailDemandeUpdate.get();
            detailDemande.setId(detailDemandeUpdate.get().getId());
            detailDemande.setEtat(Etat.Rejetté);

            detailDemandeRepository.save(detailDemande);
            return new ResponseEntity(detailDemande, HttpStatus.OK);
        }
        return notFound;
    }

    //Suppression d'un detail
    @DeleteMapping(path = "/detailDemande")
    @Transactional
    public ResponseEntity<DetailDemande> deleteDetailDemande(@PathVariable("id") Long id) {

        Optional<DetailDemande> detailDemandeDelete = detailDemandeRepository.findByIdDemande(id);
        if (detailDemandeDelete.isPresent()) {
            detailDemandeRepository.deleteByIdDemande(id);
        }
        return notFound;
    }
}
