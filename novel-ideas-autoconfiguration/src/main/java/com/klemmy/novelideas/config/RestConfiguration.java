package com.klemmy.novelideas.config;

import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

@Configuration
@EnableConfigurationProperties(SslProperties.class)
public class RestConfiguration {

    private final SslProperties sslProperties;

    public RestConfiguration(SslProperties sslProperties) {
        this.sslProperties = sslProperties;
    }

    @Bean(name = "ssl")
    public RestTemplate restTemplateSsl() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        String keyStorePassword = sslProperties.keyStorePassword();
        String trustStorePassword = sslProperties.trustStorePassword();
        KeyStore keyStore = KeyStore.getInstance(sslProperties.keyStoreType());
        keyStore.load(new FileInputStream(sslProperties.keyStore()), keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
        trustStore.load(new FileInputStream(sslProperties.trustStore()), trustStorePassword.toCharArray());

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(
                        keyStore,
                        keyStorePassword.toCharArray()
                )
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .build();
        HttpClientConnectionManager httpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConnectionSocketFactory)
            //    .setDefaultTlsConfig(TlsConfig.DEFAULT)
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new NovelIdearsRestTemplateResponseErrorHandler());
        return restTemplate;
    }

    @Bean(name = "novelIdeaClient")
    public RestTemplate restTemplateHTTP2() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        String keyStorePassword = sslProperties.keyStorePassword();
        String trustStorePassword = sslProperties.trustStorePassword();
        KeyStore keyStore = KeyStore.getInstance(sslProperties.keyStoreType());
        keyStore.load(new FileInputStream(sslProperties.keyStore()), keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
        trustStore.load(new FileInputStream(sslProperties.trustStore()), trustStorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        X509TrustManager trustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                .build();
        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), trustManager).build();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
        return new RestTemplate(requestFactory);
    }
}
