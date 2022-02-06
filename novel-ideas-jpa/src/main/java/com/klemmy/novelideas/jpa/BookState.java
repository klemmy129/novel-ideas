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
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_state", indexes = {
    @Index(name = "idx_book_state_is_deleted", columnList = "is_deleted")
})
@SQLDelete(sql = "UPDATE #{#entityName} SET isDeleted = true WHERE id=?")
public class BookState {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotEmpty
  @Column(nullable = false)
  private String state;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = Boolean.FALSE;
}