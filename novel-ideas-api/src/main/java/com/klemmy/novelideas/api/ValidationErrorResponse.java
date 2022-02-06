package com.klemmy.novelideas.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ValidationErrorResponse implements Serializable {
  private static final long serialVersionUID = 7308441932230753545L;

  private List<Violation> violations = new ArrayList<>();

}

