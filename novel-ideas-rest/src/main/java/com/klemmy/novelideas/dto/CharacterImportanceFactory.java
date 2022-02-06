package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.jpa.CharacterImportance;
import org.springframework.stereotype.Component;

@Component
public class CharacterImportanceFactory {

  public static CharacterImportanceDto toDTO(CharacterImportance entity) {
    return CharacterImportanceDto.builder()
        .id(entity.getId())
        .importance(entity.getImportance())
        .isDeleted(entity.isDeleted())
        .build();
  }

  public static CharacterImportance toEntity(CharacterImportanceDto dto) {
    return CharacterImportance.builder()
        .id(dto.getId())
        .importance(dto.getImportance())
        .isDeleted(dto.isDeleted())
        .build();
  }

}
