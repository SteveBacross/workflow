package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.Utilisateur;
import com.cagecfi.workflow.model.UtilisateurDepartement;
import com.cagecfi.workflow.model.UtilisateurDepartementMod;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.HttpPut;

@Service
public class UtilisateurDepartementHttpClient extends BaseHttpClient {

    @Autowired
    private JsonConvertServiceWf jsonConvertService;

    private static final Logger LOG = Logger.getLogger(UtilisateurDepartementHttpClient.class.getName());

    private String baseURI = "/usrdep";
    private String getUri(){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI;
    }
    private String getUri(String path){
        return this.getServerUri(APIConstants.WORKFLOW_URI + baseURI + "/" + path);
    }

    @SneakyThrows
    public UtilisateurDepartement createUDfailed(UtilisateurDepartement usrDepartement){

        UtilisateurDepartement obj = new UtilisateurDepartement();
        JSONObject object = new JSONObject(usrDepartement);
        HttpPost httpPost = new HttpPost(getUri());
        httpPost.setEntity(new StringEntity(object.toString(), "UTF-8"));
        setAuthorization(httpPost);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            LOG.log(Level.INFO,"Create UtilisateurDepartement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    if(builderIsOkOrCreated(result.optInt("statusCode"))){
                        obj = jsonConvertService.jsonToUsrDep(result.optJSONObject("data").optJSONObject("userDepartment"));
                        obj.setLevel(Boolean.TRUE);
                    }
                    obj.setBackendMessage(result.optString("message"));

                    return obj;
                }
            } else {
                LOG.log(Level.WARNING, "Creste or update UtilisateurDepartement Response : {0}", response);
            }
        } catch (JSONException | IOException | ParseException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;

        /*try (CloseableHttpResponse response = httpClient.execute(httpPost)){
            LOG.log(Level.INFO,"get UtilisateurDepartement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("userDepartment");

                    usrDep = jsonConvertService.jsonToUsrDep(jsonObject);
                    return usrDep;
                }
            } else {
                LOG.log(Level.WARNING, "UtilisateurDepartement Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }
        return null;*/
    }

    public UtilisateurDepartement createUD(UtilisateurDepartement model){
        HttpPost httpPost = new HttpPost(getUri());
        String logTitre = "Create or update";

        return functionPostPut(model, httpPost, null, logTitre);
    }

    @SneakyThrows
    private UtilisateurDepartement functionPostPut(UtilisateurDepartement model, HttpPost httpPost, HttpPut httpPut, String logTitre) {
        UtilisateurDepartement obj = new UtilisateurDepartement();

        JSONObject object = new JSONObject(model);
        CloseableHttpResponse response;
        CloseableHttpClient httpClient;
        if(httpPost != null){
            setMediaType(httpPost);
            setAuthorization(httpPost);
            httpPost.setEntity(new StringEntity(object.toString(),"UTF-8"));
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
        } else {
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
                        obj = jsonConvertService.jsonToUsrDep(result.optJSONObject("data").optJSONObject("userDepartment"));
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
    public List<Utilisateur> getAllUserWithoutDeps(){
        List<Utilisateur> listUtilisateur = new LinkedList<>();
        HttpGet httpGet = new HttpGet(getUri("noDep"));
        setAuthorization(httpGet);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get listUtilsateur Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = result.getJSONObject("data").optJSONArray("userDepartment");
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
    public List<Utilisateur> getAllUsrsByDep(Long id){
        List<Utilisateur> listUtilisateur = new LinkedList<>();
        HttpGet httpGet = new HttpGet(getUri("dep"));
        setAuthorization(httpGet);
        URI uri = new URIBuilder(httpGet.getURI())
                .setParameter("id", id.toString())
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
                    JSONArray jsonArray = result.getJSONObject("data").optJSONArray("userDepartment");
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

}
