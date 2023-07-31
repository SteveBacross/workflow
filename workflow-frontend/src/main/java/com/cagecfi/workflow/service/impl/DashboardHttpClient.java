package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.model.DashBoardStatusData;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.service.BaseHttpClient;
import com.github.openjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DashboardHttpClient extends BaseHttpClient {

    @Autowired
    private JsonConvertServiceWf jsonConvertService;

    private static final Logger LOG = Logger.getLogger(DepartementHttpClient.class.getName());
    private String baseURI = "/dash";

    private String getUri(){
        return this.getServerUri(APIConstants.WORKFLOW_URI) + baseURI;
    }

    @SneakyThrows
    public DashBoardStatusData getDashboardStatusData(){
        DashBoardStatusData dash;
        HttpGet httpGet = new HttpGet(getUri());
        setAuthorization(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            LOG.log(Level.INFO,"get dashboardStatus Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    JSONObject jsonObject = result.getJSONObject("data").optJSONObject("dashboard");

                    dash = jsonConvertService.jsonToDashBoardStatus(jsonObject);
                    return dash;
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

}
