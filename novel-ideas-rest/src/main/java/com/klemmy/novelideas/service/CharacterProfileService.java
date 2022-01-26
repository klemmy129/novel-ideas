package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.dto.CharacterProfileFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.CharacterProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CharacterProfileService {

  private final CharacterProfileRepository characterProfileRepository;

  public List<CharacterProfileDto> loadAll() {
    return characterProfileRepository.findAll()
        .stream()
        .map(CharacterProfileFactory::toDTO)
        .collect(Collectors.toList());
  }

  public CharacterProfileDto loadCharacterProfile(Integer id) throws FindDataException {
    Optional<CharacterProfile> characterProfile = characterProfileRepository.findById(id);
    return CharacterProfileFactory.toDTO(characterProfile.orElseThrow(
        () -> new FindDataException(String.format("Could not find Character Profile with id:%d.", id))));
  }

  @Validated(OnCreate.class)
  public CharacterProfileDto create(CharacterProfileDto characterProfileDto) {
    CharacterProfile characterProfile = CharacterProfileFactory.toEntity(characterProfileDto);
    return CharacterProfileFactory.toDTO(characterProfileRepository.save(characterProfile));
  }

  public void delete(Integer id) throws FindDataException {
    Optional<CharacterProfile> characterProfile = characterProfileRepository.findById(id);
    characterProfileRepository.delete(characterProfile.orElseThrow(
        () -> new FindDataException(String.format("Could not find Character Profile with id:%d, to delete.", id))));
  }
}
