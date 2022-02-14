package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.dto.CharacterProfileFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.CharacterProfileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterProfileServiceTest {

  @Mock
  CharacterProfileRepository repository = mock(CharacterProfileRepository.class);

  private final CharacterProfileService service = new CharacterProfileService(repository);

  @Test
  void loadAll__noParams__success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfile> characterProfiles = List.of(TestEntities.characterProfileBuilder().build(), TestEntities.characterProfileBuilder2().build());
    Page<CharacterProfile> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(characterPaged);

    Page<CharacterProfileDto> result = service.loadAll(null, null, null, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged.map(CharacterProfileFactory::toDTO));
  }

  @Test
  void loadAll__validData__Success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfile> characterProfiles = List.of(TestEntities.characterProfileBuilder().build());
    Page<CharacterProfile> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(anyString(), anyString(), anyString(), any(Pageable.class)))
        .thenReturn(characterPaged);

    Page<CharacterProfileDto> result = service.loadAll(TestEntities.SURNAME,
        TestEntities.GENERIC_VALUE, TestEntities.GENERIC_VALUE, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged.map(CharacterProfileFactory::toDTO));
  }

  @Test
  void loadAll__emptyData__Success() {
    Pageable page = PageRequest.of(0, 5);
    List<CharacterProfile> characterProfiles = new ArrayList<>();
    Page<CharacterProfile> characterPaged = new PageImpl<>(characterProfiles);
    when(repository.findAllByFilters(eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(characterPaged);

    Page<CharacterProfileDto> result = service.loadAll(null, null, null, page);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterPaged.map(CharacterProfileFactory::toDTO));
    assertThat(result).isEmpty();
  }

  @Test
  void loadCharacterProfile__validData__Success() throws FindDataException {
    CharacterProfile characterProfile = TestEntities.characterProfileBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(characterProfile));

    CharacterProfileDto result = service.loadCharacterProfile(TestEntities.GENERIC_ID);

    assertThat(result.getCharacterName()).isEqualTo(TestEntities.CHAR_NAME);
  }

  @Test
  void loadCharacterProfile__inValidData__failure() {
    Optional<CharacterProfile> characterProfile = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(characterProfile);

    assertThatThrownBy(() -> service.loadCharacterProfile(null)).isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Character Profile with id:%d.", null));
  }

  @Test
  void create__validData__Success() {
    CharacterProfileDto create = TestEntities.characterProfileDtoCreateBuilder().build();
    CharacterProfile characterProfile = TestEntities.characterProfileBuilder().build();
    when(repository.save(any(CharacterProfile.class))).thenReturn(characterProfile);

    CharacterProfileDto result = service.create(create);

    assertThat(result.getCharacterName()).isEqualTo(TestEntities.CHAR_NAME);
    assertThat(result.getId()).isEqualTo(TestEntities.GENERIC_ID);
    verify(repository).save(any(CharacterProfile.class));
  }

  @Test
  void delete__validData__success() throws FindDataException {
    CharacterProfile characterProfile = TestEntities.characterProfileBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.ofNullable(characterProfile));

    service.delete(TestEntities.GENERIC_ID);

    verify(repository).delete(any(CharacterProfile.class));
  }

  @Test
  void delete__inValidData__failure() {
    Optional<CharacterProfile> characterProfileEmpty = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(characterProfileEmpty);

    assertThatThrownBy(() -> service.delete(TestEntities.NOT_GENERIC_ID))
        .isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Character Profile with id:%d, to delete.", TestEntities.NOT_GENERIC_ID));

    verify(repository, never()).delete(any(CharacterProfile.class));

  }


}