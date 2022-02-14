package com.klemmy.novelideas.jpa.specification;

import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.jpa.Book;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class FilterBookSpecification {

  public Specification<Book> isLikeTitle(final String query) {

    if (query == null || query.isEmpty()) {
      return (root, criteriaQuerey, criteriaBuilder) -> null;
    }
    return (root, criteriaQuerey, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query.toLowerCase() + "%");
  }

  public Specification<Book> hasState(final Set<BookState> state) {

    if (state == null || state.isEmpty()) {
      return (root, criteriaQuerey, criteriaBuilder) -> null;
    }
    return (root, criteriaQuerey, criteriaBuilder) -> criteriaBuilder.equal(
        root.get("state"), state);
  }

  public Specification<Book> isBetweenDate(final LocalDateTime start, final LocalDateTime end) {

    if (start == null || end == null) {
      return (root, criteriaQuerey, criteriaBuilder) -> null;
    }
    return (root, criteriaQuerey, criteriaBuilder) -> criteriaBuilder.between(
        root.get("startDate"), start, end);

  }

}
