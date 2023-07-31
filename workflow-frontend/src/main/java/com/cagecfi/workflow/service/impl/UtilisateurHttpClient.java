/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.model.UtilisateurDepartementMod;
import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class UtilisateurHttpClient extends BaseHttpClient{

    @Autowired
    private JsonConvertServiceWf jsonConvertService;

    private static final Logger LOG = Logger.getLogger(UtilisateurHttpClient.class.getName());

    private String baseURI = "/usrs";
    private String getUri(){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI;
    }
    private String getUri(Long path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI + "/" + path;
    }
    private String getUri(String path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI + "/" + path;
    }

    public UtilisateurRole createUtilisateur(UtilisateurRole model){
        HttpPost httpPost = new HttpPost(getUri());
        String logTitre = "create user";

        return functionPostPut(model, httpPost, null, logTitre);

    }

    public UtilisateurRole updateUtilisateur(UtilisateurRole model){
        HttpPut httpPut = new HttpPut(getUri("edit/" + model.getId()));
        String logTitre = "update user";
        return functionPostPut(model, null, httpPut, logTitre);
    }

    public UtilisateurRole updateUtilisateurProfil(UtilisateurRole model){
        HttpPut httpPut = new HttpPut(getUri("profil/" + model.getId()));
        String logTitre = "update user profil";
        return functionPostPut(model, null, httpPut, logTitre);
    }

    @SneakyThrows
    public UtilisateurRole getUtilisateurRole(Long id){
        UtilisateurRole utilisateurRole = new UtilisateurRole();
        HttpGet httpGet = new HttpGet(getUri(id));
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get UtilisateurRole Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("utilisateurRole");

                    utilisateurRole = jsonConvertService.jsonToUser(jsonObject);
                    return utilisateurRole;
                }
            } else {
                LOG.log(Level.WARNING, "UtilisateurRole Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }
        return null;
    }

    @SneakyThrows
    private UtilisateurRole functionPostPut(UtilisateurRole model, HttpPost httpPost, HttpPut httpPut, String logTitre) {
        UtilisateurRole obj = new UtilisateurRole();

        JSONObject object = new JSONObject(model);
        CloseableHttpResponse response;
        CloseableHttpClient httpClient;
        if(httpPost != null){
            setMediaType(httpPost);
            setAuthorization(httpPost);
            httpPost.setEntity(new StringEntity(object.toString(),"UTF-8"));
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
        }else {
            setMediaType(httpPut);
            setAuthorization(httpPut);
            httpPut.setEntity(new StringEntity(object.toString(),"UTF-8"));
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPut);
        }

        try {
            LOG.log(Level.INFO, logTitre + " Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    if(builderIsOkOrCreated(result.optInt("statusCode"))){
                        obj = jsonConvertService.jsonToUser(result.optJSONObject("data").optJSONObject("Utilisateur"));
                        obj.setLevel(Boolean.TRUE);
                    }
                    obj.setBackendMessage(result.optString("message"));

                    return obj;
                }
            } else {
                LOG.log(Level.WARNING, logTitre + " Response : {0}", response);
            }
        } catch (JSONException | IOException | ParseException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }

    @SneakyThrows
    public Utilisateur getUtilisateurById(Long id){
        Utilisateur usr;
        HttpGet httpGet = new HttpGet(getUri("one"));
        setAuthorization(httpGet);
        URI uri = new URIBuilder(httpGet.getURI())
                .setParameter("id", id.toString())
                .build();
        httpGet.setURI(uri);
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get utilsateur Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("utilisateur");

                    usr = jsonConvertService.jsonToSimpleUser(jsonObject);
                    return usr;
                }
            } else {
                LOG.log(Level.WARNING, "Utilisateur Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }
        return null;
    }

    @SneakyThrows
    public List<Utilisateur> getUserByParams(String nom, String tel, String email){
        List<Utilisateur> listUtilisateur = new LinkedList<>();
        HttpGet httpGet = new HttpGet(getUri("params"));
        setAuthorization(httpGet);
        URI uri = new URIBuilder(httpGet.getURI())
                .setParameter("nom", nom)
                .setParameter("tel", tel)
                .setParameter("email", email)
                .build();
        httpGet.setURI(uri);
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get listUtilsateur Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = result.getJSONObject("data").optJSONArray("utilisateurs");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listUtilisateur.add(jsonConvertService.jsonToSimpleUser((JSONObject) jsonArray.get(i)));
                    }
                    return listUtilisateur;
                }
            } else {
                LOG.log(Level.WARNING, "Utilisateur Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }

        return null;
    }

    @SneakyThrows
    public List<Utilisateur> getAllUserByDeps(){
        List<Utilisateur> listUtilisateur = new LinkedList<>();
        HttpGet httpGet = new HttpGet(getUri("dep"));
        setAuthorization(httpGet);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get listUtilsateur Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = result.getJSONObject("data").optJSONArray("utilisateurs");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listUtilisateur.add(jsonConvertService.jsonToSimpleUser((JSONObject) jsonArray.get(i)));
                    }
                    return listUtilisateur;
                }
            } else {
                LOG.log(Level.WARNING, "Utilisateur Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }

        return null;
    }

    public UtilisateurDepartementMod userDep(){

        return null;

    }

}
