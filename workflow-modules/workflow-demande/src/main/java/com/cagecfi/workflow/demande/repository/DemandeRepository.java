/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cagecfi.workflow.demande.repository;

import com.cagecfi.workflow.demande.entity.Demande;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author GEOFFREY MO
 */
public interface DemandeRepository extends JpaRepository<Demande, Long>{

    public List<Demande> findAllByIdUtilisateur(Long id);

    public List<Demande> findAllByIdDepartement(Long idDepartement);


    
    @Query("SELECT d FROM Demande d WHERE d.dateDemande between :dateDebut and :dateFin")
    List<Demande> ListDemandeByDate(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
    
    
}
