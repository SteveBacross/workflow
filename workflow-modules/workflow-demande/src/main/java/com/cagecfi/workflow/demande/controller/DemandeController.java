/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.demande.controller;

import com.cagecfi.workflow.demande.entity.Demande;
import com.cagecfi.workflow.demande.entity.DetailDemande;
import com.cagecfi.workflow.demande.repository.DemandeRepository;
import com.cagecfi.workflow.demande.enumeration.Status;
import com.cagecfi.workflow.demande.enumeration.Etape;
import com.cagecfi.workflow.demande.enumeration.Nature;
import com.cagecfi.workflow.demande.rapport.DemandeRapportEtat;
import com.cagecfi.workflow.demande.repository.DetailDemandeRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 *
 * @author GEOFFREY MO
 */
@RestController
@RequestMapping("/demandes")
public class DemandeController {

    private Logger logger = LoggerFactory.getLogger(DemandeController.class);

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private DetailDemandeRepository detailDemandeRepository;

    private ResponseEntity notFound = new ResponseEntity("Demande non trouvée", HttpStatus.NOT_FOUND);

    //Liste de toutes les  demandes
    @GetMapping(path = "/all")
    public PagedModel<EntityModel<Demande>> get(Pageable pageable,
            PagedResourcesAssembler<Demande> resourcesAssembler) {

        Page<Demande> demandes = demandeRepository.findAll(pageable);
        return resourcesAssembler.toModel(demandes);
    }

