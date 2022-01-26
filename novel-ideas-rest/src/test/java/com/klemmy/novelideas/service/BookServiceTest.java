package com.klemmy.novelideas.service;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.dto.CharacterProfileFactory;
import com.klemmy.novelideas.dto.BookFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.BookRepository;
import org.junit.jupiter.api.Disabled;
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

class BookServiceTest {
  @Mock
  BookRepository repository = mock(BookRepository.class);

  private final BookService service = new BookService(repository);

  @Test
  void loadAll__validData__success() {
    List<Book> books = List.of(TestEntities.bookBuilder().build(), TestEntities.bookBuilderNoChars().build());
    when(repository.findAll()).thenReturn(books);

    List<BookDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(books.stream().map(BookFactory::toDTO).collect(Collectors.toList()));
  }

  @Test
  void loadAll__emptyData__success() {
    List<Book> books = new ArrayList<>();
    when(repository.findAll()).thenReturn(books);

    List<BookDto> result = service.loadAll();

    assertThat(result).usingRecursiveComparison().isEqualTo(books.stream().map(BookFactory::toDTO).collect(Collectors.toList()));
    assertThat(result).isEmpty();
  }

  @Test
  void loadBook__inValidData__failure() {
    Optional<Book> book = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(book);

    assertThatThrownBy(() -> service.loadBook(null)).isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Book with id:%d.", null));
  }

  @Test
  void loadBook__validData__success() throws FindDataException {
    Book book = TestEntities.bookBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(book));

    BookDto result = service.loadBook(TestEntities.GENERIC_ID);

    assertThat(result).usingRecursiveComparison().isEqualTo(BookFactory.toDTO(book));
  }

  @Test
  void charactersInBook__validData__success() throws FindDataException {
    List<CharacterProfile> characterProfile = List.of(TestEntities.characterProfileBuilder().build(),
        TestEntities.characterProfileBuilder2().build());
    when(repository.findBookById(TestEntities.GENERIC_ID)).thenReturn(Optional.of(characterProfile));

    List<CharacterProfileDto> result = service.getBookCharacter(TestEntities.GENERIC_ID);

    assertThat(result).usingRecursiveComparison().isEqualTo(characterProfile.stream()
        .map(CharacterProfileFactory::toDTO).collect(Collectors.toList()));
  }

  @Test
  void create__validData__success() {
    BookDto create = TestEntities.bookDtoCreateBuilder().build();
    Book book = TestEntities.bookBuilder().build();
    when(repository.save(any(Book.class))).thenReturn(book);

    BookDto result = service.create(create);

    assertThat(result.getId()).isEqualTo(TestEntities.GENERIC_ID);
    assertThat(result.getName()).isEqualTo(create.getName());
    assertThat(result.getDescription()).isEqualTo(create.getDescription());
    verify(repository).save(any(Book.class));
  }

  @Test
  void update__validData__success() throws FindDataException {
    BookDto update = TestEntities.bookDtoBuilder().build();
    Book book = TestEntities.bookBuilder().build();
    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(book));
    when(repository.save(any(Book.class))).thenReturn(book);

    BookDto result = service.update(update);

    verify(repository).findById(anyInt());
    assertThat(result).usingRecursiveComparison().isEqualTo(update);
    verify(repository).save(any(Book.class));

  }

  @Test
  void update__smallDataSet__success() throws FindDataException {
    BookDto bookDto = TestEntities.bookDtoSmallerBuilder().build();
    Book book = TestEntities.bookSmallerBuilder().build();
    when(repository.findById(anyInt())).thenReturn(Optional.of(book));
    when(repository.save(any(Book.class))).thenReturn(book);

    service.update(bookDto);

    verify(repository).findById(anyInt());
    verify(repository).save(any(Book.class));

  }

  @Test
  void update__inValidId__failure() {
    BookDto bad = TestEntities.bookDtoBuilder().build();
    bad.setId(TestEntities.NOT_GENERIC_ID);
    Optional<Book> book = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(book);

    assertThatThrownBy(() -> service.update(bad))
        .hasMessage(String.format("Could not find Book with id:%d, to update.", bad.getId()));

    verify(repository).findById(anyInt());
    verify(repository, never()).save(any(Book.class));

  }

  @Test
  void update__nullData__failure() {
    BookDto bad = BookDto.builder().build();
    Optional<Book> book = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(book);

    assertThatThrownBy(() -> service.update(bad))
        .isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Book with id:%d, to update.", bad.getId()));
    verify(repository).findById(null);
    verify(repository, never()).save(any(Book.class));

  }

  @Test
  void delete__validData__success() throws FindDataException {
    Book book = TestEntities.bookBuilder().build();
    when(repository.findById(TestEntities.GENERIC_ID)).thenReturn(Optional.ofNullable(book));

    service.delete(TestEntities.GENERIC_ID);

    verify(repository).delete(any(Book.class));

  }

  @Test
  void delete__inValidData__failure() {
    Optional<Book> bookEmpty = Optional.empty();
    when(repository.findById(anyInt())).thenReturn(bookEmpty);

    assertThatThrownBy(() -> service.delete(TestEntities.NOT_GENERIC_ID))
        .isInstanceOf(FindDataException.class)
        .hasMessage(String.format("Could not find Book with id:%d, to delete.", TestEntities.NOT_GENERIC_ID));

    verify(repository, never()).delete(any(Book.class));

  }

  @Test
  void addCharacterToBook__validData__success() throws FindDataException {
    CharacterProfileDto newCharactorDto = TestEntities.characterProfileDtoBuilder2().build();
    CharacterProfile newCharactor = TestEntities.characterProfileBuilder2().build();
    Book book = TestEntities.bookBuilderNoChars().build();
    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(book));
    Book bookSaved = book.toBuilder().build();
    bookSaved.setCharacterProfiles(List.of(newCharactor));
    when(repository.save(any(Book.class))).thenReturn(bookSaved);

    BookDto result = service.addCharacter(TestEntities.GENERIC_ID, newCharactorDto);

    verify(repository).findById(anyInt());
    assertThat(result.getCharacterProfiles().stream().findFirst()).usingRecursiveComparison()
        .isEqualTo(Optional.of(newCharactorDto));
    verify(repository).save(any(Book.class));

  }

  @Disabled
  @Test
  void removeCharacterFromBook__validData__success() throws FindDataException {
    Book book = TestEntities.bookBuilder().build();
    List<CharacterProfile> characterProfileList = List.of(TestEntities.characterProfileBuilder().build());
    Book bookSaved = TestEntities.bookBuilderNoChars().build();
    bookSaved.setCharacterProfiles(characterProfileList);
    when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(book));
    when(repository.save(any(Book.class))).thenReturn(bookSaved);

    BookDto result = service.removeCharacter(TestEntities.GENERIC_ID, TestEntities.GENERIC_ID2);

    verify(repository).findById(anyInt());
    assertThat(result.getCharacterProfiles()).usingRecursiveComparison()
        .isEqualTo(characterProfileList.stream().map(CharacterProfileFactory::toDTO).collect(Collectors.toList()));
    verify(repository).save(any(Book.class));
  }

}