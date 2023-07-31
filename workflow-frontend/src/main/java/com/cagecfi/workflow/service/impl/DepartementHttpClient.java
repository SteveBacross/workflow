package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.Departement;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.model.UtilisateurRole;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartementHttpClient extends BaseHttpClient {

    @Autowired
    private JsonConvertServiceWf jsonConvertServiceWf;

    private static final Logger LOG = Logger.getLogger(DepartementHttpClient.class.getName());
    private String baseURI = "/departments";

    private String getUri(){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI;
    }
    private String getUri(Long path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI + "/" + path;
    }
    private String getUri(String path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }

    public Departement saveDep(Departement model){

        HttpPost httpPost = new HttpPost(getUri());
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

        String logTitre = "create Department";

//        return functionPostPut(dep, httpPost,null,logTitre);
        return functionPostPuter(model, httpPost,null, logTitre);
    }

    public Departement updateDep(Departement model){
        HttpPut httpPut = new HttpPut(getUri(model.getId()) );
        String logTitre = "update Departement";
        return functionPostPuter(model, null, httpPut, logTitre);
    }

    @SneakyThrows
    public IHMModel deleteDep(Long id){
        IHMModel ihmModel = new IHMModel();
        HttpDelete httpDelete = new HttpDelete(getUri(id) );
        setAuthorization(httpDelete);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try(CloseableHttpResponse response = httpClient.execute(httpDelete)){
            LOG.log(Level.INFO, "Delete Departement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    if (builderIsOkOrCreated(result.optInt("statusCode"))){
                        ihmModel.setLevel(true);
                    }
                    ihmModel.setBackendMessage(result.optString("message"));

                    return ihmModel;
                }
            }else {
                LOG.log(Level.WARNING, "Delete Departement Response: {0}", response);
            }
        }catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }
        return null;
    }

    @SneakyThrows
    public List<Departement> getAllDep(){
        List<Departement> listDep = new LinkedList<>();
        HttpGet get = new HttpGet(getUri());
        setAuthorization(get);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            LOG.log(Level.INFO, "listDepartement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = result.optJSONObject("data").optJSONArray("departments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listDep.add(jsonConvertServiceWf.jsonToDep((JSONObject) jsonArray.get(i)));
                    }
                    return listDep;
                }
            } else {
                LOG.log(Level.WARNING, "listDemande Response : {0}",
                        response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            httpClient.close();
        }

        return null;
    }

    @SneakyThrows
    private Departement functionPostPut(Departement model, HttpPost httpPost, HttpPut httpPut, String logTitre){
        Departement obj = new Departement();

        JSONObject object = new JSONObject(model);
        CloseableHttpResponse response;
        CloseableHttpClient httpClient;

        if (httpPost != null){
            setMediaType(httpPost);
            setAuthorization(httpPost);
            httpPost.setEntity(new StringEntity(object.toString(), "UTF-8"));
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
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    if (builderIsOkOrCreated(result.optInt("statusCode"))) {
                        obj = jsonConvertServiceWf.jsonToDep(result.optJSONObject("data").optJSONObject("workflow"));
                        obj.setLevel(Boolean.TRUE);
                    }
                    obj.setBackendMessage(result.optString("message"));
                    return obj;
                }
            } else {
                LOG.log(Level.WARNING, logTitre + " Response : {0}", response);
            }
        } catch (JSONException | IOException | ParseException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }

        return null;
    }

    @SneakyThrows
    public Departement getOneDepartmentByUser(Long id){
        Departement departement;
        HttpGet httpGet = new HttpGet(getUri("/usrs/department"));
        setAuthorization(httpGet);
        URI uri = new URIBuilder(httpGet.getURI())
                        .setParameter("usrId", id.toString())
                        .build();
        httpGet.setURI(uri);
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get departement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("departement");

                    departement = jsonConvertServiceWf.jsonToDep(jsonObject);
                    return departement;
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
    public Departement getDepartmentById(Long id){
        Departement departement;
        HttpGet httpGet = new HttpGet(getUri(id));
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get departement Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("departement");

                    departement = jsonConvertServiceWf.jsonToDep(jsonObject);
                    return departement;
                }
            } else {
                LOG.log(Level.WARNING, "Departement Respone : {0}", response);
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }
        return null;
    }

}
