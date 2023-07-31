package com.cagecfi.workflow.departement.controller;

import com.cagecfi.exception.RestDefaultException;
import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.departement.entity.Niveau;
import com.cagecfi.workflow.departement.model.ResponseAPI;
import com.cagecfi.workflow.departement.repository.NiveauRepository;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/niveaux")
public class NiveauController {

    @Autowired
    private NiveauRepository niveauRepository;

    private Logger logger = LoggerFactory.getLogger(DepartementController.class);

    @PostMapping
    public ResponseEntity createDep(@RequestBody Niveau niveau){
        Optional<Niveau> niv = niveauRepository.findByLibelle(niveau.getLibelle());

        if (niv.isPresent()){
            throw new RestDefaultException("Doublon libelle");
        }

//        return new ResponseEntity(niveauRepository.save(niveau), HttpStatus.CREATED);
        Niveau n = niveauRepository.save(niveau);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workflow", n);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .data(jsonObject)
                .message("Enregistrement effectué.")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity getAllNiveau(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("niveaux", niveauRepository.findAll());

//        return new ResponseEntity(listDep, HttpStatus.OK);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Succes liste niveaux")
                        .data(jsonObject)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNiveau(@PathVariable("id") Long id){
        if (!niveauRepository.existsById(id)){
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(404)
                            .message("Niveau non trouvee")
                            .build()
            );
        }

        niveauRepository.deleteById(id);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Niveau supprime avec succes")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity updateNiveau(@PathVariable("id") Long id ,@RequestBody Niveau niveau){
        if (niveauRepository.existsById(id)){
//            throw new RestDefaultException("Doulon");
            niveau.setId(id);

            return new ResponseEntity(niveauRepository.save(niveau), HttpStatus.OK);
        }
        return null;
    }
}
