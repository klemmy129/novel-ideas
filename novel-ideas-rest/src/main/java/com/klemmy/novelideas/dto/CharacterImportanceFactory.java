package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.jpa.CharacterImportance;
import org.springframework.stereotype.Component;

@Component
public class CharacterImportanceFactory {

  public static CharacterImportanceDto toDTO(CharacterImportance entity) {
    return new CharacterImportanceDto(
        entity.getId(),
        entity.getImportance(),
        entity.isDeleted());

  }

  public static CharacterImportance toEntity(CharacterImportanceDto dto) {
    return new CharacterImportance(dto.id(),dto.importance(),dto.isDeleted());

  }

}
