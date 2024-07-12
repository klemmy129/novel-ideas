package com.klemmy.novelideas.config;

import com.klemmy.novelideas.error.FindDataException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class NovelIdearsRestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

    return httpResponse.getStatusCode().isError();
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {


    if (httpResponse.getStatusCode().is5xxServerError()) {
      // handle SERVER_ERROR
    } else if (httpResponse.getStatusCode().is4xxClientError()) {
      // handle CLIENT_ERROR
      if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
        try {
          throw new FindDataException(new Throwable("Can not find it"));
        } catch (FindDataException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
