package com.klemmy.novelideas.jpa;

import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(nullable = false)
  @Length(max = 255)
  @NotNull(message = "Need a Book Name")
  private String name;

  @Column(length = 4000)
  @Length(max = 4000)
  private String description;

  @Past
  @Column(nullable = false)
  private LocalDateTime startDate;

  @Enumerated(EnumType.STRING)
  private BookState state;

  @ManyToMany
  @JoinTable(name = "book_character_profile",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "character_profile_id"))
  private List<CharacterProfile> characterProfiles = new java.util.ArrayList<>();

}
