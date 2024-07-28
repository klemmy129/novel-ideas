package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Long>, JpaSpecificationExecutor<CharacterProfile> {

}
