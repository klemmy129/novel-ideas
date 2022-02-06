package com.klemmy.novelideas.jpa;

import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "character_gender", indexes = {
    @Index(name = "idx_character_gender_is_deleted", columnList = "is_deleted")
})
@SQLDelete(sql = "UPDATE #{#entityName} SET isDeleted = true WHERE id=?")
public class CharacterGender {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @Column(nullable = false, unique = true)
  private String gender;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = Boolean.FALSE;
}
