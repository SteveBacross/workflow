/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.demande.controller;

import com.cagecfi.workflow.demande.entity.Demande;
import com.cagecfi.workflow.demande.enumeration.Status;
import com.cagecfi.workflow.demande.model.DashBoardStatusData;
import com.cagecfi.workflow.demande.model.ResponseAPI;
import com.cagecfi.workflow.demande.rapport.DemandeRapportEtat;
import com.cagecfi.workflow.demande.repository.DemandeRepository;
import com.cagecfi.workflow.demande.repository.DetailDemandeRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dell
 */
@RestController
public class RapportController {

    private final Logger logger;

    @Autowired
    private DemandeRepository demandeRepository;

    public RapportController() {
        this.logger = LoggerFactory.getLogger(RapportController.class);
    }

    @GetMapping("/demandes/rapport/demandes")
    public ResponseEntity<byte[]> rapportDeamnde(
            @RequestParam(value = "dateDebut")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
            @RequestParam(value = "dateFin")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin
    ) {

        List<Demande> demandes = demandeRepository.ListDemandeByDate(dateDebut, dateFin);

        List<Demande> dmdValide = new LinkedList();
        List<Demande> dmdDenied = new LinkedList();
        List<Demande> dmdProgress = new LinkedList();
        List<Demande> dmdNew = new LinkedList();

        List<JSONObject> taille = new LinkedList<>();
        JSONObject data = new JSONObject();

        int nombreDemande = demandes.size();
        if (demandes.isEmpty()) {
            nombreDemande = 0;
        }
        data.put("tailleTotale", nombreDemande);

        String template = "template/rapportDemande.jrxml";

        Map map = new HashMap();
        map.put("dateDebut", new SimpleDateFormat("dd/MM/yyyy").format(dateDebut));
        map.put("dateFin", new SimpleDateFormat("dd/MM/yyyy").format(dateFin));
        map.put("dateOfDay", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        DemandeRapportEtat etat = new DemandeRapportEtat(map);

        for (Demande demande : demandes) {

            if (demande.getStatus() == Status.Validé) {
                dmdValide.add(demande);
            }
            if (demande.getStatus() == Status.Refusé) {
                dmdDenied.add(demande);
            }
            if (demande.getStatus() == Status.En_Cours) {
                dmdProgress.add(demande);
            }
            if (demande.getStatus() == Status.Nouveau) {
                dmdNew.add(demande);
            }
        }
        int nombreValide = dmdValide.size();
        data.put("tailleValide", nombreValide);

        int nombreRefuse = dmdDenied.size();
        data.put("tailleRefuse", nombreRefuse);

        int nombreProgress = dmdProgress.size();
        data.put("tailleProgress", nombreProgress);

        int nombreNouveau = dmdNew.size();
        data.put("tailleNew", nombreNouveau);

        System.out.println("valide : " + dmdValide.size());
        System.out.println("Refusé : " + dmdDenied.size());
        System.out.println("En cours : " + dmdProgress.size());
        System.out.println("Nouveau : " + dmdNew.size());

        taille.add(data);
        if (taille.isEmpty()) {
            return null;
        } else {
            byte[] bytes = etat.getReport(template, taille);
            return ResponseEntity
                    .ok()
                    // Specify content type as PDF
                    .header("Content-Type", "application/pdf; charset=UTF-8")
                    // Tell browser to display PDF if it can
                    .header("Content-Disposition", "inline; filename=\"" + "Rapport_Demande.pdf\"")
                    .body(bytes);
        }
    }
}
