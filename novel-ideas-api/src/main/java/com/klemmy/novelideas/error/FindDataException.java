package com.klemmy.novelideas.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
public class FindDataException extends Exception {

  private Long id;
  public FindDataException(Long id, String message) {
    super(message);
    this.id = id;
  }

  public FindDataException(String message, Throwable cause) {
    super(message, cause);
  }

  public FindDataException(Throwable cause) {
    super(cause);
  }

}
