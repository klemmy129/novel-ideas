package com.klemmy.novelideas.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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