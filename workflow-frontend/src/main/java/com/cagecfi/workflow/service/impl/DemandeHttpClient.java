/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.Priorite;
import com.cagecfi.workflow.model.TypeDemande;
import com.cagecfi.workflow.model.Demande;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Nature;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
public class DemandeHttpClient extends BaseHttpClient {

    @Autowired
    private DepartementHttpClient departementHttpClient;

    private static final Logger LOG = Logger.getLogger(DemandeHttpClient.class.getName());

    private String getUri(String path) {
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }

    //Liste de toutes les demandes
    public List<Demande> listAlDemande() throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/all"));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        try {
            LOG.log(Level.INFO, "list Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                    JSONObject embedded = result.optJSONObject("_embedded");
                    if (embedded != null) {
                        JSONArray array = embedded.optJSONArray("demandes");
                        Demande demande;
                        for (int i = 0; i < array.length(); i++) {
                            demande = jsonToDemande(array.getJSONObject(i));
                            demandes.add(demande);
                        }
                    }

                }
            } else {
                LOG.log(Level.WARNING, "list Response : {0}", response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return demandes;
    }

    //Liste des demandes materiel à l'etape intermediaire
    public List<Demande> listDmdMateriel() throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/listDmdMateriel"));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "listDmd intermediaire Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "listDmd intermediaire Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des demandes formation à l'etape intermediaire
    public List<Demande> listDmdFormation() throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/listDmdFormation"));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "listDmd intermediaire Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "listDmd intermediaire Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des demandes à l'etape finale
    public List<Demande> listDmdFinale() throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/listDmdFinale"));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "listDmdFinale Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "listDmdFinale Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des demandes à valider par direction
    public List<Demande> listDemandeDirection(Long idDepartement) throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/directionDemande/new/" + idDepartement));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "listDmdDirection Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "listDmdDirection Response : {0}", response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des nouvelles demandes d'une direction
    public List<Demande> listNewDemandeDirection(Long idUtilisateur) throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/directionDemande/new/" + idUtilisateur));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "listNouvelleDmd Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "listNouvelleDmd Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des  demandes en cours d'un utilisateur
    public List<Demande> listProgressDemandeDirection(Long idUtilisateur) throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/directionDemande/progress/" + idUtilisateur));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "Liste demande en cours Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "Liste demande en cours Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //Liste des demandes d'un utilisateur terminées
    public List<Demande> listFinishDemandeDirection(Long idUtilisateur) throws IOException {
        List<Demande> demandes = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/demandes/directionDemande/finish/" + idUtilisateur));
        setAuthorization(get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            LOG.log(Level.INFO, "Liste demande utilisateur terminé Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONArray result = new JSONArray(EntityUtils.toString(entity, "UTF-8"));
                    Demande dmd;
                    for (int i = 0; i < result.length(); i++) {
                        dmd = jsonToDemande(result.getJSONObject(i));
                        demandes.add(dmd);
                    }
                    return demandes;
                }

            } else {
                LOG.log(Level.WARNING, "Liste demande utilisateur terminé Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;

    }

    //enregistrer une demande
    @SneakyThrows
    public Demande saveDemande(Demande demande) throws IOException {
        HttpPost post = new HttpPost(getUri("/demandes/add"));
        setAuthorization(post);
        setMediaType(post);
        try {
            post.setEntity(new StringEntity(new JSONObject(demande).toString(), "UTF-8"));
        } catch (JSONException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);
        try {
            LOG.log(Level.INFO, "addDemande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));

                    return jsonToDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "addDemande Response : {0}",
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

    //Valider etape
    @SneakyThrows
    public Demande validation(Long id) throws IOException {
        HttpPut put = new HttpPut(getUri("/demandes/validation/" + id));
        setAuthorization(put);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(put);
        try {
            LOG.log(Level.INFO, "validation Demande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));

                    return jsonToDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "validation demande Response : {0}",
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

    //Refuser une demande
    @SneakyThrows
    public Demande refus(Long id) throws IOException {
        HttpPut put = new HttpPut(getUri("/demandes/refus/" + id));
        setAuthorization(put);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(put);
        try {
            LOG.log(Level.INFO, "refus Demande Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));

                    return jsonToDemande(result);
                }
            } else {
                LOG.log(Level.WARNING, "refus demande Response : {0}",
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

    //Supprimer une demande
    public boolean deleteDemande(Long id) throws IOException {
        HttpDelete delete = new HttpDelete(getUri("/demandes/delete/" + id));
        setAuthorization(delete);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(delete);
        try {
            LOG.log(Level.INFO, "DeleteDemande Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                org.apache.http.HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    return true;
                }
            } else {
                LOG.log(Level.WARNING, "DeleteDemande Response : {0}", response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
            return false;
        }
    }

    //JsonToDemande
    public Demande jsonToDemande(JSONObject result) throws ParseException {
        if (result == null || result.isNull("id")) {
            return null;
        }
        Demande demande = new Demande();

        demande.setId(result.optLong("id"));
        demande.setNumeroDemande(optString(result, "numeroDemande"));
        
        if (!result.isNull("typeDemande")) {
        demande.setTypeDemande(TypeDemande.valueOf(optString(result, "typeDemande")));
        }

        demande.setPriorite(Priorite.valueOf(optString(result, "priorite")));
        demande.setNature(Nature.valueOf(optString(result, "nature")));

        demande.setStatus(optString(result, "status"));
        demande.setEtape(optString(result, "etape"));
        demande.setLieu(optString(result, "lieu"));

//        demande.setIdDepartement(result.optLong("idDepartement"));
        demande.setDepartement(new Departement());
        if (!result.isNull("idDepartement")) {
            demande.setDepartement(departementHttpClient.getDepartmentById(result.optLong("idDepartement")));
        }

        demande.setIdUtilisateur(result.optLong("idUtilisateur"));

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        if (optString(result,
                "dateDemande") != null) {
            demande.setDateDemandeValue(sdf2.parse(optString(result, "dateDemande")));
        }

        if (optString(result,
                "dateLivraisonSouhaite") != null) {
            demande.setDateLivraisonSouhaiteValue(sdf2.parse(optString(result, "dateLivraisonSouhaite")));
        }

        if (optString(result,
                "dateMiseAJour") != null) {
            demande.setDateMiseAJourValue(sdf2.parse(optString(result, "dateMiseAJour")));
        }

        return demande;

    }

}
