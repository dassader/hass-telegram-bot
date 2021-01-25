package com.home.hasstelegrambot.config;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.function.Supplier;

@Configuration
public class HttpClientConfig {

    @Bean
    public Supplier<HttpClient> httClientBuilder(PoolingHttpClientConnectionManager connectionManager) {
        return () -> {
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setConnectionManager(connectionManager);
            return builder.build();
        };
    }

    @Bean
    public PoolingHttpClientConnectionManager connectionManager(SSLContext sslContext) {
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();

        return new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    }

    @Bean
    public SSLContext trustAllSslContext() throws Exception {
        return new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
    }
}
