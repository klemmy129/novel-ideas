package com.klemmy.novelideas.jpa;

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

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "character_importance", indexes = {
    @Index(name = "idx_char_imp_is_deleted", columnList = "is_deleted")
})
@SQLDelete(sql = "UPDATE #{#entityName} SET isDeleted = true WHERE id=?")
public class CharacterImportance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(nullable = false)
  private String importance;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = Boolean.FALSE;

}