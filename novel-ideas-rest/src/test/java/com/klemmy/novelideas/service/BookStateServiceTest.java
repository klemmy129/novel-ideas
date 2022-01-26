package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.BookStateDto;
import com.klemmy.novelideas.dto.BookStateFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.BookState;
import com.klemmy.novelideas.jpa.repository.BookStateRepository;
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

class BookStateServiceTest {

  @Mock
  BookStateRepository repository = mock(BookStateRepository.class);

  private final BookStateService service = new BookStateService(repository);

  @Test
  void loadAll__validData__success() {
    List<BookState> bookStates = List.of(TestEntities.bookStateBuilder().build(), TestEntities.bookStateBuilder2().build());
    when(repository.findByIsDeletedFalse()).thenReturn(bookStates);

    List<BookStateDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(bookStates.stream().map(BookStateFactory::toDTO).collect(Collectors.toList()));
  }

  @Test
  void loadAll__emptyData__success() {
    List<BookState> bookStates = new ArrayList<>();
    when(repository.findByIsDeletedFalse()).thenReturn(bookStates);

    List<BookStateDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(bookStates.stream().map(BookStateFactory::toDTO).collect(Collectors.toList()));
    assertThat(result).isEmpty();
  }

  @Test
  void loadBookState__validData__success() throws FindDataException {
    BookState bookState = TestEntities.bookStateBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(bookState));

    BookStateDto result = service.loadBookState(TestEntities.GENERIC_ID);

    assertThat(result.getState()).isEqualTo(TestEntities.GENERIC_VALUE);
  }

  @Test
  void loadBookState__inValidData__failure() {
    Optional<BookState> bookState = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(bookState);

    assertThatThrownBy(() -> service.loadBookState(null)).isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Book State with id:%d.", null));
  }

  @Test
  void create__validData__success() {
    BookStateDto create = TestEntities.bookStateDtoCreateBuilder().build();
    BookState bookState = TestEntities.bookStateBuilder().build();
    when(repository.save(any(BookState.class))).thenReturn(bookState);

    BookStateDto result = service.create(create);

    assertThat(result.getState()).isEqualTo(create.getState());
    assertThat(result.getId()).isEqualTo(TestEntities.GENERIC_ID);
    verify(repository).save(any(BookState.class));
  }

  @Test
  void delete__validData__success() throws FindDataException {
    BookState bookState = TestEntities.bookStateBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.ofNullable(bookState));

    service.delete(TestEntities.GENERIC_ID);

    verify(repository).delete(any(BookState.class));
  }

  @Test
  void delete__inValidData__failure() {
    Optional<BookState> bookStateEmpty = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(bookStateEmpty);

    assertThatThrownBy(() -> service.delete(TestEntities.NOT_GENERIC_ID))
        .isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Book State with id:%d, to delete.", TestEntities.NOT_GENERIC_ID));

    verify(repository, never()).delete(any(BookState.class));

  }

}
