package com.klemmy.novelideas.jpa;

import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CharacterProfile {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  @NotBlank(message = "Must have a Character Name")
  private String characterName;
  private String title;
  private String firstName;
  private String middleName;
  private String surname;
  private String nickName;

  @ManyToOne
  @JoinColumn(name = "character_gender_id")
  private CharacterGender characterGender;

  @NotNull(message = "What Type or Importance is this character")
  @ManyToOne(optional = false)
  @JoinColumn(name = "character_importance_id", nullable = false)
  private CharacterImportance characterImportance;

  @Length(max = 4000)
  @Column(length = 4000)
  private String physicalDescription;

  @Length(max = 4000)
  @Column(length = 4000)
  private String innerGoal;

  @Length(max = 4000)
  @Column(length = 4000)
  private String outerGoal;

  @Length(max = 4000)
  @Column(length = 4000)
  private String functionInStory;

  private LocalDate dateOfBirth;

}
