package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.dto.CharacterImportanceFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.CharacterImportance;
import com.klemmy.novelideas.jpa.repository.CharacterImportanceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CharacterImportanceServiceTest {

    @Mock
    CharacterImportanceRepository repository = mock(CharacterImportanceRepository.class);

    private final CharacterImportanceService service = new CharacterImportanceService(repository);

    @Test
    void loadAll__validData__success() {
        List<CharacterImportance> characterImportances = List.of(TestEntities.characterImportanceBuilder().build(), TestEntities.characterImportanceBuilder2().build());
        when(repository.findByIsDeletedFalse()).thenReturn(characterImportances);

        List<CharacterImportanceDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterImportances.stream().map(CharacterImportanceFactory::toDTO).toList());
    }

    @Test
    void loadAll__emptyData__success() {
        List<CharacterImportance> characterImportances = new ArrayList<>();
        when(repository.findByIsDeletedFalse()).thenReturn(characterImportances);

        List<CharacterImportanceDto> result = service.loadAll();

        assertThat(result).usingRecursiveComparison().isEqualTo(characterImportances.stream().map(CharacterImportanceFactory::toDTO).toList());
        assertThat(result).isEmpty();
    }

    @Test
    void loadCharacterImportance__validData__success() throws FindDataException {
        CharacterImportance characterImportance = TestEntities.characterImportanceBuilder().build();
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(characterImportance));

        CharacterImportanceDto result = service.loadCharacterImportance(TestEntities.GENERIC_ID);

        assertThat(result.importance()).isEqualTo(TestEntities.GENERIC_VALUE);
    }

    @Test
    void loadCharacterImportance__inValidData__failure() {
        Optional<CharacterImportance> characterImportance = Optional.empty();
        when(repository.findById(anyLong())).thenReturn(characterImportance);

        assertThatThrownBy(() -> service.loadCharacterImportance(null)).isInstanceOf(FindDataException.class)
                .hasMessage(String.format("Could not find Character Importance with id:%d.", null));
    }

    @Test
    void create__validData__success() {
        CharacterImportanceDto create = TestEntities.characterImportanceDtoCreateBuilder();
        CharacterImportance characterImportance = TestEntities.characterImportanceBuilder().build();
        when(repository.save(any(CharacterImportance.class))).thenReturn(characterImportance);

        CharacterImportanceDto result = service.create(create);

        assertThat(result.importance()).isEqualTo(create.importance());
        assertThat(result.id()).isEqualTo(TestEntities.GENERIC_ID);
        verify(repository).save(any(CharacterImportance.class));
    }

//    @Test
//    void create__inValidData__failure() {
//        CharacterImportanceDto badDto =  CharacterImportanceDto.builder().build();
//        CharacterImportance bad =  CharacterImportance.builder().build();
//     //   CharacterImportance characterImportance = TestEntities.characterImportanceBuilder().build();
//
//        when(CharacterImportanceFactory.toDTO(bad)).thenThrow(new NullPointerException());
//       // assertThatNullPointerException().isThrownBy(() -> CharacterImportanceFactory.toDTO(any(CharacterImportance.class)));
//        NullPointerException throwable = assertThrows(NullPointerException.class,()-> { CharacterImportanceFactory.toDTO(bad);
//        });
//       // doThrow(new NullPointerException()).when((service).create(bad)).CharacterImportanceFactory.toDTO(any(CharacterImportance.class);
//
//        CharacterImportanceDto result = service.create(badDto);
//        //service.create(bad);
//     //   assertThatNullPointerException().isThrownBy(() -> CharacterImportanceFactory.toDTO(any(CharacterImportance.class)));
//      //  assertEquals(NullPointerException.class, throwable.getClass());
//
//        verify(repository,never() ).save(any(CharacterImportance.class));
//    }


    @Test
    void delete__validData__success() throws FindDataException {
        CharacterImportance characterImportance = TestEntities.characterImportanceBuilder().build();
        when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.ofNullable(characterImportance));

        service.delete(TestEntities.GENERIC_ID);

        verify(repository).delete(any(CharacterImportance.class));
    }

    @Test
    void delete__inValidData__failure() {
        Optional<CharacterImportance> characterImportanceEmpty = Optional.empty();
        when(repository.findById(anyLong())).thenReturn(characterImportanceEmpty);

        assertThatThrownBy(() -> service.delete(TestEntities.NOT_GENERIC_ID))
                .isInstanceOf(FindDataException.class)
                .hasMessage(String.format("Could not find Character Importance with id:%d, to delete.", TestEntities.NOT_GENERIC_ID));

        verify(repository, never()).delete(any(CharacterImportance.class));

    }
}
