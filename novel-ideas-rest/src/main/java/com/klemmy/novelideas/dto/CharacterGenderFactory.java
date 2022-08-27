package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.jpa.CharacterGender;
import org.springframework.stereotype.Component;

@Component
public class CharacterGenderFactory {

  public static CharacterGenderDto toDTO(CharacterGender entity) {
    return new CharacterGenderDto(
        entity.getId(),
        entity.getGender(),
        entity.isDeleted());
  }

  public static CharacterGender toEntity(CharacterGenderDto dto) {
    return CharacterGender.builder()
        .id(dto.id())
        .gender(dto.gender())
        .isDeleted(dto.isDeleted())
        .build();
  }

}
