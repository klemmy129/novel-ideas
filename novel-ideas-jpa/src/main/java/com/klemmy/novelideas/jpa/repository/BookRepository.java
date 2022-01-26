package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

  void delete(Book book);

  Optional<List<CharacterProfile>> findBookById(Integer id);

}
