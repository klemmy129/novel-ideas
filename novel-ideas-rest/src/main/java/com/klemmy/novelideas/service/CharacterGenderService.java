package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import com.klemmy.novelideas.dto.CharacterGenderFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterGender;
import com.klemmy.novelideas.jpa.repository.CharacterGenderRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class CharacterGenderService {

  private final CharacterGenderRepository characterGenderRepository;

  public CharacterGenderService(CharacterGenderRepository characterGenderRepository) {
    this.characterGenderRepository = characterGenderRepository;

  }

  public List<CharacterGenderDto> loadAll() {
    return characterGenderRepository.findByIsDeletedFalse()
        .stream()
        .map(CharacterGenderFactory::toDTO)
        .collect(Collectors.toList());
  }

  public CharacterGenderDto loadGender(Integer id) throws FindDataException {
    Optional<CharacterGender> gender = characterGenderRepository.findById(id);
    return CharacterGenderFactory.toDTO(gender.orElseThrow(() -> new FindDataException(
        String.format("Could not find Gender with id:%d.", id))));
  }

  @Validated(OnCreate.class)
  public CharacterGenderDto create(@Valid CharacterGenderDto characterGenderDto) {
    CharacterGender characterGender = CharacterGenderFactory.toEntity(characterGenderDto);
    return CharacterGenderFactory.toDTO(characterGenderRepository.save(characterGender));
  }

  @Validated(OnUpdate.class)
  public CharacterGenderDto update(@Valid CharacterGenderDto characterGenderDto) throws FindDataException {
    Optional<CharacterGender> gender = characterGenderRepository.findById(characterGenderDto.id());
    if (gender.isPresent()) {
      CharacterGender updatedCharacterGender = CharacterGenderFactory.toEntity(characterGenderDto);
      return CharacterGenderFactory.toDTO(characterGenderRepository.save(updatedCharacterGender));
    } else {
      throw new FindDataException(
          String.format("Could not find Gender with id:%d, to update.", characterGenderDto.id()));
    }

  }

  public void delete(Integer id) throws FindDataException {
    Optional<CharacterGender> gender = characterGenderRepository.findById(id);
    characterGenderRepository.delete(gender.orElseThrow(
        () -> new FindDataException(String.format("Could not find Gender with id:%d, to delete.", id))));
  }

}