    //Liste des nouvelles demandes  pour une direction
    @GetMapping(path = "/directionDemande/new/{idDepartement}")
    public ResponseEntity<Demande> directionNewDemande(@PathVariable("idDepartement") Long idDepartement) {

        List<Demande> demandes = demandeRepository.findAllByIdDepartement(idDepartement);

        List<Demande> dmds = new LinkedList();

        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Responsable) {
                dmds.add(demande);
            }
        }
        return new ResponseEntity(dmds, HttpStatus.OK);
    }

    //Liste des demandes en cours pour une direction
    @GetMapping(path = "/directionDemande/progress/{idDepartement}")
    public ResponseEntity<Demande> directionProgressDemande(@PathVariable("idDepartement") Long idDepartement) {

        List<Demande> demandes = demandeRepository.findAllByIdDepartement(idDepartement);

        List<Demande> dmds = new LinkedList();

        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Intermediaire || demande.getEtape() == Etape.Direction_Generale) {
                dmds.add(demande);
            }
        }
        return new ResponseEntity(dmds, HttpStatus.OK);
    }

    //Liste des demandes terminées pour une direction
    @GetMapping(path = "/directionDemande/finish/{idDepartement}")
    public ResponseEntity<Demande> directionDemandeFinish(@PathVariable("idDepartement") Long idDepartement) {

        List<Demande> demandes = demandeRepository.findAllByIdDepartement(idDepartement);

        List<Demande> dmds = new LinkedList();

        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Fin_de_traitement) {
                dmds.add(demande);
            }
        }
        return new ResponseEntity(dmds, HttpStatus.OK);
    }

    //Recuperation d'un idDemande à partir d'un detail demande
    @GetMapping(path = "/demandeByDetail/{id}")
    public ResponseEntity<Demande> getDemandeByDetail(@PathVariable("id") Long id) {

        Optional<DetailDemande> detailDemandeOpt = detailDemandeRepository.findById(id);

        DetailDemande detailDemande = detailDemandeOpt.get();

        Optional<Demande> demande = demandeRepository.findById(detailDemande.getIdDemande());

        return new ResponseEntity(demande, HttpStatus.OK);
    }

    //Recuperation d'une demande par id
    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable("id") Long id) {
        Optional<Demande> detailDemande = demandeRepository.findById(id);
        return new ResponseEntity(detailDemande, HttpStatus.OK);
    }

    //Ajout d'une demande
    @PostMapping(path = "/add")
    @Transactional
    public ResponseEntity<Demande> addDemande(@RequestBody Demande demande) {

        demande.setStatus(Status.Nouveau);
        demande.setEtape(Etape.Responsable);
        demandeRepository.save(demande);
        return new ResponseEntity(demande, HttpStatus.CREATED);
    }

    //Modification d'une demande
    @PutMapping(path = "/id/")
    @Transactional
    public ResponseEntity<Demande> updateDemande(@PathVariable("id") Long id, @RequestBody Demande demande) {

        Optional<Demande> demandeUpdate = demandeRepository.findById(id);

        if (demandeUpdate.isPresent()) {
            Demande dmd = demandeUpdate.get();
            demande.setId(dmd.getId());
            demande.setNumeroDemande(dmd.getNumeroDemande());
            demande.setTypeDemande(demande.getTypeDemande());
            demande.setDateMiseAJour(new Date());
            demande.setDateLivraisonSouhaite(demande.getDateLivraisonSouhaite());
            demande.setStatus(demande.getStatus());
            demande.setPriorite(demande.getPriorite());
            demande.setLieu(demande.getLieu());
            demande.setTypeDemande(demande.getTypeDemande());

            demandeRepository.save(demande);

            return new ResponseEntity(demande, HttpStatus.OK);
        }
        return notFound;
    }

    //Validation etape
    @PutMapping(path = "/validation/{id}")
    @Transactional
    public ResponseEntity<Demande> validation(@PathVariable("id") Long id) {

        Optional<Demande> demandeUpdate = demandeRepository.findById(id);
        if (demandeUpdate.isPresent()) {
            Demande dmd = demandeUpdate.get();

            //Etape responsable à  etape intermediaire
            switch (demandeUpdate.get().getEtape()) {
                case Responsable:
                    dmd.setId(dmd.getId());
                    dmd.setEtape(Etape.Intermediaire);
                    dmd.setStatus(Status.En_Cours);
                    demandeRepository.save(dmd);
                    break;

                //Etape intermediaire à  etape DIRECTION_GENERALE
                case Intermediaire:
                    dmd.setId(dmd.getId());
                    dmd.setEtape(Etape.Direction_Generale);
                    dmd.setStatus(Status.En_Cours);
                    demandeRepository.save(dmd);
                    break;

                //Etape DIRECTION_GENERALE à  etape FIN_TRAITEMENT
                case Direction_Generale:
                    dmd.setId(dmd.getId());
                    dmd.setEtape(Etape.Fin_de_traitement);
                    dmd.setStatus(Status.Validé);
                    demandeRepository.save(dmd);
                    break;

                default:
                    dmd.setId(dmd.getId());
                    demandeRepository.save(dmd);
                    break;
            }

            return new ResponseEntity(dmd, HttpStatus.OK);
        }
        return notFound;
    }

    //Refuser une demande
    @PutMapping(path = "/refus/{id}")
    @Transactional
    public ResponseEntity<Demande> Deny(@PathVariable("id") Long id) {

        Optional<Demande> demandeUpdate = demandeRepository.findById(id);
        if (demandeUpdate.isPresent()) {
            Demande dmd = demandeUpdate.get();
            dmd.setId(dmd.getId());
            dmd.setStatus(Status.Refusé);
            dmd.setEtape(Etape.Fin_de_traitement);
            demandeRepository.save(dmd);
            return new ResponseEntity(dmd, HttpStatus.OK);
        }
        return notFound;
    }

    //Liste des demandes par direction
    @GetMapping("/listDmdDirection/{idDepartementSup}")
    public ResponseEntity<Demande> listDmDirection(@PathVariable("idDepartement") Long idDepartement) {
        List<Demande> demandes = demandeRepository.findAllByIdDepartement(idDepartement);
        List<Demande> demandeDirection = new LinkedList();
        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Responsable) {
                demandeDirection.add(demande);
            }
        }
        return new ResponseEntity(demandeDirection, HttpStatus.OK);

    }

    //Liste des demanndes à l'etape intermediaire avec nature formation
    @GetMapping("/listDmdFormation")
    public ResponseEntity<Demande> listDmdFormation() {

        List<Demande> demandes = demandeRepository.findAll();
        List<Demande> demandeTmp = new LinkedList();
        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Intermediaire && demande.getNature() == Nature.Formation) {
                demandeTmp.add(demande);
            }
        }
        return new ResponseEntity(demandeTmp, HttpStatus.OK);
    }

    //Liste des demanndes à l'etape intermediaire avec nature materiel
    @GetMapping("/listDmdMateriel")
    public ResponseEntity<Demande> listDmdMateriel() {

        List<Demande> demandes = demandeRepository.findAll();
        List<Demande> demandeTmp = new LinkedList();
        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Intermediaire && demande.getNature() == Nature.Matériel) {
                demandeTmp.add(demande);
            }
        }
        return new ResponseEntity(demandeTmp, HttpStatus.OK);
    }

    //Liste des demandes à l'etape finale
    @GetMapping(path = "/listDmdFinale")
    public ResponseEntity<Demande> listDmdFinale() {

        List<Demande> demandes = demandeRepository.findAll();
        List<Demande> demandeTmp = new LinkedList();

        for (Demande demande : demandes) {
            if (demande.getEtape() == Etape.Direction_Generale) {
                demandeTmp.add(demande);
            }
        }
        return new ResponseEntity(demandeTmp, HttpStatus.OK);
    }

    //Suppression d'une demande
    @DeleteMapping(path = "/delete/{id}")
    @Transactional
    public ResponseEntity<Demande> deleteDemande(@PathVariable("id") Long id) {

        Optional<Demande> demandeDelete = demandeRepository.findById(id);
        if (demandeDelete.isPresent()) {
            demandeRepository.deleteById(id);
            detailDemandeRepository.deleteByIdDemande(id);
            return new ResponseEntity("Demande supprimée", HttpStatus.OK);
        }
        return notFound;
    }

