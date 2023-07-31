/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.service.BaseHttpClient;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author dell
 */
@Service
public class RapportDemandeHttpClient extends BaseHttpClient {

    private String getUri(String path) {
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }
    private static final Logger LOG = Logger.getLogger(RapportDemandeHttpClient.class.getName());

    //Rapport demande
    @SneakyThrows
    public File getReport(Date dateDebut, Date dateFin) throws URISyntaxException, IOException {

        HttpGet httpGet = new HttpGet(getUri("/demandes/rapport/demandes"));

        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("dateDebut", dateToString(null, dateDebut))
                .addParameter("dateFin", dateToString(null, dateFin))
                .build();
        ((HttpRequestBase) httpGet).setURI(uri);
//        httpGet.setURI(uri);
        setAuthorization(httpGet);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            LOG.log(Level.INFO, "download Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header firstHeader = response.getFirstHeader("Content-Disposition");
                    String fileName = "download";
                    if (firstHeader != null) {
                        String disposition = firstHeader.getValue();
                        fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
                    }
                    return inputStreamToFile(fileName, entity.getContent());
                }
            } else {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                LOG.log(Level.WARNING, "download Response : {0}", responseBody);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return null;

    }
}
