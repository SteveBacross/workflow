/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.controller;

import com.cagecfi.exception.RestDefaultException;
import com.cagecfi.utilisateur.controller.BaseUtilisateurController;
import com.cagecfi.utilisateur.entity.BaseUtilisateur;
import com.cagecfi.utilisateur.model.AppUser;
import com.cagecfi.utilisateur.model.BaseUtilisateurRole;
import com.cagecfi.utilisateur.repository.BaseUtilisateurRepository;
import com.cagecfi.utilisateur.service.BaseUtilisateurService;
import com.cagecfi.workflow.departement.entity.Departement;
import com.cagecfi.workflow.departement.model.ResponseAPI;
import com.cagecfi.workflow.departement.repository.DepartementRepository;
import com.cagecfi.workflow.utilisateurs.entity.Utilisateur;
import com.cagecfi.workflow.utilisateurs.entity.UtilisateurDepartement;
import com.cagecfi.workflow.utilisateurs.feign.AppUserRestClient;
import com.cagecfi.workflow.utilisateurs.model.UtilisateurRole;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurDepartementRepository;
import com.cagecfi.workflow.utilisateurs.repository.UtilisateurRepository;
import com.cagecfi.workflow.utilisateurs.service.UtilisateurService;
import com.cagecfi.workflow.utilisateurs.utils.PermissionRole;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.ResponseBuilder;

/**
 *
 * @author GEOFFREY MO
 */
@RestController
//@RequestMapping("/users")
public class UtilisateurController extends BaseUtilisateurController<BaseUtilisateur, BaseUtilisateurRole>{

    @Autowired
    private AppUserRestClient appUserRestClient;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurDepartementRepository usrDepRepository;

    @Autowired
    private DepartementRepository departementRepository;

    private PasswordEncoder passwordEncoder;

    public UtilisateurController(BaseUtilisateurRepository utilisateurRepository, BaseUtilisateurService utilisateurService) {
        super(utilisateurRepository, utilisateurService);
    }

    @Override
    public List getRoles() {
        return PermissionRole.getRoles();
    }

