package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.dto.CharacterImportanceFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterImportance;
import com.klemmy.novelideas.jpa.repository.CharacterImportanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CharacterImportanceService {

  private final CharacterImportanceRepository characterImportanceRepository;

  public List<CharacterImportanceDto> loadAll() {
    return characterImportanceRepository.findByIsDeletedFalse()
        .stream()
        .map(CharacterImportanceFactory::toDTO)
        .collect(Collectors.toList());
  }

  public CharacterImportanceDto loadCharacterImportance(Integer id) throws FindDataException {
    Optional<CharacterImportance> characterImportance = characterImportanceRepository.findById(id);
    return CharacterImportanceFactory.toDTO(characterImportance.orElseThrow(() -> new FindDataException(
        String.format("Could not find Character Importance with id:%d.", id))));
  }

  public CharacterImportanceDto create(CharacterImportanceDto characterImportanceDto) {
    CharacterImportance characterImportance = CharacterImportanceFactory.toEntity(characterImportanceDto);
    return CharacterImportanceFactory.toDTO(characterImportanceRepository.save(characterImportance));
  }

  public void delete(Integer id) throws FindDataException {
    Optional<CharacterImportance> characterImportance = characterImportanceRepository.findById(id);
    characterImportanceRepository.delete(characterImportance.orElseThrow(
        () -> new FindDataException(String.format("Could not find Character Importance with id:%d, to delete.", id))));
  }

}
