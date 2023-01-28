package com.klemmy.novelideas.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestPage<T> extends PageImpl<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 3348189030448292033L;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestPage(@JsonProperty("content") List<T> content, @JsonProperty("number") int number, @JsonProperty("size") int size,
                          @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first,
                          @JsonProperty("numberOfElements") int numberOfElements) {
    super(content, PageRequest.of(number, size), totalElements);
  }

  public RestPage(List<T> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  public RestPage(List<T> content) {
    super(content);
  }

  public RestPage() {
    super(new ArrayList<>());
  }

}