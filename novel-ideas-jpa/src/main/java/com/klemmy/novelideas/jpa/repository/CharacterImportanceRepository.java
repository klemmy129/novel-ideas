package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterImportance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterImportanceRepository extends JpaRepository<CharacterImportance, Integer> {

  List<CharacterImportance> findByIsDeletedFalse();

  //Look up deleted entities
  @Query("select e from #{#entityName} e where e.isDeleted=true")
  public List<CharacterImportance> recycleBin();

}