    @Transactional
    @PostMapping("/usrs")
    public ResponseEntity createUser(@RequestBody UtilisateurRole utilisateur){

        Utilisateur usr = utilisateurService.toUtilisateur(utilisateur);
//        BaseUtilisateur usrB = (BaseUtilisateur) utilisateur; 

        if (utilisateurService.isUsernameDoublon(usr)) {
            throw new RestDefaultException("Erreur de Doublon");
        }

        AppUser appUser = new AppUser(null, utilisateur.getUsername(), utilisateur.getPassword());
        appUser.setActif(utilisateur.getActif() != null ? utilisateur.getActif() : Boolean.TRUE);
        appUser.setPwdUpdated(Boolean.TRUE);
        appUser = appUserRestClient.addUser(appUser);
        usr.setIdAppUser(appUser.getId());
        usr.setTitre(utilisateur.getTitre());
        usr = utilisateurRepository.save(usr);

//        UtilisateurDepartement ud = new UtilisateurDepartement();
//        ud.setIdUtilisateur(usr.getId());
//        ud.setIdDepartement(utilisateur.getIdDepartement());
//        usrDepRepository.save(ud);

//        return new ResponseEntity.(utilisateurRepository.save(usr) ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
//        return new ResponseEntity(usr, HttpStatus.CREATED);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Utilisateur", usr);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(201)
                .message("Succes enregistrement utilisateur ...")
                .data(jsonObject)
                .build()
        );
    }

    @Transactional
    @PutMapping("/usrs/profil/{id}")
    public ResponseEntity updateUserFull(@PathVariable("id") Long id,@RequestBody UtilisateurRole utilisateur){

        Optional<Utilisateur> usr = utilisateurRepository.findById(id);
        AppUser appUser1 = new AppUser();
        if (usr.isPresent()) {
            appUser1 = appUserRestClient.getUser(usr.get().getIdAppUser());
        }
        
//        Optional<Utilisateur> usr1 = utilisateurRepository.findById(utilisateur.getId());

        if (utilisateur.getPassword() != null){
            if (passwordEncoder.matches(utilisateur.getPasswordOld(), appUser1.getPassword())){
                appUser1.setPassword(utilisateur.getPassword());
                appUser1 = appUserRestClient.updateUser(id, appUser1);
                Utilisateur u = new Utilisateur();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Utilisateur", u);
                return ResponseEntity.ok(
                            ResponseAPI.builder()
                                    .timeStamp(LocalDateTime.now())
                                    .status(HttpStatus.CREATED)
                                    .statusCode(201)
                                    .message("Success mise a jour Password ")
                                    .data(jsonObject)
                                    .build()
                );
            } else {
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                        .timeStamp(LocalDateTime.now())

                        .message("Password actuel non valide")
                        .build()
                );
            }

        }

        if (utilisateur.getEmail() != null){
            if (usr.isPresent()){
                Utilisateur u = usr.get();
                u.setEmail(utilisateur.getEmail());
                u.setUsername(utilisateur.getUsername());
                u = utilisateurRepository.save(u);
                appUser1.setUsername(utilisateur.getEmail());
                appUser1 = appUserRestClient.updateUser(id, appUser1);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Utilisateur", u);
                return ResponseEntity.ok(
                            ResponseAPI.builder()
                                    .timeStamp(LocalDateTime.now())
                                    .status(HttpStatus.CREATED)
                                    .statusCode(201)
                                    .message("Success mise a jour Email ...")
                                    .data(jsonObject)
                                    .build()
                );
            } else {
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                        .timeStamp(LocalDateTime.now())

                        .message("Email non mise a jour")
                        .build()
                );
            }

        }

        if (utilisateur.getNom()!= null){
            if (usr.isPresent()){
                Utilisateur u = usr.get();
                u.setNom(utilisateur.getNom());
                u = utilisateurRepository.save(u);
               /* appUser1.setUsername(u.getEmail());
                appUser1 = appUserRestClient.updateUser(id, appUser1);*/

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Utilisateur", u);
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(200)
                                .message("Succes mise a jour email utilisateur")
                                .data(jsonObject)
                                .build()
                );
            }
        }

        if (utilisateur.getPrenoms() != null){
            if (usr.isPresent()){
                Utilisateur u = usr.get();
                u.setPrenoms(utilisateur.getPrenoms());
                u = utilisateurRepository.save(u);
//                appUser1.setUsername(u.getUsername());
//                appUser1 = appUserRestClient.updateUser(id, appUser1);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Utilisateur", u);
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(200)
                                .message("Succes mise a jour Prenoms utilisateur")
                                .data(jsonObject)
                                .build()
                );
            }
        }

        if (utilisateur.getTelephone() != null){
            if (usr.isPresent()){
                Utilisateur u = usr.get();
                u.setTelephone(utilisateur.getTelephone());
                u = utilisateurRepository.save(u);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Utilisateur", u);
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                                .timeStamp(LocalDateTime.now())
                                .status(HttpStatus.OK)
                                .statusCode(200)
                                .message("Succes mise a jour Telephone utilisateur")
                                .data(jsonObject)
                                .build()
                );
            }
        }

        /*Utilisateur user = utilisateurService.toUtilisateur(utilisateur);

        if (utilisateurRepository.findById(user.getId()).isPresent()){
            AppUser appUser = new AppUser(null, utilisateur.getUsername(), utilisateur.getPassword());
            appUser.setActif(utilisateur.getActif() != null ? utilisateur.getActif() : Boolean.FALSE);
            appUser = appUserRestClient.addUser(appUser);
            user.setIdAppUser(appUser.getId());

            return new ResponseEntity(utilisateurRepository.save(user), HttpStatus.CREATED);
        }*/

        return null;
    }

    @PutMapping("/usrs/edit/{id}")
    public ResponseEntity editUsr(@PathVariable("id") Long id, @RequestBody UtilisateurRole utilisateur){

        Optional<Utilisateur> usr = utilisateurRepository.findById(id);
        Utilisateur u = new Utilisateur();
        AppUser appUser1 = new AppUser();
        if (usr.isPresent()) {
            appUser1 = appUserRestClient.getUser(usr.get().getIdAppUser());
            u = usr.get();
        }

        if ((utilisateur.getPassword() != null) || (utilisateur.getEmail() != null)) {
            if (utilisateur.getPassword() != null){
                appUser1.setPassword(utilisateur.getPassword());
//            appUser1 = appUserRestClient.updateUser(id, appUser1);
            }
            if(utilisateur.getEmail() != null){
                appUser1.setUsername(utilisateur.getEmail());
                u.setUsername(utilisateur.getEmail());
                u.setEmail(utilisateur.getEmail());
            }
            if(utilisateur.getActif() != null){
                appUser1.setActif(utilisateur.getActif());
            }
            appUser1 = appUserRestClient.updateUser(id, appUser1);
        }

        u.setNom(utilisateur.getNom());
        u.setPrenoms(utilisateur.getPrenoms());
        u.setTelephone(utilisateur.getTelephone());

        u = utilisateurRepository.save(u);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Utilisateur", u);

        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Succes mise a jour utilisateur")
                .data(jsonObject)
                .build()
        );
    }

    @PutMapping("/usrs/{id}")
    public ResponseEntity updateUser(@RequestBody UtilisateurRole utilisateur){

        Utilisateur usr = utilisateurService.toUtilisateur(utilisateur);

        if (utilisateurRepository.findById(usr.getId()).isPresent()){
            AppUser appUser = new AppUser(null, utilisateur.getUsername(), utilisateur.getPassword());
            appUser.setActif(utilisateur.getActif() != null ? utilisateur.getActif() : Boolean.FALSE);
            appUser = appUserRestClient.addUser(appUser);
            usr.setIdAppUser(appUser.getId());

            return new ResponseEntity(utilisateurRepository.save(usr), HttpStatus.CREATED);
        }

        return null;
    }

    @GetMapping("/usrs/{id}")
    public ResponseEntity getUserRole(@PathVariable("id") Long id){
        UtilisateurRole utilisateurRole = new UtilisateurRole();
        Optional<Utilisateur> user = utilisateurRepository.findById(id);

        AppUser appUser;

        if (user.isPresent()){

            Utilisateur ur = user.get();
            appUser = appUserRestClient.getUser(ur.getIdAppUser());

            utilisateurRole.setId(id);
            utilisateurRole.setNom(ur.getNom());
            utilisateurRole.setPrenoms(ur.getPrenoms());
            utilisateurRole.setUsername(ur.getUsername());
            utilisateurRole.setTelephone(ur.getTelephone());
            utilisateurRole.setActif(appUser.getActif());

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("utilisateurRole", utilisateurRole);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Succes Object UtilisateurRole")
                .data(jsonObject)
                .build()
        );
    }

    @GetMapping("/usrs/department")
    public ResponseEntity getDepByUser(@RequestParam("usrId") Long usrId){

        Optional<UtilisateurDepartement> u = usrDepRepository.findByIdUtilisateurFull(usrId);
        JSONObject jsonObject = new JSONObject();
        if (u != null){
            Optional<Departement> d = departementRepository.findDepByIdFull(u.get().getDepartement().getId());
            if (d.isPresent()){
                Departement dep = d.get();
                jsonObject.put("departement", dep) ;
                return ResponseEntity.ok(
                        ResponseAPI.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Succes object Departement")
                        .data(jsonObject)
                        .build()
                );
            }
        }
        return null;
    }

    @GetMapping("/usrs/one")
    public ResponseEntity getUserById(@RequestParam("id") Long id){

        Optional<Utilisateur> user = utilisateurRepository.findById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("utilisateur", user.get());
        if (user.isPresent()){
            return ResponseEntity.ok(
                    ResponseAPI.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .message("Success object Utilisateur")
                    .data(jsonObject)
                    .build()
            );
        }
        return null;
    }

    @GetMapping("/usrs/params")
    public ResponseEntity findUserByParam(@RequestParam(value = "nom", required = false) String nom,
                                          @RequestParam(value = "tel", required = false) String tel,
                                          @RequestParam(value = "email", required = false) String email
                                         ){
        List<Utilisateur> listUser;
        listUser = utilisateurRepository.findUtilisateurByParams(nom, tel, email);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("utilisateurs", listUser);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("succes listes utilisateurs")
                .data(jsonObject)
                .build()
        );
    }

    @GetMapping("/usrs/dep")
    public ResponseEntity findAllUser(){
        List<Utilisateur> listUser;
        listUser = utilisateurRepository.findAll();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("utilisateurs", listUser);
        return ResponseEntity.ok(
                ResponseAPI.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("succes listes utilisateurs")
                        .data(jsonObject)
                        .build()
        );
    }

    private Optional<Utilisateur> getUsrById(Long id){
        Optional<Utilisateur> user = utilisateurRepository.findById(id);
        return user;
    }

    /*ResponseBuilder.okBuilder.setMessage();
    ResponseBuilder.okResponse(ResponseBuilder.okBuilder);*/

