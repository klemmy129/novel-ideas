package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db")
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:BookTestData.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:BookDataCleanup.sql")
})
class BookSpecificationTest {

  @Autowired
  BookRepository bookRepository;

  @Test
  @WithMockUser
  void findAll__validData__success() {
    List<Book> books = bookRepository.findAll();

    assertThat(books).isNotEmpty()
        .hasSize(4);
    assertThat(books.stream().findAny().get().getName()).isEqualTo("AAAA");
  }

  @Test
  @WithMockUser
  void findAllByFilters__nullFilters__success() {
    Pageable page = PageRequest.of(0, 5);

    Page<Book> result = bookRepository.findAllByFilters(null, null, null, null, page);

    assertThat(result.getContent()).isNotEmpty()
        .hasSize(4);
    assertThat(result.getContent().get(0).getId()).isEqualTo(901);
  }

  @Test
  @WithMockUser
  void findAllByFilters__noMatchFilters__success() {
    Pageable page = PageRequest.of(0, 5);
    Set<BookState> noMatch = Collections.singleton(BookState.ARCHIVED);

    Page<Book> result = bookRepository.findAllByFilters(null, null, null, noMatch, page);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @WithMockUser
  void findAllByFilters__allFilters__success() {
    Pageable page = PageRequest.of(0, 5);
    LocalDateTime oneMonthEarlier = TestEntities.PAST_DATETIME.minusMonths(1);
    LocalDateTime oneMonthLater = TestEntities.PAST_DATETIME.plusMonths(1);
    Set<BookState> state = Collections.singleton(BookState.ACTIVE);

    Page<Book> result = bookRepository.findAllByFilters("harry potter", oneMonthEarlier, oneMonthLater, state, page);

    assertThat(result.getContent()).isNotEmpty()
        .hasSize(2)
        .allSatisfy(i -> assertThat(i.getName().contains("harry potter")));
  }

}
