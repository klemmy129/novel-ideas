package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Character Profile")
public class CharacterProfileDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private Integer id;
  @NotEmpty(message = "Must have a Character Name")
  private String characterName;
  private String title;
  private String firstName;
  private String middleName;
  private String surname;
  private String nickName;
  private CharacterGenderDto gender;
  @NotNull(message = "What Type or Importance is this character")
  private CharacterImportanceDto characterImportance;
  @Length(max = 4000)
  private String physicalDescription;
  @Length(max = 4000)
  private String innerGoal;
  @Length(max = 4000)
  private String outerGoal;
  @Length(max = 4000)
  private String functionInStory;
  private LocalDate dateOfBirth;
}
