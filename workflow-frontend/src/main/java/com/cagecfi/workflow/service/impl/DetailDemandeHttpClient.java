/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.DetailDemande;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author dell
 */
@Service
public class DetailDemandeHttpClient extends BaseHttpClient {

    private static final Logger LOG = Logger.getLogger(DetailDemandeHttpClient.class.getName());

    private String getUri(String path) {
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }

    //Liste des details demandes
    public List<DetailDemande> listDetailDemande() throws IOException {
        List<DetailDemande> detailDemandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/detailDemandes/all"));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "list detailsDmd  Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    DetailDemande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDetailDemande(result.getJSONObject(i));
                        detailDemandes.add(dmd);
                    }
                    return detailDemandes;
                }

            } else {
                LOG.log(Level.WARNING, "list detailsDmd Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des detailsdemandes d'une demande
    public List<DetailDemande> detailsByDemande(Long idDemande) throws IOException {
        List<DetailDemande> detailDemandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/detailDemande/detailsByDemande/" + idDemande));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "list detailsDmd  Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    DetailDemande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDetailDemande(result.getJSONObject(i));
                        detailDemandes.add(dmd);
                    }
                    return detailDemandes;
                }

            } else {
                LOG.log(Level.WARNING, "list detailsDmd Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des detailsdemandes approuvées d'une demande
    public List<DetailDemande> getDetailsAcceptByDemande(Long idDemande) throws IOException {
        List<DetailDemande> detailDemandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/detailDemande/detailsAcceptByDemande/" + idDemande));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "list detailsAccept  Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    DetailDemande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDetailDemande(result.getJSONObject(i));
                        detailDemandes.add(dmd);
                    }
                    return detailDemandes;
                }

            } else {
                LOG.log(Level.WARNING, "list detailsAccept Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Enregistrer un detail demande
    @SneakyThrows
    public DetailDemande saveDetailDemande(DetailDemande detailDemande) throws IOException {
        HttpPost post = new HttpPost(getUri("/detailDemande/add"));
        setAuthorization(post);
        setMediaType(post);
        try {
            post.setEntity(new StringEntity(new JSONObject(detailDemande).toString(), "UTF-8"));
        } catch (JSONException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);
        try {
            LOG.log(Level.INFO, "add DetailDemande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));

                    return jsonToDetailDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "add DetailDemande Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }

    //Enregistrer une liste de details demande
    @SneakyThrows
    public List<DetailDemande> saveDetailDemandes(List<DetailDemande> detailDemandes) throws IOException {
        List<DetailDemande> ddmd = new LinkedList<>();
        HttpPost post = new HttpPost(getUri("/detailDemande/addMany"));
        setAuthorization(post);
        setMediaType(post);
        try {
            post.setEntity(new StringEntity(new JSONArray(detailDemandes).toString(), "UTF-8"));
        } catch (JSONException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);
        try {
            LOG.log(Level.INFO, "add DetailDemande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    for(int i=0; i<result.length(); i++){
                        ddmd.add(jsonToDetailDemande((JSONObject)result.get(i)));
                    }
                    return ddmd;
                }
            } else {
                LOG.log(Level.WARNING, "add DetailDemande Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }

    //Valider un detail
    public DetailDemande valideDetail(DetailDemande detailDemande) throws IOException {

        HttpPut put = new HttpPut(getUri("/detailDemande/valideDetail/" + detailDemande.getId()));
        setAuthorization(put);
        setMediaType(put);

        try {
            put.setEntity(new StringEntity(new JSONObject(detailDemande).toString(), "UTF-8"));
        } catch (JSONException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(put);
        try {
            LOG.log(Level.INFO, "UpdateDetailDemande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));

                    return jsonToDetailDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "UpdateDetailDemande Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }

    //Refuser un detail
    public DetailDemande refusDetail(Long id) throws IOException {

        HttpPut put = new HttpPut(getUri("/detailDemande/refusDetail/" + id));
        setAuthorization(put);
        setMediaType(put);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(put);
        try {
            LOG.log(Level.INFO, "UpdateDetailDemande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));

                    return jsonToDetailDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "UpdateDetailDemande Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;
    }

    //JsonToDemande
    public static DetailDemande jsonToDetailDemande(JSONObject result) throws ParseException {
        if (result == null || result.isNull("id")) {
            return null;
        }
        DetailDemande detailDemande = new DetailDemande();

        detailDemande.setId(result.optLong("id"));
        detailDemande.setIdDemande(result.optLong("idDemande"));
        detailDemande.setQuantite(result.optInt("quantite"));
        detailDemande.setDescription(optString(result, "description"));
        detailDemande.setEtat(optString(result, "etat"));
        detailDemande.setLigneBudgetaire(optString(result, "ligneBudgetaire"));
        detailDemande.setEtat(optString(result, "etat"));

        return detailDemande;

    }

}
