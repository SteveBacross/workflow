package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.model.Niveau;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NiveauHttpClient extends BaseHttpClient {

    @Autowired
    private JsonConvertServiceWf jsonConvertService;

    private static final Logger LOG = Logger.getLogger(NiveauHttpClient.class.getName());
    private String baseURI = "/niveaux";

    private String getUri(){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI;
    }
    private String getUri(Long path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI + "/" + path;
    }
    private String getUri(String path){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }

    public Niveau saveNiveau(Niveau model){

        HttpPost httpPost = new HttpPost(getUri());
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

        String logTitre = "create Niveau";

        return functionPostPut(model, httpPost,null, logTitre);
    }

    public Niveau updateNiveau(Niveau model){
        HttpPut httpPut = new HttpPut(getUri(model.getId()) );
        String logTitre = "update Niveau";
        return functionPostPut(model, null, httpPut, logTitre);
    }

    @SneakyThrows
    public IHMModel deleteNiveau(Long id){
        IHMModel ihmModel = new IHMModel();
        HttpDelete httpDelete = new HttpDelete(getUri(id) );
        setAuthorization(httpDelete);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try(CloseableHttpResponse response = httpClient.execute(httpDelete)){
            LOG.log(Level.INFO, "Delete Niveau Response status: {0}", response.getStatusLine().getStatusCode());
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
                LOG.log(Level.WARNING, "Delete Niveau Response: {0}", response);
            }
        }catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            httpClient.close();
        }
        return null;
    }

    @SneakyThrows
    public List<Niveau> getAllNiveau(){
        List<Niveau> listNiveau = new LinkedList<>();
        HttpGet get = new HttpGet(getUri());
        setAuthorization(get);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            LOG.log(Level.INFO, "list Niveaux Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONArray jsonArray = result.optJSONObject("data").optJSONArray("niveaux");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listNiveau.add(jsonConvertService.jsonToNiveau((JSONObject) jsonArray.get(i)));
                    }
                    return listNiveau;
                }
            } else {
                LOG.log(Level.WARNING, "list Niveaux Response : {0}",
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
    private Niveau functionPostPut(Niveau model, HttpPost httpPost, HttpPut httpPut, String logTitre){
        Niveau obj = new Niveau();

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
                        obj = jsonConvertService.jsonToNiveau(result.optJSONObject("data").optJSONObject("workflow"));
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
}