//    @GetMapping("/rapport/demandes")
//    public ResponseEntity<byte[]> rapportDeamnde(
//            @RequestParam(value = "dateDebut")
//            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
//            @RequestParam(value = "dateFin")
//            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin
//    ) {
//
//        List<Demande> demandes = demandeRepository.ListDemandeByDate(dateDebut, dateFin);
//
//        List<Demande> dmdValide = new LinkedList();
//        List<Demande> dmdDenied = new LinkedList();
//        List<Demande> dmdProgress = new LinkedList();
//        List<Demande> dmdNew = new LinkedList();
//
//        List<JSONObject> taille = new LinkedList<>();
//        JSONObject data = new JSONObject();
//
//        int nombreDemande = demandes.size();
//        if (demandes.isEmpty()) {
//            nombreDemande = 0;
//        }
//        data.put("tailleTotale", nombreDemande);
//
//        String template = "template/rapportDemande.jrxml";
//
//        Map map = new HashMap();
//        map.put("dateDebut", new SimpleDateFormat("dd/MM/yyyy").format(dateDebut));
//        map.put("dateFin", new SimpleDateFormat("dd/MM/yyyy").format(dateFin));
//        map.put("dateOfDay", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
//
//        DemandeRapportEtat etat = new DemandeRapportEtat(map);
//
//        for (Demande demande : demandes) {
//
//            if (demande.getStatus() == Status.VALIDE) {
//                dmdValide.add(demande);
//            }
//            if (demande.getStatus() == Status.REFUSE) {
//                dmdDenied.add(demande);
//            }
//            if (demande.getStatus() == Status.EN_COURS) {
//                dmdProgress.add(demande);
//            }
//            if(demande.getStatus() == Status.NOUVEAU){
//                dmdNew.add(demande);
//            }
//        }
//        int nombreValide = dmdValide.size();
//        data.put("tailleValide", nombreValide);
//        
//        int nombreRefuse = dmdDenied.size();
//        data.put("tailleRefuse", nombreRefuse);
//        
//        int nombreProgress = dmdProgress.size();
//        data.put("tailleProgress", nombreProgress);
//        
//        int nombreNouveau = dmdNew.size();
//         data.put("tailleNew", nombreNouveau);
//        
//
//        System.out.println("valide : " + dmdValide.size());
//        System.out.println("Refusé : " + dmdDenied.size());
//        System.out.println("En cours : " + dmdProgress.size());
//         System.out.println("Nouveau : " + dmdProgress.size());
//
//        taille.add(data);
//        if (taille.isEmpty()) {
//            return null;
//        } else {
//            byte[] bytes = etat.getReport(template, taille);
//            return ResponseEntity
//                    .ok()
//                    // Specify content type as PDF
//                    .header("Content-Type", "application/pdf; charset=UTF-8")
//                    // Tell browser to display PDF if it can
//                    .header("Content-Disposition", "inline; filename=\"" + "Rapport_Demande.pdf\"")
//                    .body(bytes);
//        }
//    }

}
