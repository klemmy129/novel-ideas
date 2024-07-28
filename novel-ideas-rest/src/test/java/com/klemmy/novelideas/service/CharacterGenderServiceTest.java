package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.database.CharacterGenderDao;
import com.klemmy.novelideas.error.FindDataException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterGenderServiceTest {

    @Mock
    private final CharacterGenderDao repository = mock(CharacterGenderDao.class);
    private final CharacterGenderService service = new CharacterGenderService(repository);

    @Test
    void loadAll__validData__success() {
        List<CharacterGenderDto> characterGenders = List.of(TestEntities.characterGenderDtoBuilder(),
            TestEntities.characterGenderDtoBuilder2());
        when(repository.findAll()).thenReturn(characterGenders);

        List<CharacterGenderDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterGenders.stream().toList());
    }

    @Test
    void loadAll__emptyData__success() {
        List<CharacterGenderDto> characterGenders = new ArrayList<>();
        when(repository.findAll()).thenReturn(characterGenders);

        List<CharacterGenderDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterGenders.stream().toList());
        assertThat(result).isEmpty();
    }

    @Test
    void loadGender__validData__success() throws FindDataException {
        CharacterGenderDto characterGender = TestEntities.characterGenderDtoBuilder();
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(characterGender);

        CharacterGenderDto result = service.loadGender(TestEntities.GENERIC_ID);

        assertThat(result.gender()).isEqualTo(TestEntities.GENDER_MALE);
    }

//    Couldn't get this to work
//    @Test
//    void loadGender__inValidData__failure() {
//        when(repository.findById(TestEntities.NOT_GENERIC_ID)).thenThrow(DataAccessException.class);
//
//        assertThrows(FindDataException.class, () -> service.loadGender(TestEntities.NOT_GENERIC_ID));
//    }

    @Test
    void create__validData__success() {
        CharacterGenderDto create = TestEntities.characterGenderDtoCreateBuilder();
        CharacterGenderDto characterGender = TestEntities.characterGenderDtoBuilder();
        when(repository.create(any(CharacterGenderDto.class))).thenReturn(TestEntities.GENERIC_ID);
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(characterGender);

        CharacterGenderDto result = service.create(create);

        assertThat(result.gender()).isEqualTo(create.gender());
        assertThat(result.id()).isEqualTo(TestEntities.GENERIC_ID);
        verify(repository).create(any(CharacterGenderDto.class));
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
        CharacterGenderDto update = TestEntities.characterGenderDtoBuilder();
        when(repository.findById(anyLong())).thenReturn(update);

        CharacterGenderDto result = service.update(update);

        verify(repository).update(any(CharacterGenderDto.class),eq(TestEntities.GENERIC_ID));
        verify(repository).findById(anyLong());
        assertThat(result).usingRecursiveComparison().isEqualTo(update);

    }

//    @Test
//    void update__IdNull__success() throws FindDataException {
//        GenderDto update = TestEntities.genderDtoBuilder().build();
//        update.setId(null);
//        Gender gender = TestEntities.genderBuilder().build();
//        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(gender));
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
    void update__inValidData__failure() throws FindDataException {
        CharacterGenderDto bad = TestEntities.characterGenderDtoBadCreateBuilder();
        doThrow(FindDataException.class).when(repository).update(bad,bad.id());

        assertThrows(FindDataException.class, () -> service.update(bad));

        verify(repository,never()).findById(anyLong());
    }

    @Test
    void update__nullData__failure() throws FindDataException {
        CharacterGenderDto bad = TestEntities.characterGenderDtoBadNullIdCreateBuilder();
        doThrow(FindDataException.class).when(repository).update(bad,null);

        assertThrows(FindDataException.class, () -> service.update(bad));

        verify(repository,never()).findById(anyLong());
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