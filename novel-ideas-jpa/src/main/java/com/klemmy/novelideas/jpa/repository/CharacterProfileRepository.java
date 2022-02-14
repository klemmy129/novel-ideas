package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.specification.FilterCharacterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Integer>, JpaSpecificationExecutor<CharacterProfile> {

  void delete(CharacterProfile characterProfile);

  default Page<CharacterProfile> findAllByFilters(String queryName,
                                                  String importance,
                                                  String gender,
                                                  Pageable pageable) {
    return findAll(new FilterCharacterSpecification(queryName, importance, gender), pageable);
  }
}
