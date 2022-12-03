package com.klemmy.novelideas.config;

import com.klemmy.novelideas.client3.NovelIdeasClient3;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Configuration
@EnableConfigurationProperties({NovelIdeasClientProperties.class, SslProperties.class})
@ConditionalOnProperty(name = "novel-ideas.url")
public class NovelIdeasService {

    private final NovelIdeasClientProperties novelIdeasClientProperties;
    private final SslProperties sslProperties;

    public NovelIdeasService(NovelIdeasClientProperties novelIdeasClientProperties, SslProperties sslProperties) {
        this.novelIdeasClientProperties = novelIdeasClientProperties;
        this.sslProperties = sslProperties;
    }

    @Bean
    NovelIdeasClient3 clientService() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
//        String keyStorePassword = sslProperties.keyStorePassword();
        String trustStorePassword = sslProperties.trustStorePassword();
//        KeyStore keyStore = KeyStore.getInstance(sslProperties.keyStoreType());
 //       keyStore.load(new FileInputStream(sslProperties.keyStore()),keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(sslProperties.trustStoreType());
        trustStore.load(new FileInputStream(sslProperties.trustStore()),trustStorePassword.toCharArray());

//        SSLContext sslContext = new SSLContextBuilder()
//                .loadKeyMaterial(
//                        keyStore,
//                        keyStorePassword.toCharArray()
//                )
//                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
//                .build();
//        SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
//                .setSslContext(sslContext)
//                .build();
//        HttpClientConnectionManager httpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
//                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .setDefaultTlsConfig(TlsConfig.custom()
//                        .setHandshakeTimeout(Timeout.ofSeconds(30))
//                        .setSupportedProtocols(TLS.V_1_3)
//                        .build())
//                .build();
     //   TrustManagerFactorySpi k = new TrustManagerFactorySpi();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(tmf)
                .build();
  //      ClientHttpConnector httpConnector = (ClientHttpConnector) HttpClient.create().secure(t -> t.sslContext(sslContext) );


        WebClient client = WebClient.builder()
                .baseUrl(novelIdeasClientProperties.url())
      //          .clientConnector(httpConnector)
              //  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        HttpServiceProxyFactory proxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
                        .build();

        NovelIdeasClient3 kkk= proxyFactory.createClient(NovelIdeasClient3.class);
        return kkk;
       // return proxyFactory.createClient(NovelIdeasClient3.class);
    }
}
