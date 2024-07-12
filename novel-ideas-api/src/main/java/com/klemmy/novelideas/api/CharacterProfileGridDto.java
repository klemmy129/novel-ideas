package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Character Profile")
public class CharacterProfileGridDto implements Serializable {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private Long id;
  @NotEmpty(message = "Must have a Character Name")
  private String characterName;
  private String title;
  private String firstName;
  private String middleName;
  private String surname;
  private String nickName;
  private String gender;
  private String characterImportance;
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
