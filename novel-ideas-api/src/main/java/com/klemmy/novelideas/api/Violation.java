package com.klemmy.novelideas.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Violation implements Serializable {
  private static final long serialVersionUID = 5638750926923442206L;

  private String fieldName;

  private String message;

}
