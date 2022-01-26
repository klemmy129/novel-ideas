package com.klemmy.novelideas.api;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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

  private final BookStateDto state;

  private final List<CharacterProfileDto> characterProfiles;
}
