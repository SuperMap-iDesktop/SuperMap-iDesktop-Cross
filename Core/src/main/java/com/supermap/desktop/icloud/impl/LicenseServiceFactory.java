package com.supermap.desktop.icloud.impl;

import com.supermap.desktop.icloud.api.LicenseService;
import com.supermap.desktop.icloud.commontypes.ProductType;
import com.supermap.desktop.icloud.online.AuthenticationException;
import com.supermap.desktop.icloud.online.AuthenticatorImpl;
import com.supermap.desktop.icloud.online.UsernamePassword;
import com.supermap.desktop.utilities.CoreResources;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/24.
 * LicenseService创建工厂
 */
public class LicenseServiceFactory {
    private static final String STR_SERVIE_URL = "http://www.supermapol.com/shiro-cas";
    private static final String KEY_STORE_RESOURCE = "StartComKeystore";
    private static final SSLContext SSLCONTEXT;
    private static final URL LICENSE_SERVICE_LOGIN;
    private static final Map<ProductType, String> APP_KEYS;

    static {
        SSLCONTEXT = loadSSLContext(KEY_STORE_RESOURCE);
        try {
            LICENSE_SERVICE_LOGIN = new URL(STR_SERVIE_URL);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
        Map<ProductType, String> appKeys = new HashMap<>();
        appKeys.put(ProductType.IDESKTOP, "12afd4f1807824719803caaea4e48d6a6");
        appKeys.put(ProductType.ISERVER, "0a6c50c961b1490390234871aa5f2b85");
        appKeys.put(ProductType.IEXPRESS, "2c9521ad030a45938ce6dbf20873a5d2");
        appKeys.put(ProductType.ICLOUDMANAGER, "46d3e82911634ac59025495ff4b628e5");
        appKeys.put(ProductType.IPORTAL, "5202cac5de6a48f88dbb6dd4d7d9d97a");
        appKeys.put(ProductType.IOBJECT, "b373b3f803964459ae43af8abc1976bd");
        appKeys.put(ProductType.IMOBILE, "1c536c4a222c47fdb68746b79990cd9f");
        APP_KEYS = Collections.unmodifiableMap(appKeys);
    }

    private static SSLContext loadSSLContext(String resource) {

        URL url = CoreResources.getResourceURL("/coreresources/StartComKeystore");
        assert (url != null) : "load trust keystore failed";
        try {
            return SSLContexts.custom().loadTrustMaterial(url, null, new TrustSelfSignedStrategy()).build();
        } catch (
                Exception e)

        {
            throw new IllegalStateException("load key store " + resource + " failed.", e);
        }

    }

    public static CloseableHttpClient getClient(String username, String password) {
        CloseableHttpClient client = null;
        AuthenticatorImpl authenticator = new AuthenticatorImpl();
        authenticator.setSsoHttpClientBuilder(HttpClients.custom().setSSLContext(SSLCONTEXT));
        try {
            client = authenticator.authenticate(new UsernamePassword(username, password), HttpClients.custom().setSSLContext(SSLCONTEXT), LICENSE_SERVICE_LOGIN);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static LicenseService create(CloseableHttpClient client, ProductType clientProductType)
            throws AuthenticationException {
        LicenseServiceImpl licenseService = new LicenseServiceImpl();
        if (null != APP_KEYS.get(clientProductType)) {
            String appKey = APP_KEYS.get(clientProductType);
            licenseService.setAppKey(appKey);
            licenseService.setClient(client);
        }
        return licenseService;
    }

}
