package com.klemmy.novelideas.config;

import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
@EnableConfigurationProperties(SslProperties.class)
public class RestConfiguration {

    private final SslProperties sslProperties;

    public RestConfiguration(SslProperties sslProperties) {
        this.sslProperties = sslProperties;
    }

    @Bean(name="ssl")
    public RestTemplate restTemplateSsl() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        String keyStorePassword = sslProperties.keyStorePassword();
        String trustStorePassword = sslProperties.trustStorePassword();
        KeyStore keyStore = KeyStore.getInstance(sslProperties.keyStoreType());
        keyStore.load(new FileInputStream(sslProperties.keyStore()),keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
        trustStore.load(new FileInputStream(sslProperties.trustStore()),trustStorePassword.toCharArray());

        SSLContext sslContext = new SSLContextBuilder()
                .loadKeyMaterial(
                        keyStore,
                        keyStorePassword.toCharArray()
                )
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
HttpClient httpClient = HttpClientBuilder.create()
        .setSSLContext(sslContext)
        .build();
        ClientHttpRequestFactory requestFactory= new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
       // restTemplate.setErrorHandler();
        return restTemplate;
    }

    @Bean(name="novelIdeaClient")
    public RestTemplate restTemplateHTTP2() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        String keyStorePassword = sslProperties.keyStorePassword();
        String trustStorePassword = sslProperties.trustStorePassword();
        KeyStore keyStore = KeyStore.getInstance(sslProperties.keyStoreType());
        keyStore.load(new FileInputStream(sslProperties.keyStore()),keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
        trustStore.load(new FileInputStream(sslProperties.trustStore()),trustStorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        X509TrustManager trustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                .build();
        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(),trustManager).build();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
        return new RestTemplate(requestFactory);
    }
}
