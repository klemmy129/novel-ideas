package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.dto.CharacterGenderFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterGender;
import com.klemmy.novelideas.jpa.repository.CharacterGenderRepository;
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

class CharacterGenderServiceTest {

    @Mock
    CharacterGenderRepository repository = mock(CharacterGenderRepository.class);

    private final CharacterGenderService service = new CharacterGenderService(repository);

    @Test
    void loadAll__validData__success() {
        List<CharacterGender> characterGenders = List.of(TestEntities.characterGenderBuilder().build(),
            TestEntities.characterGenderBuilder2().build());
        when(repository.findByIsDeletedFalse()).thenReturn(characterGenders);

        List<CharacterGenderDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterGenders.stream()
            .map(CharacterGenderFactory::toDTO).collect(Collectors.toList()));
    }

    @Test
    void loadAll__emptyData__success() {
        List<CharacterGender> characterGenders = new ArrayList<>();
        when(repository.findByIsDeletedFalse()).thenReturn(characterGenders);

        List<CharacterGenderDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterGenders.stream()
            .map(CharacterGenderFactory::toDTO).collect(Collectors.toList()));
        assertThat(result).isEmpty();
    }

    @Test
    void loadGender__validData__success() throws FindDataException {
        CharacterGender characterGender = TestEntities.characterGenderBuilder().build();
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(characterGender));

        CharacterGenderDto result = service.loadGender(TestEntities.GENERIC_ID);

        assertThat(result.getGender()).isEqualTo(TestEntities.GENDER_MALE);
    }

    @Test
    void loadGender__inValidData__failure() {
        Optional<CharacterGender> gender = Optional.empty();
        when(repository.findById(anyInt())).thenReturn(gender);

        assertThatThrownBy(() -> service.loadGender(null)).isInstanceOf(FindDataException.class)
                .hasMessage(String.format("Could not find Gender with id:%d.", null));
    }

    @Test
    void create__validData__success() {
        CharacterGenderDto create = TestEntities.characterGenderDtoCreateBuilder().build();
        CharacterGender characterGender = TestEntities.characterGenderBuilder().build();
        when(repository.save(any(CharacterGender.class))).thenReturn(characterGender);

        CharacterGenderDto result = service.create(create);

        assertThat(result.getGender()).isEqualTo(create.getGender());
        assertThat(result.getId()).isEqualTo(TestEntities.GENERIC_ID);
        verify(repository).save(any(CharacterGender.class));
    }

//    @Test
//    void create__idOnCreate__failure() {
//        GenderDto createWithId = TestEntities.genderDtoBuilder().build();
//       // create.setId(TestEntities.GENERIC_ID);
//        Gender gender = TestEntities.genderBuilder().build();
//        when(repository.save(any(Gender.class))).thenReturn(gender);
//
//       // GenderDto result = service.create(create);
//
//        assertThrows(ConstraintViolationException.class, () -> {
//            service.create(createWithId);
//        });
//        verify(repository).save(any(Gender.class));
//    }

//    @Test
//    void create__inValidData__failure() {
//        GenderDto bad =  GenderDto.builder().build();
//        Gender gender = TestEntities.genderBuilder().build();
//        when(repository.save(any(Gender.class))).thenThrow(ConstraintValidationException.class);
//
//        GenderDto result = service.create(bad);
//
//        assertThatIllegalArgumentException().isThrownBy(() -> service.create(bad))
//                .withMessage(null);
//        verify(repository, never()).save(any(Gender.class));
//    }

    @Test
    void update__validData__success() throws FindDataException {
        CharacterGenderDto update = TestEntities.characterGenderDtoBuilder().build();
        CharacterGender characterGender = TestEntities.characterGenderBuilder().build();
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(characterGender));
        when(repository.save(any(CharacterGender.class))).thenReturn(characterGender);

        CharacterGenderDto result = service.update(update);

        verify(repository).findById(anyInt());
        assertThat(result).usingRecursiveComparison().isEqualTo(update);
        verify(repository).save(any(CharacterGender.class));
    }

//    @Test
//    void update__IdNull__success() throws FindDataException {
//        GenderDto update = TestEntities.genderDtoBuilder().build();
//        update.setId(null);
//        Gender gender = TestEntities.genderBuilder().build();
//        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(gender));
//        when(repository.save(any(Gender.class))).thenReturn(gender);
//
//        assertThrows(ConstraintViolationException.class, () -> {
//            service.update(update);
//        });
//       // GenderDto result = service.update(update);
//
//        verify(repository, never()).save(any(Gender.class));
//    }

    @Test
    void update__inValidData__failure() {
        CharacterGenderDto bad = CharacterGenderDto.builder().build();
        bad.setId(TestEntities.NOT_GENERIC_ID);
        Optional<CharacterGender> gender = Optional.empty();
        when(repository.findById(anyInt())).thenReturn(gender);

        assertThatThrownBy(() -> service.update(bad))
                .hasMessage(String.format("Could not find Gender with id:%d, to update.", bad.getId()));

        verify(repository).findById(anyInt());
        verify(repository, never()).save(any(CharacterGender.class));

    }

    @Test
    void update__nullData__failure() {
        CharacterGenderDto bad = CharacterGenderDto.builder().build();
        Optional<CharacterGender> gender = Optional.empty();
        when(repository.findById(anyInt())).thenReturn(gender);

        assertThatThrownBy(() -> service.update(bad))
                .isInstanceOf(FindDataException.class)
                .hasMessage(String.format("Could not find Gender with id:%d, to update.", bad.getId()));
        verify(repository).findById(null);
        verify(repository, never()).save(any(CharacterGender.class));

    }

    @Test
    void delete__validData__success() throws FindDataException {
        CharacterGender characterGender = TestEntities.characterGenderBuilder().build();
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.ofNullable(characterGender));

        service.delete(TestEntities.GENERIC_ID);

        verify(repository).delete(any(CharacterGender.class));
    }

    @Test
    void delete__inValidData__failure() {
        Optional<CharacterGender> genderEmpty = Optional.empty();
        when(repository.findById(anyInt())).thenReturn(genderEmpty);

        assertThatThrownBy(() -> service.delete(TestEntities.NOT_GENERIC_ID))
                .isInstanceOf(FindDataException.class)
                .hasMessage(String.format("Could not find Gender with id:%d, to delete.", TestEntities.NOT_GENERIC_ID));

        verify(repository, never()).delete(any(CharacterGender.class));

    }
}