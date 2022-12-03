package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Book")
public class BookDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private Integer id;

  @NotNull(message = "Need a Book Name")
  @Length(max = 255)
  private String name;

  @Length(max = 4000)
  private String description;

  @Past
  private LocalDateTime startDate;

  @Enumerated(EnumType.STRING)
  @Schema
  private BookState state;

  @ArraySchema(schema = @Schema(implementation = CharacterProfileDto.class, description = "Character in Book"))
  private List<CharacterProfileDto> characterProfiles;
}
