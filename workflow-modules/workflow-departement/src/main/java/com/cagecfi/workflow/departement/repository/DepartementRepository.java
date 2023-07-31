/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cagecfi.workflow.departement.repository;

import com.cagecfi.workflow.departement.entity.Departement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author GEOFFREY MO
 */
public interface DepartementRepository extends JpaRepository<Departement, Long>{

    Optional<Departement> findByLibelle(String libelle);

    @Query("SELECT d FROM Departement d JOIN FETCH d.niveau ")
    List<Departement> findAllDepFull();

    @Query("SELECT d FROM Departement d JOIN FETCH d.niveau WHERE d.id = :id")
    Optional<Departement> findDepByIdFull(Long id);

}
