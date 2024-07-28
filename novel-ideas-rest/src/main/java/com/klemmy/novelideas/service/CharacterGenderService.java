package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import com.klemmy.novelideas.database.CharacterGenderDao;
import com.klemmy.novelideas.error.FindDataException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.util.List;

@Service
@Validated
public class CharacterGenderService {

  private final CharacterGenderDao characterGenderDao;

  public CharacterGenderService(CharacterGenderDao characterGenderDao) {
    this.characterGenderDao = characterGenderDao;

  }

  public List<CharacterGenderDto> loadAll() {
    return characterGenderDao.findAll()
        .stream()
        .toList();
  }

  public CharacterGenderDto loadGender(Long id) throws FindDataException {
    try {
      return characterGenderDao.findById(id);
    } catch (DataAccessException e) {
     // throw new FindDataException(String.format("Could not find Gender with id %d.", id));
      throw new FindDataException(id, e.getMessage());
    }
  }

  @Transactional
  @Validated(OnCreate.class)
  public CharacterGenderDto create(@Valid CharacterGenderDto characterGenderDto) {
    Long id = characterGenderDao.create(characterGenderDto);
    return characterGenderDao.findById(id);
  }

  @Transactional
  @Validated(OnUpdate.class)
  public CharacterGenderDto update(@Valid CharacterGenderDto characterGenderDto) throws DataAccessException, FindDataException {
    characterGenderDao.update(characterGenderDto, characterGenderDto.id());
    return characterGenderDao.findById(characterGenderDto.id());
  }

  @Transactional
  public void delete(Long id) throws FindDataException {
    characterGenderDao.delete(id);
  }

}
