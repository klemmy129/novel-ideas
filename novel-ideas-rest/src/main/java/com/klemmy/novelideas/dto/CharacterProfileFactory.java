package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.jpa.CharacterProfile;
import org.springframework.stereotype.Component;

@Component
public class CharacterProfileFactory {

  public static CharacterProfileDto toDTO(CharacterProfile entity) {
    return CharacterProfileDto.builder()
        .id(entity.getId())
        .characterName(entity.getCharacterName())
        .title(entity.getTitle())
        .firstName(entity.getFirstName())
        .middleName(entity.getMiddleName())
        .surname(entity.getSurname())
        .nickName(entity.getNickName())
        .gender(CharacterGenderFactory.toDTO(entity.getCharacterGender()))
        .characterImportance(CharacterImportanceFactory.toDTO(entity.getCharacterImportance()))
        .dateOfBirth(entity.getDateOfBirth())
        .functionInStory(entity.getFunctionInStory())
        .innerGoal(entity.getInnerGoal())
        .outerGoal(entity.getOuterGoal())
        .physicalDescription(entity.getPhysicalDescription())
        .build();
  }

  public static CharacterProfile toEntity(CharacterProfileDto dto) {
    return CharacterProfile.builder()
        .id(dto.getId())
        .characterName(dto.getCharacterName())
        .title(dto.getTitle())
        .firstName(dto.getFirstName())
        .middleName(dto.getMiddleName())
        .surname(dto.getSurname())
        .nickName(dto.getNickName())
        .characterGender(CharacterGenderFactory.toEntity(dto.getGender()))
        .characterImportance(CharacterImportanceFactory.toEntity(dto.getCharacterImportance()))
        .dateOfBirth(dto.getDateOfBirth())
        .functionInStory(dto.getFunctionInStory())
        .innerGoal(dto.getInnerGoal())
        .outerGoal(dto.getOuterGoal())
        .physicalDescription(dto.getPhysicalDescription())
        .build();
  }

}
