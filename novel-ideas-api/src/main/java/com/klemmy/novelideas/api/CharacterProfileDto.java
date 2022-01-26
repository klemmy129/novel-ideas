package com.klemmy.novelideas.api;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class CharacterProfileDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private final Integer id;
  @NotEmpty(message = "Must have a Character Name")
  private final String characterName;
  private final String title;
  private final String firstName;
  private final String middleName;
  private final String surname;
  private final String nickName;
  private final CharacterGenderDto gender;
  @NotNull(message = "What Type or Importance is this character")
  private final CharacterImportanceDto characterImportance;
  @Length(max = 4000)
  private final String physicalDescription;
  @Length(max = 4000)
  private final String innerGoal;
  @Length(max = 4000)
  private final String outerGoal;
  @Length(max = 4000)
  private final String functionInStory;
  private final LocalDate dateOfBirth;
}
