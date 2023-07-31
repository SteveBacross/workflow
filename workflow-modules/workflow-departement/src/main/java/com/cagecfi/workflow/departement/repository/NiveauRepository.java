package com.cagecfi.workflow.departement.repository;

import com.cagecfi.workflow.departement.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NiveauRepository extends JpaRepository<Niveau, Long> {

    Optional<Niveau> findByLibelle(String libelle);
}
