/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.service;

import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.IHMModel;
import com.cagecfi.workflow.service.impl.JsonConvertServiceWf;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import static org.apache.wicket.ThreadContext.getSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author USER
 */
public abstract class BaseHttpClient {

    @Autowired
    private JsonConvertServiceWf jsonConvertServiceWf;
    
    private static final Logger LOG = Logger.getLogger(BaseHttpClient.class.getName());

    @Value("${gateway.service-url:http://localhost:8888}")
    private String SERVER_URI;

    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    protected static SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");

    protected final String getServerUri() {
        return this.SERVER_URI;
    }

    protected final String getServerUri(String path) {
        return this.getServerUri() + path;
    }

    public BaseHttpClient() {
    }


    protected static final void setAuthorization(HttpRequestBase get) {
        get.setHeader("Authorization", "Bearer " + WorkflowHttpsSession.accessToken());

//     protected final String accessToken() {
//        SecureWebSession session = (SecureWebSession) getSession();
//        return session.getAttribute(WorkflowHttpsSession.SESSION_ACCESS_TOKEN).toString();

    }

//    protected final void setAuthorization(HttpRequestBase get) {
//        get.setHeader("Authorization", "Bearer " + accessToken());
//    }

    protected static final void setMediaType(HttpRequestBase get) {
        get.setHeader("Accept", "Application/json");
        get.setHeader("Content-Type", "Application/json");
    }

    protected static final String optString(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optString(key);
    }

    protected static final boolean isOkOrCreated(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200
                || response.getStatusLine().getStatusCode() == 201;
    }

    protected static final boolean isOk(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    protected static final boolean isCreated(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 201;
    }

    protected static final boolean builderIsOkOrCreated(Integer statusCode) {
        return statusCode == 200 || statusCode == 201;
    }

    protected static final boolean isOkOrCreatedNotFound(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200
                || response.getStatusLine().getStatusCode() == 201
                || response.getStatusLine().getStatusCode() == 404;
    }

    protected static final boolean isConflictOrNotFound(CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 409
                || response.getStatusLine().getStatusCode() == 404;
    }

    public static final File inputStreamToFile(String fileName, InputStream is) throws IOException {
        File filePath = null;
        if (is != null) {
            filePath = File.createTempFile("tmp", fileName);
            FileOutputStream fos = new FileOutputStream(filePath);
            int inByte;
            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
            }
            is.close();
            fos.close();
        }
        return filePath;
    }

    protected static final byte[] fileToBytes(File file) throws IOException {
        byte[] bFile = null;
        if (file != null) {
            bFile = Files.readAllBytes(file.toPath());
        }
        return bFile;
    }

//    protected static final File download(String url) throws IOException {
//        HttpGet get = new HttpGet(url);
//        setAuthorization(get);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = httpClient.execute(get);
//
//        try {
//            LOG.log(Level.INFO, "download Response status: {0}",
//                    response.getStatusLine().getStatusCode());
//            if (isOk(response)) {
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    String disposition = response.getFirstHeader("Content-Disposition").getValue();
//                    String fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
//                    return inputStreamToFile(fileName, entity.getContent());
//                }
//            } else {
//                LOG.log(Level.WARNING, "download Response : {0}",
//                        response);
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
//        } finally {
//            response.close();
//            httpClient.close();
//        }
//        return null;
//    }

//    protected static final boolean uploadFileToGed(String url, File file) throws IOException {
//        return uploadFileToGed(url, file, null);
//    }

//    protected static final boolean uploadFileToGed(String url, File file, String contentType) throws IOException {
//        HttpPost post = new HttpPost(url);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        if (contentType != null) {
//            FileBody fileBody = new FileBody(file, ContentType.create(contentType));
//            builder.addPart("file", fileBody);
//        } else {
//            builder.addBinaryBody("file", file);
//
//        }
//
//        setAuthorization(post);
//        HttpEntity entityBuilder = builder.build();
//        post.setEntity(entityBuilder);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = httpClient.execute(post);
//        try {
//            LOG.log(Level.INFO, "uploadFileToGed Response status: {0}", response.getStatusLine().getStatusCode());
//            if (isCreated(response)) {
//                org.apache.http.HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    // return it as a String
//                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
//                    return true;
//                }
//            } else {
//                LOG.log(Level.WARNING, "uploadFileToGed Response : {0}", EntityUtils.toString(response.getEntity()));
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
//        } finally {
//            response.close();
//            httpClient.close();
//        }
//        return false;
//    }

//    protected static final boolean uploadFileToSave(String url, File file, String contentType) throws IOException {
//        HttpPost post = new HttpPost(url);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        if (contentType != null) {
//            FileBody fileBody = new FileBody(file, ContentType.create(contentType));
//            builder.addPart("file", fileBody);
//        } else {
//            builder.addBinaryBody("file", file);
//
//        }
//
//        setAuthorization(post);
//        HttpEntity entityBuilder = builder.build();
//        post.setEntity(entityBuilder);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = httpClient.execute(post);
//        try {
//            LOG.log(Level.INFO, "uploadFileToGed Response status: {0}", response.getStatusLine().getStatusCode());
//            if (isOkOrCreated(response)) {
//                org.apache.http.HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    // return it as a String
//                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
//                    return (result.get("message").equals("true"));
//                }
//            } else {
//                LOG.log(Level.WARNING, "uploadFileToGed Response : {0}", EntityUtils.toString(response.getEntity()));
//            }
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
//        } finally {
//            response.close();
//            httpClient.close();
//        }
//        return false;
//    }

    //--------------------------------------------------------------------------------------
    public static Date stringToDate(String format, String date) throws ParseException {
        format = format == null ? "yyyy-MM-dd" : format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }

    public static String dateToString(String format, Date date) throws ParseException {
        format = format == null ? "yyyy-MM-dd" : format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    //result transform
    public static String stringParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optString(key);
    }

    public static Integer integerParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return Integer.parseInt(result.optString(key));
    }

    public static Long longParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optLong(key);
    }

    public static Boolean booleanParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optBoolean(key);
    }

    public static Double doubleParser(JSONObject result, String key) {
        if (result.isNull(key)) {
            return null;
        }
        return result.optDouble(key);
    }

    public static Date dateParser(JSONObject result, String key, String format) throws ParseException {
        if (result.isNull(key)) {
            return null;
        }
        return stringToDate(format, result.optString(key));
    }

    //Object is the Object to save or put
    @SneakyThrows
    protected Departement functionPostPuter(Object model, HttpPost httpPost, HttpPut httpPut, String logTitre){
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
        } catch (JSONException | IOException | org.apache.http.ParseException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }

        return null;
    }

}
