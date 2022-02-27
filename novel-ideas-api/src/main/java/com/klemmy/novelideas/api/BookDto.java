package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Schema(description = "Book")
public class BookDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private Integer id;

  @NotNull(message = "Need a Book Name")
  @Length(max = 255)
  private final String name;

  @Length(max = 4000)
  private final String description;

  @Past
  private final LocalDateTime startDate;

  @Enumerated(EnumType.STRING)
  @ArraySchema(schema = @Schema(implementation = BookState.class, description = "BookState"))
  private final BookState state;

  @ArraySchema(schema = @Schema(implementation = CharacterProfileDto.class, description = "Character in Book"))
  private final List<CharacterProfileDto> characterProfiles;
}
