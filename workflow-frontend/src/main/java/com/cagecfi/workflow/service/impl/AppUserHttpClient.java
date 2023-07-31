package com.cagecfi.workflow.service.impl;

import com.cagecfi.workflow.constantes.APIConstants;
import com.cagecfi.workflow.constantes.CommonAPIConstants;
import com.cagecfi.workflow.model.Role;
import com.cagecfi.workflow.model.Token;
import com.cagecfi.workflow.model.User;
import com.cagecfi.workflow.service.BaseHttpClient;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;
import netscape.javascript.JSException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AppUserHttpClient extends BaseHttpClient {

    private static final Logger LOG = Logger.getLogger(AppUserHttpClient.class.getName());

    private String getUri(String path){
        return this.getServerUri(APIConstants.SECURITY_URI) + path;
    }

    private String getUriUser(String path){
//        return this.getServerUri(CommonAPIConstants.UTILISATEUR_URI) + path;
        return this.getServerUri(APIConstants.WORKFLOW_URI) + path;
    }

    public User findByUsername(String username) throws IOException{
        User user = null;
        List<String> roleList = new LinkedList<>();
        HttpGet get = new HttpGet(getUri("/loadUser/" + username));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);

        try {
            LOG.log(Level.INFO, "findByUsername Response status : {0}",
                    response.getStatusLine().getStatusCode());
            if (isOk(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    user = new User();
                    user.setId(result.optLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setPassword(result.getString("password"));
                    user.setActif(result.getBoolean("actif"));
                    user.setPwdUpdated(result.getBoolean("pwdUpdated"));
                    JSONArray rolesString = result.getJSONArray("rolesString");
                    for (int i = 0; i < rolesString.length(); i++) {
                        roleList.add(rolesString.getString(i));
                    }
                    user.setRolesString(roleList);

                }
            } else {
                LOG.log(Level.WARNING, "findByUsername Response : {0}",
                        EntityUtils.toString(response.getEntity()));
            }
        }catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return user;
    }

    public Token login(String username, String password) throws IOException {
        Token token = null;
        HttpPost post = new HttpPost(getUri("/login"));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);

        try {
            LOG.log(Level.WARNING, "login Response status : {0}",
                    +response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(response.getEntity());

                    JSONObject userJson = new JSONObject(result);
                    token = new Token();
                    token.setAccessToken(userJson.get("accessToken").toString());
                    token.setRefreshToken(userJson.get("refreshToken").toString());
                }
            } else {
                LOG.log(Level.WARNING, "login Response : {0}",
                        response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return token;
    }

    public User profile(String accessToken) throws IOException {
        User user = null;
        List<String> roleList = new LinkedList<>();
        HttpGet get = new HttpGet(getUriUser("/profile"));
        get.setHeader("Authorization", "Bearer " + accessToken);

        LOG.log(Level.INFO, "accessToken: {0}", accessToken);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        try {
            LOG.log(Level.INFO, "profile Response status: {0}",
                    response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
//                    System.out.println(result);
                    user = new User();
                    user.setId(result.getLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setName(result.getString("name"));
                    user.setEmail(result.getString("email"));
                    user.setTelephone(result.getString("telephone"));
//                    user.setIsClient(result.getBoolean("isClient"));
//                    user.setEmailValid(result.getBoolean("emailValid"));
                    user.setActif(result.getBoolean("actif"));
                    user.setPassword(result.getString("password"));
//                    user.setPassword("1234");
//                    user.setUserWithPhone(result.optBoolean("userWithPhone"));
                }
            } else {
                LOG.log(Level.WARNING, "profile Response : {0}",
                        EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return user;
    }
    public List<String> roleList(Long idUser, String finalToken) throws IOException {
        List<String> roles = new LinkedList<>();
        HttpGet get = new HttpGet(getUriUser("/groupes/" + idUser + "/roles"));
        setHeaderToken(finalToken, get);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        try {
            LOG.log(Level.INFO, "list role Response status: {0}", response.getStatusLine().getStatusCode());
            if (isOkOrCreated(response)){
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    JSONArray array = new JSONArray(EntityUtils.toString(entity));
                    Role role;
                    for (int i = 0; i < array.length(); i++) {
                        role = jsonToRole(array.getJSONObject(i));
                        roles.add(role.getRolename());
                    }
                }
            } else {
                LOG.log(Level.WARNING, "list role Response : {0}", response);
            }
        } catch (Exception e){
            response.close();
            httpClient.close();
        }
        return roles;
    }

    private void setHeaderToken(String finalToken, HttpGet get) {
        get.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + finalToken);
    }

    public JSONObject jsonToZone(JSONObject result) {
        if (result == null || result.isNull("id")) {
            return null;
        }
        JSONObject zone = new JSONObject();
        zone.put("id", result.optLong("id"));
        zone.put("libelle", optString(result, "libelle"));
        return zone;
    }

    public Role jsonToRole(JSONObject result) {
        if (result == null || result.isNull("id")) {
            return null;
        }
        Role role = new Role();
        role.setId(result.optLong("id"));
        role.setRolename(result.optString("rolename"));
        role.setRolelibelle(result.optString("rolelibelle"));
        return role;
    }

    //**Password forget
    public User passwordForget(String username, String finalToken) throws IOException {
        User user = null;
        HttpGet get = new HttpGet(getUriUser("/migration/users/credentials/" + username));
        setHeaderToken(finalToken, get);
        /*  setAuthorization(get);*/

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        try {
            LOG.log(Level.INFO, "passwordForget Response status : {0}", response.getStatusLine().getStatusCode());
            if (isOk(response)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject result = new JSONObject(EntityUtils.toString(entity));
                    user = new User();
                    user.setId(result.optLong("idAppUser"));
                    user.setUsername(result.getString("username"));
                    user.setPassword(result.getString("password"));
                    user.setEmail(result.getString("email"));
                    user.setName(result.getString("name"));
                }
            } else {
                LOG.log(Level.WARNING, "passwordForget Response : {0}",
                        EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            response.close();
            httpClient.close();
        }
        return user;
    }

}
