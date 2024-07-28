package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterGenderRepository extends JpaRepository<CharacterGender, Long> {

}
