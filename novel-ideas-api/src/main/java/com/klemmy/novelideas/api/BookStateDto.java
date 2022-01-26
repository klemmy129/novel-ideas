package com.klemmy.novelideas.api;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@Builder
public class BookStateDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private final Integer id;

  @NotEmpty
  private final String state;

  private boolean isDeleted;
}
