package com.klemmy.novelideas.jpa;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.jpa.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("oracle")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:BookTestData.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:BookDataCleanup.sql")
})
class BookSpecificationTest {

  @Autowired
  BookRepository bookRepository;

  @Container
  private static final OracleContainer oracle = new OracleContainer("gvenzl/oracle-free:slim-faststart");

  @Test
  void top_level_container_should_be_running() {
    assertThat(oracle.isRunning()).isTrue();
  }

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
   // Set<BookState> noMatch = Collections.singleton(BookState.ARCHIVED);
    BookState noMatch = BookState.ARCHIVED;

    Page<Book> result = bookRepository.findAllByFilters(null, null, null, noMatch, page);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @WithMockUser
  void findAllByFilters__allFilters__success() {
    Pageable page = PageRequest.of(0, 5);
    LocalDateTime oneMonthEarlier = TestEntities.PAST_DATETIME.minusMonths(1);
    LocalDateTime oneMonthLater = TestEntities.PAST_DATETIME.plusMonths(1);
    BookState state = BookState.ACTIVE;

    Page<Book> result = bookRepository.findAllByFilters("harry potter", oneMonthEarlier, oneMonthLater, state, page);

    assertThat(result.getContent()).isNotEmpty()
        .hasSize(2)
        .satisfiesExactly(
            item1 -> assertThat(item1.getName()).contains("Chamber of Secrets"),
            item2 -> assertThat(item2.getName()).contains("Deathly Hallows")
        );
  }

}
