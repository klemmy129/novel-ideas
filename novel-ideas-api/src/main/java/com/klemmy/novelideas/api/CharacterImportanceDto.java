package com.klemmy.novelideas.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.io.Serializable;

public record CharacterImportanceDto(
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    Integer id,

    @NotNull
    String importance,

    boolean isDeleted) implements Serializable {
}
