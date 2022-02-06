package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterGenderRepository extends JpaRepository<CharacterGender, Integer> {

  List<CharacterGender> findByIsDeletedFalse();

  //Look up deleted entities
  @Query("select e from #{#entityName} e where e.isDeleted=true")
  public List<CharacterGender> recycleBin();

}
