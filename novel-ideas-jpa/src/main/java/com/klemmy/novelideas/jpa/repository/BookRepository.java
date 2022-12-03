package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.specification.FilterBookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

  void delete(Book book);

  Optional<List<CharacterProfile>> findBookById(Integer id);

  default Page<Book> findAllByFilters(final String queryTitle,
                                      final LocalDateTime startDate,
                                      final LocalDateTime endDate,
                                      final BookState state,
                                      final Pageable page) {
    return findAll(where(FilterBookSpecification.isLikeTitle(queryTitle))
        .and(FilterBookSpecification.isBetweenDate(startDate, endDate))
        .and(FilterBookSpecification.hasState(state)), page);
  }

}
