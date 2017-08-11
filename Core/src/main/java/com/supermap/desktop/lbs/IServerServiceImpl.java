package com.supermap.desktop.lbs;

import com.alibaba.fastjson.JSON;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.IServerLoginInfo;
import com.supermap.desktop.lbs.params.IServerResponse;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.lbs.params.Token;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by xie on 2017/1/6.
 */
public class IServerServiceImpl implements IServerService {

    private final String HTTP_STR = "http://";
	private static final String KERNELDENSITY_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/spatialanalyst/density.json";
	private static final String BUILDCACHE_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/mapping/buildCache.json";
	private static final String OVERLAYANALYSTGEO_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/spatialanalyst/vectorclip.json";
	private static final String GRIDREGIONAGGREGATION_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/spatialanalyst/aggregatepoints.json";
	private static final String SINGLEQUERY_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/spatialanalyst/query.json";
	private static final String SUMMARYREGION_URL = "/iserver/services/spatialprocessing/rest/v1/jobs/spatialanalyst/summaryregion.json";
    private final String LOGIN_URL = "/iserver/services/security/login.json";
    private static final Charset UTF8 = Charsets.UTF_8;
    private static final String JSON_UTF8_CONTENT_TPYE = "application/json;;charset=" + UTF8.name();

    @Override
    public CloseableHttpClient login(String userName, String passWord) {
        CloseableHttpClient result = null;
        try {
            IServerLoginInfo.error = false;
            CloseableHttpClient client = HttpClients.createDefault();
            String url = HTTP_STR + IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + LOGIN_URL;
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
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
                // 获取client
                String responseStr = getJsonStrFromResponse(response);
                IServerResponse iServerResponse = JSON.parseObject(responseStr, IServerResponse.class);
                if ("true".equals(iServerResponse.succeed))
                    result = client;
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(CommonProperties.getString("Strng_ConnectionException"));
            IServerLoginInfo.error = true;
        }
        return result;
    }

	@Override
	public JobResultResponse queryResult(String type,  String jsonBody){
		String url = null;
		if(type.contains("HeatMap")){
			url = HTTP_STR + IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + BUILDCACHE_URL;
		}else if(type.contains("Density")){
			url = HTTP_STR +IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + KERNELDENSITY_URL;
		}else if(type.contains("Aggregation")){
			url = HTTP_STR +IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + GRIDREGIONAGGREGATION_URL;
		}else if(type.contains("overlayanalystgeo")){
			url = HTTP_STR +IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + OVERLAYANALYSTGEO_URL;
		}else if(type.contains("SingleQuery")){
			url = HTTP_STR +IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + SINGLEQUERY_URL;
		}else if(type.contains("SummaryRegion")){
			url = HTTP_STR +IServerLoginInfo.ipAddr +":"+IServerLoginInfo.port + SUMMARYREGION_URL;
		}
		return returnJobResult(url, jsonBody);
	}

	private JobResultResponse returnJobResult(String url, String jsonBody) {
		JobResultResponse result = null;
        try {
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(jsonBody, UTF8);
            body.setContentType(JSON_UTF8_CONTENT_TPYE);
            post.setEntity(body);
            HttpResponse response = IServerLoginInfo.client.execute(post);
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
                String returnInfo = getJsonStrFromResponse(response);
                JobResultResponse tempResponse = JSON.parseObject(returnInfo, JobResultResponse.class);
                if ("true".equals(tempResponse.succeed)) {
                    result = tempResponse;
                }
            } else {
                Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_HaveNoResponse"));
                IOUtils.closeQuietly(IServerLoginInfo.client);
                IServerLoginInfo.client = HttpClients.createDefault();
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
            if (null != response && response.getStatusLine().getStatusCode() == 200) {
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

