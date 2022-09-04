package com.klemmy.novelideas.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

public record CharacterImportanceDto(
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    Integer id,

    @NotNull
    String importance,

    boolean isDeleted) implements Serializable {
}
