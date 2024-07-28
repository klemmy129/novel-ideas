package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.CharacterProfileGridDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.database.CharacterProfileDao;
import com.klemmy.novelideas.error.FindDataException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
public class CharacterProfileService {

  private final CharacterProfileDao characterProfileDao;

  public Page<CharacterProfileGridDto> loadAll(String queryName,
                                           String importance,
                                           String gender,
                                           Pageable pageable) {
    return characterProfileDao.findAllByFilters(queryName, importance, gender, pageable);
  }

  public CharacterProfileGridDto loadCharacterProfile(Long id) throws FindDataException {
    return characterProfileDao.findById(id);
  }

  @Transactional
  @Validated(OnCreate.class)
  public CharacterProfileGridDto create(CharacterProfileDto characterProfileDto) throws FindDataException {
    Long id = characterProfileDao.create(characterProfileDto);
    return characterProfileDao.findById(id);
  }

  @Transactional
  @Validated(OnCreate.class)
  public CharacterProfileGridDto update(CharacterProfileDto characterProfileDto,Long id) throws FindDataException {
    characterProfileDao.update(characterProfileDto, id);
    return characterProfileDao.findById(id);
  }

  public void delete(Long id) throws FindDataException {
    characterProfileDao.delete(id);
  }
}
