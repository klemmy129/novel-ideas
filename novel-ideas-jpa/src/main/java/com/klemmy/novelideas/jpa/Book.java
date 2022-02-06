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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
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
  private Integer id;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "state_id")
  private BookState state;

  @ManyToMany
  @JoinTable(name = "book_character_profile",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "character_profile_id"))
  private List<CharacterProfile> characterProfiles = new java.util.ArrayList<>();

}
