package com.supermap.desktop.ui.icloud.impl;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.supermap.desktop.ui.icloud.api.LicenseService;
import com.supermap.desktop.ui.icloud.commontypes.*;
import com.supermap.desktop.ui.icloud.fastJSon.CloudLicenseJSON;
import com.supermap.desktop.ui.icloud.fastJSon.ExcludesPropertyFilter;
import com.supermap.desktop.ui.icloud.utils.ValidationUtil;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;


/**
 * Created by xie on 2016/12/24.
 */
public class LicenseServiceImpl implements LicenseService {
    private static String baseUrl = "https://license.supermapol.com/api/web/v1/ilicense/license";
    private CloseableHttpClient client;
    private String appKey;
    private static final TypeReference<ServiceResponse<Integer>> RETURN_RESPONSE_TYPE = new TypeReference<ServiceResponse<Integer>>() {
    };
    private static final TypeReference<ServiceResponse<QueryTrialLicenseResponse>> QUERY_TRIAL_RESPONSE_TYPE = new TypeReference<ServiceResponse<QueryTrialLicenseResponse>>() {
    };
    private static final TypeReference<ServiceResponse<QueryFormalLicenseResponse>> QUERY_FORMAL_RESPONSE_TYPE = new TypeReference<ServiceResponse<QueryFormalLicenseResponse>>() {
    };
    private static final TypeReference<ServiceResponse<ApplyTrialLicenseResponse>> APPLY_TRIAL_RESPONSE_TYPE = new TypeReference<ServiceResponse<ApplyTrialLicenseResponse>>() {
    };
    private static final TypeReference<ServiceResponse<ApplyFormalLicenseResponse>> APPLY_FORMAL_RESPONSE_TYPE = new TypeReference<ServiceResponse<ApplyFormalLicenseResponse>>() {
    };
    private static final TypeReference<ServiceResponse<Void>> DELETE_TRIAL_LICENSE_RESPONSE_TYPE = new TypeReference<ServiceResponse<Void>>() {
    };
    private static final Charset UTF8 = Charsets.UTF_8;
    private static final String JSON_UTF8_CONTENT_TPYE = "application/json;;charset=" + UTF8.name();

    public LicenseServiceImpl() {
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    /**
     * 查询试用许可信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    public ServiceResponse<QueryTrialLicenseResponse> query(QueryTrialLicenseRequest request)
            throws IOException {
        ValidationUtil.validate(request);
        String url = baseUrl + "/trial" + this.appKey + request.getQueryStr();
        HttpGet getRequest = new HttpGet(url);
        CloseableHttpResponse response = this.client.execute(getRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, QUERY_TRIAL_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * 查询正式许可信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    public ServiceResponse<QueryFormalLicenseResponse> query(QueryFormalLicenseRequest request)
            throws IOException {
        ValidationUtil.validate(request);
        String url = baseUrl + "/formal" + this.appKey + request.getQueryStr();
        HttpGet getRequest = new HttpGet(url);
        CloseableHttpResponse response = this.client.execute(getRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, QUERY_FORMAL_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * 申请正式许可
     *
     * @param request
     * @return
     * @throws IOException
     */
    public ServiceResponse<ApplyFormalLicenseResponse> apply(ApplyFormalLicenseRequest request)
            throws IOException {
        ValidationUtil.validate(request);
        String url = baseUrl + "/formal/ids/" + request.licenseId.value() + "/returns" + this.appKey;
        HttpPost postRequest = new HttpPost(url);
        ExcludesPropertyFilter filter = new ExcludesPropertyFilter();
        filter.setExcludes(new String[]{"licenseId"});
        String jsonBody = CloudLicenseJSON.toJSONString(request, filter, new SerializerFeature[0]);
        StringEntity body = new StringEntity(jsonBody, UTF8);
        body.setContentType(JSON_UTF8_CONTENT_TPYE);
        postRequest.setEntity(body);
        CloseableHttpResponse response = this.client.execute(postRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, APPLY_FORMAL_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * 申请试用许可
     *
     * @param request
     * @return
     * @throws IOException
     */
    public ServiceResponse<ApplyTrialLicenseResponse> apply(ApplyTrialLicenseRequest request)
            throws IOException {
        ValidationUtil.validate(request);
        String url = baseUrl + "/trial/ids" + this.appKey;
        HttpPost postRequest = new HttpPost(url);
        String jsonBody = CloudLicenseJSON.toJSONString(request);
        StringEntity body = new StringEntity(jsonBody, UTF8);
        body.setContentType(JSON_UTF8_CONTENT_TPYE);
        postRequest.setEntity(body);
        CloseableHttpResponse response = this.client.execute(postRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, APPLY_TRIAL_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * 归还真是许可
     *
     * @param request
     * @return
     * @throws IOException
     */
    public ServiceResponse<Integer> returns(ReturnLicenseRequest request)
            throws IOException {
        ValidationUtil.validate(request);
        String url = baseUrl + "/formal/ids/" + request.licenseId.value() + "/returns/ids/" + request.returnId.value() + this.appKey;
        HttpPut postRequest = new HttpPut(url);
        CloseableHttpResponse response = this.client.execute(postRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, RETURN_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * 归还试用许可
     *
     * @param returnId
     * @return
     * @throws IOException
     */
    public ServiceResponse<Void> deleteTrialLicense(ReturnId returnId)
            throws IOException {
        String url = baseUrl + "/trial/ids/returns/ids/" + returnId.value() + this.appKey;
        HttpPut httpRequest = new HttpPut(url);
        CloseableHttpResponse response = this.client.execute(httpRequest);
        String json = getJsonStrFromResponse(response);
        return (ServiceResponse) CloudLicenseJSON.parseObject(json, DELETE_TRIAL_LICENSE_RESPONSE_TYPE, new Feature[0]);
    }

    /**
     * @param response
     * @return
     * @throws java.lang.UnsupportedOperationException
     * @throws IOException
     */
    private String getJsonStrFromResponse(CloseableHttpResponse response)
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
            response.close();
        }

        return result;
    }

    public void close()
            throws IOException {
        IOUtils.closeQuietly(this.client);
    }

    public void setAppKey(String value) {
        this.appKey = ("?appKey=" + value);
    }
}
