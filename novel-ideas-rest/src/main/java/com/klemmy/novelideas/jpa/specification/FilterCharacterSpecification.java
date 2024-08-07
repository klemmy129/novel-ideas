package com.klemmy.novelideas.jpa.specification;

import com.klemmy.novelideas.jpa.CharacterGender;
import com.klemmy.novelideas.jpa.CharacterImportance;
import com.klemmy.novelideas.jpa.CharacterProfile;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *  This Specification is NOT used anymore, but I thought I would leave it as a demo.
 *  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */

@AllArgsConstructor
public class FilterCharacterSpecification implements Specification<CharacterProfile> {

  private final String queryName;
  private final String importance;
  private final String gender;

  @Override
  public Predicate toPredicate(Root<CharacterProfile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

    Set<Predicate> allCriteria = new HashSet<>();
    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("characterName")));

    criteriaQuery.distinct(true);

    if (queryName != null) {
      String wild = ('%' + queryName + '%').toLowerCase();
      allCriteria.add(criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("characterName")), wild),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), wild),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), wild),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("nickName")), wild)
      ));
    }
    if (importance != null) {
      final Join<CharacterProfile, CharacterImportance> characterImportance = root.join("characterImportance", JoinType.LEFT);
      allCriteria.add(criteriaBuilder.equal(
          criteriaBuilder.lower(characterImportance.get("importance")), importance.toLowerCase()));
    }
    if (gender != null) {
      final Join<CharacterProfile, CharacterGender> characterGender = root.join("characterGender", JoinType.LEFT);
      allCriteria.add(criteriaBuilder.equal(
          criteriaBuilder.lower(characterGender.get("gender")), gender.toLowerCase()));
    }

    return criteriaBuilder.and(allCriteria.toArray(new Predicate[0]));
  }

}
