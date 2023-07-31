/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.model.*;
import com.github.openjson.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author GEOFFREY MO
 */
@Service
public class JsonConvertServiceWf {
    
    public UtilisateurRole jsonToUser(JSONObject result) throws ParseException{
        UtilisateurRole usr = new UtilisateurRole();
        usr.setId(longParser(result, "id"));
        usr.setNom(stringParser(result, "nom"));
        usr.setPrenoms(stringParser(result, "prenoms"));
        usr.setTelephone(stringParser(result, "telephone"));
        usr.setEmail(stringParser(result, "email"));
        usr.setUsername(stringParser(result, "username"));
        usr.setName(stringParser(result, "name"));
        usr.setActif(booleanParser(result, "actif"));

        return usr;
    }

    public Utilisateur jsonToSimpleUser(JSONObject result) throws ParseException{
        Utilisateur usr = new Utilisateur();
        usr.setId(longParser(result, "id"));
        usr.setNom(stringParser(result, "nom"));
        usr.setPrenoms(stringParser(result, "prenoms"));
        usr.setTelephone(stringParser(result, "telephone"));
        usr.setEmail(stringParser(result, "email"));
        usr.setUsername(stringParser(result, "username"));

        return usr;
    }

    public Departement jsonToDep(JSONObject result) throws ParseException{
        Departement dep = new Departement();
        dep.setId(longParser(result, "id"));
        dep.setLibelle(stringParser(result, "libelle"));

        Niveau niveau = new Niveau();
        JSONObject jsObj = objParser(result, "niveau");
        niveau.setId(jsObj.getLong("id"));
        niveau.setLibelle(jsObj.getString("libelle"));
        niveau.setDescription(jsObj.getString("description"));
        dep.setNiveau(niveau);

        dep.setIdDepartementSup(longParser(result, "idDepartementSup"));

        return dep;
    }

    public Niveau jsonToNiveau(JSONObject result) throws ParseException{
        Niveau niv = new Niveau();
        niv.setId(longParser(result, "id"));
        niv.setLibelle(stringParser(result, "libelle"));
        niv.setDescription(stringParser(result, "description"));

        return niv;
    }

    public UtilisateurDepartement jsonToUsrDep(JSONObject result){
        UtilisateurDepartement ud = new UtilisateurDepartement();
        ud.setId(longParser(result, "id"));

        Departement d = new Departement();
        JSONObject jsonDep = objParser(result, "departement");
        d.setId(jsonDep.getLong("id"));
        d.setLibelle(jsonDep.getString("libelle"));
        ud.setDepartement(d);

        Utilisateur u = new Utilisateur();
        JSONObject jsonUsr = objParser(result, "utilisateur");
        u.setId(jsonUsr.getLong("id"));
        u.setNom(jsonUsr.getString("nom"));
        ud.setUtilisateur(u);
//        ud.setIdDepartement(longParser(result, "idDepartement"));
//        ud.setIdUtilisateur(longParser(result, "idUtilisateur"));

        return ud;
    }

    public DashBoardStatusData jsonToDashBoardStatus(JSONObject result) throws ParseException{
        DashBoardStatusData dsh = new DashBoardStatusData();

        dsh.setNbrDmdNv(longParser(result, "nbrDmdNv"));
        dsh.setPercNbrDmdNv(longParser(result, "percNbrDmdNv"));
        dsh.setNbrDmdEncours(longParser(result, "nbrDmdEncours"));
        dsh.setPercNbrDmdEncours(longParser(result, "percNbrDmdEncours"));
        dsh.setNbrDmdValidee(longParser(result, "nbrDmdValidee"));
        dsh.setPercNbrDmdValidee(longParser(result, "percNbrDmdValidee"));
        dsh.setNbrDmdRefuse(longParser(result, "nbrDmdRefuse"));
        dsh.setPercNbrDmdRefuse(longParser(result, "percNbrDmdRefuse"));
//        dsh.setNbrTotDmd(longParser(result, "nbrTotDmd"));

        return dsh;
    }

    private static JSONObject objParser(JSONObject result, String keyObj){
        if (result.isNull(keyObj)){
            return null;
        }
   
        JSONObject obj = result.getJSONObject(keyObj);
        return obj;

    }

    //result transform
    private static String stringParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optString(key);
    }

    private static Integer integerParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return Integer.parseInt(result.optString(key));
    }

    private static Long longParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optLong(key);
    }

    private static Boolean booleanParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optBoolean(key);
    }

    private static Double doubleParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optDouble(key);
    }

    private static Date dateParser(JSONObject result, String key, String format) throws ParseException {
        if (result.isNull(key)) {
            return null;
        }
        return stringToDate(format, result.optString(key));
    }

    //  dateformat
    private static Date stringToDate(String format, String date) throws ParseException {
        format = format == null ? "yyyy-MM-dd" : format;
//        format = format == null ? "dd-MM-yyyy" : format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }

    public static String dateToString(String format, Date date) throws ParseException {
//        format = format == null ? "dd-MM-yyyy" : format;
        format = format == null ? "yyyy-MM-dd" : format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

}
