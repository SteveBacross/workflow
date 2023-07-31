/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cagecfi.workflow.demande.repository;

import com.cagecfi.workflow.demande.entity.DetailDemande;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author GEOFFREY MO
 */
public interface DetailDemandeRepository extends JpaRepository<DetailDemande, Long>{

    public Optional<DetailDemande> findByIdDemande(Long id);

    public List<DetailDemande> findAllByIdDemande(Long id);

    public void deleteByIdDemande(Long id);
    
}
