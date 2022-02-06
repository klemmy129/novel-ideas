package com.klemmy.novelideas.service;

import com.klemmy.novelideas.api.BookStateDto;
import com.klemmy.novelideas.dto.BookStateFactory;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.jpa.BookState;
import com.klemmy.novelideas.jpa.repository.BookStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookStateService {

  private final BookStateRepository bookStateRepository;

  public List<BookStateDto> loadAll() {
    return bookStateRepository.findByIsDeletedFalse()
        .stream()
        .map(BookStateFactory::toDTO)
        .collect(Collectors.toList());
  }

  public BookStateDto loadBookState(Integer id) throws FindDataException {
    Optional<BookState> bookState = bookStateRepository.findById(id);
    return BookStateFactory.toDTO(bookState.orElseThrow(
        () -> new FindDataException(String.format("Could not find Book State with id:%d.", id))));
  }

  public BookStateDto create(BookStateDto bookStateDto) {
    BookState bookState = BookStateFactory.toEntity(bookStateDto);
    return BookStateFactory.toDTO(bookStateRepository.save(bookState));
  }

  public void delete(Integer id) throws FindDataException {
    Optional<BookState> bookState = bookStateRepository.findById(id);
    bookStateRepository.delete(bookState.orElseThrow(
        () -> new FindDataException(String.format("Could not find Book State with id:%d, to delete.", id))));
  }

}
