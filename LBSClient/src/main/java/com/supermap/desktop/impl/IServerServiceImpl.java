package com.supermap.desktop.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IServerService;
import com.supermap.desktop.params.*;
import org.apache.commons.compress.utils.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by xie on 2017/1/6.
 */
public class IServerServiceImpl implements IServerService {

    private final String HTTP_STR = "http://";
    private final String KERNELDENSITY_URL = "/iserver/services/processing/rest/v1/jobs/spatialanalyst/kernelDensity.json";
    private final String LOGIN_URL = "/iserver/services/security/login.json";
    private static final Charset UTF8 = Charsets.UTF_8;
    private static final String JSON_UTF8_CONTENT_TPYE = "application/json;;charset=" + UTF8.name();

    @Override
    public HttpClient login(String userName, String passWord) {
        HttpClient result = null;
        try {
            HttpClient client = new DefaultHttpClient();
            String url = HTTP_STR + IServerLoginInfo.ipAddr + LOGIN_URL;
            HttpPost post = new HttpPost(url);
            Token token = new Token();
            token.username = userName;
            token.password = passWord;
            token.rememberme = "false";
            String jsonBody = JSON.toJSONString(token);
            StringEntity body = new StringEntity(jsonBody, UTF8);
            body.setContentType(JSON_UTF8_CONTENT_TPYE);
            post.setEntity(body);
            HttpResponse response = client.execute(post);
            if (response != null) {
                // 获取client
                String responseStr = getJsonStrFromResponse(response);
                IServerResponse iServerResponse = JSON.parseObject(responseStr, IServerResponse.class);
                if ("true".equals(iServerResponse.succeed))
                    result = client;
            }
        } catch (UnsupportedEncodingException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (ClientProtocolException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (IOException e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return result;
    }

    @Override
    public KernelDensityJobResponse query(KernelDensityJobSetting kernelDensityJobSetting) {
        KernelDensityJobResponse result = null;
        HttpResponse response = null;
        try {
            String url = HTTP_STR + IServerLoginInfo.ipAddr + KERNELDENSITY_URL;
            HttpPost post = new HttpPost(url);
            String jsonBody = JSON.toJSONString(kernelDensityJobSetting);
            StringEntity body = new StringEntity(jsonBody, UTF8);
            body.setContentType(JSON_UTF8_CONTENT_TPYE);
            post.setEntity(body);
            response = IServerLoginInfo.client.execute(post);
            if (null != response) {
                String returnInfo = getJsonStrFromResponse(response);
                KernelDensityJobResponse tempResponse = JSON.parseObject(returnInfo, KernelDensityJobResponse.class);
                if ("true".equals(tempResponse.succeed)) {
                    result = tempResponse;
                }
            }
        } catch (ClientProtocolException e) {
            Application.getActiveApplication().getOutput().output(e);
        } catch (IOException e) {
            Application.getActiveApplication().getOutput().output(e);
        }

        return result;
    }

    @Override
    public String query(String newResourceLocation) {
        String result = null;
        HttpResponse response = null;
        try {
            HttpGet get = new HttpGet(newResourceLocation + ".json");
            response = IServerLoginInfo.client.execute(get);
            if (null != response) {
                result = getJsonStrFromResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param response
     * @return
     * @throws java.lang.UnsupportedOperationException
     * @throws IOException
     */
    private String getJsonStrFromResponse(HttpResponse response)
            throws java.lang.UnsupportedOperationException, IOException {
        String result = "";
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("----------------------");
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println(result);
                System.out.println("----------------------");
            }
        } finally {
            response = null;
        }

        return result;
    }
}
