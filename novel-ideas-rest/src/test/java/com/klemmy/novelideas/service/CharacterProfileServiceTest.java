package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.dto.CharacterProfileFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.CharacterProfileRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterProfileServiceTest {

  @Mock
  CharacterProfileRepository repository = mock(CharacterProfileRepository.class);

  private final CharacterProfileService service = new CharacterProfileService(repository);

  @Test
  void loadAll__validData__Success() {
    List<CharacterProfile> characterProfiles = List.of(TestEntities.characterProfileBuilder().build(), TestEntities.characterProfileBuilder2().build());
    when(repository.findAll()).thenReturn(characterProfiles);

    List<CharacterProfileDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(characterProfiles.stream().map(CharacterProfileFactory::toDTO).collect(Collectors.toList()));
  }

  @Test
  void loadAll__emptyData__Success() {
    List<CharacterProfile> characterProfiles = new ArrayList<>();
    when(repository.findAll()).thenReturn(characterProfiles);

    List<CharacterProfileDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(characterProfiles.stream().map(CharacterProfileFactory::toDTO).collect(Collectors.toList()));
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