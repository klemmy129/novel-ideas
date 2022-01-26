package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Integer> {

  void delete(CharacterProfile characterProfile);

}