//    private Logger logger=LoggerFactory.getLogger(UtilisateurController.class);
//
//
//    @Autowired
//    private AppUserRestClient appUserRestClient;
//
//    /*public UtilisateurController(AppUserRestClient appUserRestClient) {
//        this.appUserRestClient = appUserRestClient;
//    }*/
//
//    @Autowired
//    private UtilisateurRepository utilisateurRepository;
//
////    public UtilisateurController(AppUserRestClient appUserRestClient) {
////        this.appUserRestClient = appUserRestClient;
////    }
//
//    public void controlDoublon(Utilisateur usr){
//        Optional<Utilisateur> u = utilisateurRepository.findByEmail(usr.getEmail());
//        if (u.isPresent()){
//            throw new RestDefaultException("Doublon de Email");
//        }
//    }
//
//    @PostMapping("/obj")
//    @Transactional
//    public Utilisateur addUser(@RequestBody Utilisateur usr) {
//        usr.setId(null);
//        controlDoublon(usr);
//        AppUser user = new AppUser(null, usr.getEmail(), usr.getPassword());
//        user = appUserRestClient.addUser(user);
//        usr.setIdAppUser(user.getId());
//        return utilisateurRepository.save(usr);
//    }
//
//    /*@GetMapping("/{id}")
//    public ResponseEntity<Utilisateur> getUser(@PathVariable("id") Long id){
//
//        return ResponseEntity.ok() utilisateurRepository.findById(id).orElseThrow(()-> new  ());
//    }*/
//
//    @GetMapping("/obj")
//    public List<Utilisateur> listUser(){
//        return utilisateurRepository.findAll();
//    }
//
//    @Transactional
//    @PutMapping("/obj")
//    public Utilisateur updateUser(@RequestBody Utilisateur utilisateur){
//
//       // Utilisateur usr =
//        return utilisateurRepository.save(utilisateur);
//    }
//
//    @DeleteMapping("/obj/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
//        Optional<Utilisateur> usr = utilisateurRepository.findById(id);
//        if (usr.isPresent()){
//            utilisateurRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return null;
//    }

}
