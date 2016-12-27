package com.supermap.desktop.ui.icloud.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2016/12/24.
 * 登陆Online实现类
 */
public class AuthenticatorImpl implements Authenticator {

    private HttpClientBuilder ssoHttpClientBuilder = null;

    public void setSsoHttpClientBuilder(HttpClientBuilder ssoHttpClientBuilder) {
        this.ssoHttpClientBuilder = ssoHttpClientBuilder.disableCookieManagement().disableRedirectHandling();
    }

    /**
     * 获取登陆后的HttpClient，为LicenseServece提供依赖的HttpClient
     *
     * @param token         令牌（登陆信息）
     * @param clientBuilder HttpClient创建器
     * @param service
     * @return
     * @throws AuthenticationException
     */
    public CloseableHttpClient authenticate(UsernamePassword token, HttpClientBuilder clientBuilder, URL service)
            throws AuthenticationException {
        CloseableHttpClient client = this.ssoHttpClientBuilder.build();
        try {
            String jSessionId = login(client, token, service);
            clientBuilder.addInterceptorLast(new SessionIdCookie(jSessionId));
            return clientBuilder.build();
        } catch (IOException e) {
            throw new AuthenticationException(e);
        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    /**
     * 获取Online的jsessionid
     *
     * @param client
     * @param token   用户名密码
     * @param service 链接地址
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    public String login(CloseableHttpClient client, UsernamePassword token, URL service)
            throws IOException, AuthenticationException {
        String ssoLoginUrl = getURI(service.toString());
        RequestContent content = getSSOLoginParameter(client, ssoLoginUrl);
        String location = sendUsernamePassword(client, token, ssoLoginUrl, content);
        return getJSessionId(client, location);
    }

    /**
     * 获取SSO登陆URL
     *
     * @param service
     * @return
     */
    private String getURI(String service) {
        String uri = "https://sso.supermap.com/login?format=json";
        if (StringUtils.isNotBlank(service)) {
            try {
                uri = uri + "&service=" + URLEncoder.encode(service, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(StandardCharsets.UTF_8.name() + " Unsupported. That's unacceptable!!", e);
            }
        }
        return uri;
    }

    /**
     * 第一次请求获取SSO的jsessionid
     * 请求方法get
     *
     * @param client
     * @param uri
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    private RequestContent getSSOLoginParameter(CloseableHttpClient client, String uri)
            throws IOException, AuthenticationException {
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(get);
        try {
            return buildRequestContent(response);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * 第二次请求获取location:location包含jsessionid,execution,_eventId,lt四个参数
     * 请求方法post
     *
     * @param client
     * @param token
     * @param uri
     * @param content
     * @return
     * @throws IOException
     * @throws AuthenticationException
     */
    private String sendUsernamePassword(CloseableHttpClient client, UsernamePassword token, String uri, RequestContent content)
            throws IOException, AuthenticationException {
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> formparams = new ArrayList();
        formparams.add(new BasicNameValuePair("username", token.username));
        formparams.add(new BasicNameValuePair("password", token.password));
        formparams.add(new BasicNameValuePair("lt", content.lt));
        formparams.add(new BasicNameValuePair("execution", content.execution));
        formparams.add(new BasicNameValuePair("_eventId", content._eventId));
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, StandardCharsets.UTF_8);
        post.setHeader("Cookie", content.jsessionid);
        post.setEntity(uefEntity);
        CloseableHttpResponse response = client.execute(post);
        try {
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                throw new AuthenticationException("login failed. invalid username/password?");
            }
            if (code == 302) {
                Header header = response.getFirstHeader("Location");
                if (header == null) {
                    throw new AuthenticationException("unexpected response:code 302 without Location response header");
                }
                return header.getValue();
            }
            String entity = getEntityText(response);
            throw new AuthenticationException("login failed. code:" + code + ";entity:" + entity);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * 将Entity转换为字符串，并编码
     *
     * @param response
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private String getEntityText(CloseableHttpResponse response)
            throws ParseException, IOException {
        HttpEntity entity = response.getEntity();
        return entity == null ? null : EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

    /**
     * 第三次请求获取online的jsessionid(用户id)
     *
     * @param client
     * @param location
     * @return 登陆成功的jsessionid
     * @throws IOException
     * @throws AuthenticationException
     */
    private String getJSessionId(CloseableHttpClient client, String location)
            throws IOException, AuthenticationException {
        HttpClientContext context = HttpClientContext.create();
        HttpGet httpGetToCheckTicket = new HttpGet(location);
        CloseableHttpResponse response = client.execute(httpGetToCheckTicket, context);
        try {
            int code = response.getStatusLine().getStatusCode();
            String result = getJSessionIdFromResponse(response);
            String entity;
            if ((code >= 400) || (StringUtils.isEmpty(result))) {
                entity = getEntityText(response);
                throw new AuthenticationException("invalid response. url:" + location + ".code:" + code + "." + entity);
            }
            return result;
        } finally {
            response.close();
        }
    }

    private RequestContent buildRequestContent(CloseableHttpResponse response)
            throws AuthenticationException, ParseException, IOException {
        RequestContent content = new RequestContent();
        HttpEntity httpEntity = response.getEntity();
        int code = response.getStatusLine().getStatusCode();
        if (httpEntity == null) {
            throw new AuthenticationException("login failed.code:" + code);
        }
        String responseInfo = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        if (200 == response.getStatusLine().getStatusCode()) {
            try {
                content = JSON.parseObject(responseInfo, RequestContent.class);
                content.jsessionid = getJSessionIdFromResponse(response);
            } catch (JSONException e) {
                throw new AuthenticationException("invalid response:" + responseInfo);
            } finally {
                EntityUtils.consumeQuietly(httpEntity);
            }
        } else {
            throw new AuthenticationException("login failed.code:" + response.getStatusLine().getStatusCode() + ".response:" + getEntityText(response));
        }
        return content;
    }

    /**
     * 从HttpRespose中获取jsessionid
     *
     * @param response
     * @return
     */
    private String getJSessionIdFromResponse(HttpResponse response) {
        Header header = response.getFirstHeader("Set-Cookie");
        if (header == null) {
            return "";
        }
        String[] kvps = StringUtils.split(header.getValue(), ';');
        for (String kvp : kvps) {
            int index = kvp.indexOf('=');
            if (StringUtils.equalsIgnoreCase(kvp.substring(0, index), "JSESSIONID")) {
                return kvp;
            }
        }
        return "";
    }

    private static class SessionIdCookie implements HttpRequestInterceptor {
        private String jSessionId;

        SessionIdCookie(String paramSessionId) {
            this.jSessionId = paramSessionId;
        }

        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            request.setHeader("Cookie", this.jSessionId);
        }
    }

    /**
     *
     */
    private static class RequestContent {
        public String lt;
        public String execution;
        public String _eventId;
        public String jsessionid;
    }
}
