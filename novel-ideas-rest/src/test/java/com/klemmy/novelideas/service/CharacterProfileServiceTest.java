package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.CharacterProfileGridDto;
import com.klemmy.novelideas.database.CharacterProfileDao;
import com.klemmy.novelideas.error.FindDataException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterProfileServiceTest {

  @Mock
  CharacterProfileDao repository = mock(CharacterProfileDao.class);

  private final CharacterProfileService service = new CharacterProfileService(repository);

  @Test
  void loadAll__noParams__success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfileGridDto> characterProfiles = List.of(TestEntities.characterProfileFlatDtoBuilder().build(), TestEntities.characterProfileFlatDtoBuilder2().build());
    Page<CharacterProfileGridDto> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(characterPaged);

    Page<CharacterProfileGridDto> result = service.loadAll(null, null, null, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged);
  }

  @Test
  void loadAll__validData__Success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfileGridDto> characterProfiles = List.of(TestEntities.characterProfileFlatDtoBuilder().build());
    Page<CharacterProfileGridDto> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(anyString(), anyString(), anyString(), any(Pageable.class)))
        .thenReturn(characterPaged);

    Page<CharacterProfileGridDto> result = service.loadAll(TestEntities.SURNAME,
        TestEntities.GENERIC_VALUE, TestEntities.GENERIC_VALUE, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged);
  }

  @Test
  void loadAll__emptyData__Success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfileGridDto> characterProfiles = new ArrayList<>();
    Page<CharacterProfileGridDto> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(characterPaged);

    Page<CharacterProfileGridDto> result = service.loadAll(null, null, null, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged);
    assertThat(result).isEmpty();
  }

  @Test
  void loadCharacterProfile__validData__Success() throws FindDataException {
    CharacterProfileGridDto characterProfile = TestEntities.characterProfileFlatDtoBuilder().build();
    ;
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(characterProfile);

    CharacterProfileGridDto result = service.loadCharacterProfile(TestEntities.GENERIC_ID);

    assertThat(result.getCharacterName()).isEqualTo(TestEntities.CHAR_NAME);
  }

  @Test
  void loadCharacterProfile__inValidData__failure() throws FindDataException {
    doThrow(FindDataException.class).when(repository).findById(TestEntities.NOT_GENERIC_ID);

    assertThrows(FindDataException.class, () -> service.loadCharacterProfile(TestEntities.NOT_GENERIC_ID));
  }

  @Test
  void loadCharacterProfile__inValidData2__failure() throws FindDataException {
    doThrow(FindDataException.class).when(repository).findById(null);

    assertThrows(FindDataException.class, () -> service.loadCharacterProfile(null));
  }

  @Test
  void create__validData__Success() throws FindDataException {
    CharacterProfileDto create = TestEntities.characterProfileDtoCreateBuilder().build();
    CharacterProfileGridDto characterProfile = TestEntities.characterProfileFlatDtoBuilder().build();
    when(repository.create(any(CharacterProfileDto.class))).thenReturn(TestEntities.GENERIC_ID);
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(characterProfile);

    CharacterProfileGridDto result = service.create(create);

    assertThat(result.getCharacterName()).isEqualTo(TestEntities.CHAR_NAME);
    assertThat(result.getId()).isEqualTo(TestEntities.GENERIC_ID);
    verify(repository).create(any(CharacterProfileDto.class));
  }

  @Test
  void delete__validData__success() throws FindDataException {

    service.delete(TestEntities.GENERIC_ID);

    verify(repository).delete(TestEntities.GENERIC_ID);
  }

  @Test
  void delete__inValidData__failure() throws FindDataException {
    doThrow(FindDataException.class).when(repository).delete(TestEntities.NOT_GENERIC_ID);

    assertThrows(FindDataException.class, () -> service.delete(TestEntities.NOT_GENERIC_ID));
  }

}
