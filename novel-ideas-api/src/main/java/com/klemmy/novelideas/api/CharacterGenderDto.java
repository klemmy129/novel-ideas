package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Schema(description = "Character Gender")
public record CharacterGenderDto(

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  Integer id,

  @NotNull
  String gender,

  boolean isDeleted
) implements Serializable {}
