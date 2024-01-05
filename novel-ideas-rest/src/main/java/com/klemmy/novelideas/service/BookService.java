package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.dto.BookFactory;
import com.klemmy.novelideas.dto.CharacterProfileFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.BookRepository;
//import com.klemmy.novelideas.producer.MessageBus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

  private final String MSG = "Could not find Book with id:%d%s";

  private final BookRepository bookRepository;

//  private final MessageBus messageBus;

  public Page<BookDto> loadAll(String queryTitle, LocalDateTime startDate, LocalDateTime endDate,
                               BookState state, Pageable pageable) {
    return bookRepository.findAllByFilters(queryTitle, startDate, endDate, state, pageable)
        .map(BookFactory::toDTO);
  }

  public BookDto loadBook(Integer id) throws FindDataException {
    Optional<Book> book = bookRepository.findById(id);
    BookDto bookDto = BookFactory.toDTO(book.orElseThrow(
        () -> new FindDataException(String.format(MSG, id, "."))));
//    messageBus.sendMessage(bookDto);
    return bookDto;
  }

  public BookDto create(BookDto bookDto) {
    Book book = BookFactory.toEntity(bookDto);
    return BookFactory.toDTO(bookRepository.save(book));
  }

  public BookDto update(BookDto bookDto) throws FindDataException {
    Optional<Book> book = bookRepository.findById(bookDto.getId());
    if (book.isPresent()) {
      Book updatedBook = BookFactory.toEntity(bookDto);
      return BookFactory.toDTO(bookRepository.save(updatedBook));
    } else {
      throw new FindDataException(String.format(MSG, bookDto.getId(), ", to update."));
    }
  }

  public void delete(Integer id) throws FindDataException {
    Optional<Book> book = bookRepository.findById(id);
    bookRepository.delete(book.orElseThrow(
        () -> new FindDataException(String.format(MSG, id, ", to delete."))));
  }

  public List<CharacterProfileDto> getBookCharacter(Integer id) throws FindDataException {
    Optional<List<CharacterProfile>> characterProfile = bookRepository.findBookById(id);
    return characterProfile.orElseThrow(() -> new FindDataException(String.format(MSG, id, ".")))
        .stream()
        .map(CharacterProfileFactory::toDTO)
        .collect(Collectors.toList());

  }

  public BookDto addCharacter(Integer id, CharacterProfileDto characterProfileDto) throws FindDataException {
    Optional<Book> book = bookRepository.findById(id);
    Book updated = book.orElseThrow(() -> new FindDataException(String.format(MSG, id, ".")));
    List<CharacterProfile> characterProfileList = updated.getCharacterProfiles() != null ? updated.getCharacterProfiles() : new ArrayList<>();
    characterProfileList.add(CharacterProfileFactory.toEntity(characterProfileDto));
    updated.setCharacterProfiles(characterProfileList);
    return BookFactory.toDTO(bookRepository.save(updated));

  }

  public BookDto removeCharacter(Integer bookId, Integer characterProfileId) throws FindDataException {
    Optional<Book> book = bookRepository.findById(bookId);
    Book updated = book.orElseThrow(() -> new FindDataException(String.format(MSG, bookId, ".")));
    List<CharacterProfile> characterProfileList = updated.getCharacterProfiles() != null ? updated.getCharacterProfiles() : new ArrayList<>();
    //TODO UnsupportedOperationException
    characterProfileList.removeIf(chId -> chId.getId().equals(characterProfileId));
    updated.setCharacterProfiles(characterProfileList);
    return BookFactory.toDTO(bookRepository.save(updated));

  }
}
