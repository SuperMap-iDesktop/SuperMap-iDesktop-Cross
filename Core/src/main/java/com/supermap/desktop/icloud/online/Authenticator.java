package com.supermap.desktop.icloud.online;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URL;

/**
 * Created by xie on 2016/12/24.
 */

public interface Authenticator {
    CloseableHttpClient authenticate(UsernamePassword paramUsernamePassword, HttpClientBuilder paramHttpClientBuilder, URL paramURL) throws AuthenticationException;
}


