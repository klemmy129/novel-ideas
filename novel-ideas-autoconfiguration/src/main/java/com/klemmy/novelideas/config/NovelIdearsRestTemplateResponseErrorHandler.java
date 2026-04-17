package com.klemmy.novelideas.config;

import com.klemmy.novelideas.error.FindDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class NovelIdearsRestTemplateResponseErrorHandler implements ResponseErrorHandler {

  private static final Logger logger = LoggerFactory.getLogger(NovelIdearsRestTemplateResponseErrorHandler.class);

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

    return httpResponse.getStatusCode().isError();
  }

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse httpResponse) throws IOException {


    if (httpResponse.getStatusCode().is5xxServerError()) {
      // handle SERVER_ERROR
    } else if (httpResponse.getStatusCode().is4xxClientError()) {
      // handle CLIENT_ERROR
      if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
        try {
          logger.error("Error encountered during HTTP request.");
          logger.error("Request URL: {}", url);
          logger.error("HTTP Method: {}", method);
          logger.error("Status Code: {} ({})", httpResponse.getStatusCode(), httpResponse.getStatusText());
          logger.error("Response Body: {}", new String(httpResponse.getBody().readAllBytes(), StandardCharsets.UTF_8));
          throw new FindDataException(new Throwable("Can not find it"));
        } catch (FindDataException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
