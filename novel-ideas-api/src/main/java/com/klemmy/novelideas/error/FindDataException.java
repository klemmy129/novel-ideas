package com.klemmy.novelideas.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FindDataException extends Exception {

  public FindDataException(String message) {
    super(message);
  }

  public FindDataException(String message, Throwable cause) {
    super(message, cause);
  }

  public FindDataException(Throwable cause) {
    super(cause);
  }

}
