package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.jpa.CharacterGender;
import org.springframework.stereotype.Component;

@Component
public class CharacterGenderFactory {

  public static CharacterGenderDto toDTO(CharacterGender entity) {
    return CharacterGenderDto.builder()
        .id(entity.getId())
        .gender(entity.getGender())
        .isDeleted(entity.isDeleted())
        .build();
  }

  public static CharacterGender toEntity(CharacterGenderDto dto) {
    return CharacterGender.builder()
        .id(dto.getId())
        .gender(dto.getGender())
        .isDeleted(dto.isDeleted())
        .build();
  }

}
