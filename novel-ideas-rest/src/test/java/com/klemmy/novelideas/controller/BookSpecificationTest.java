package com.klemmy.novelideas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.dto.BookFactory;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.repository.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db")
class BookSpecificationTest {
/*
I tried to do this test in the JPA module and was getting errors about ApplicationContext
I then move the test to the controller.
I created a new yaml profile "db" that runs up the flyway and connect to a H2 in-memory database
So nothing is mocked (except the mockMvC)
In "Try 1" I tried writing (save) to the repository but got a null error for id.
So I when the full flow, through the controller "Try 2".

I tried using the POST call of the controller to add data to the database.
I got the same error as "try 1". Grrrrr.....

The error is when I'm adding data in the `setUp()` method

This is the error I get:

2022-02-20 09:06:14.159  WARN 1358269 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 23502, SQLState: 23502
2022-02-20 09:06:14.160 ERROR 1358269 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : NULL not allowed for column "id"; SQL statement:
insert into book (id, description, name, start_date, state) values (null, ?, ?, ?, ?) [23502-210]

org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.springframework.dao.DataIntegrityViolationException:
could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
...
...
Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: NULL not allowed for column "id"; SQL statement:
insert into book (id, description, name, start_date, state) values (null, ?, ?, ?, ?) [23502-210]

 */


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  BookRepository bookRepository;


  @WithMockUser
  @BeforeEach
  void setUp() throws Exception {
	 
	 /*
	  * I don't know if you've used this at all but I find it very helpful when debugging h2
	  * If you start the web server you can then connect to the web interface for the h2 database.
	  * It's what allowed me to see that the flyway scripts wern't being applied
	  * 
	  * You can access it at http://localhost:8082/
	  */
	 Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
	  
     Book book = TestEntities.bookNewBuilder().build();  // with id = null which it should be on create
     bookRepository.save(book);

  }
  
  /*
  I know this does not test the specification it was there to get things working when I was getting errors
  
  Like you mention this doesn't actually test the specification but it does show a working call to the
  in memory h2 database.
   */
	@Test
	@WithMockUser
	void getId__validData__success() throws Exception {

		Book hp2 = TestEntities.bookNewBuilder().name("Harry Potter and the Chamber of Secrets").build();
		Book hp7 = TestEntities.bookNewBuilder().name("Harry Potter and the Deathly Hallows").build();
		Book a1 = TestEntities.bookNewBuilder().name("Alice in Wonderland").build();

		bookRepository.save(hp2);
		bookRepository.save(hp7);
		bookRepository.save(a1);

		List<Book> books = bookRepository.findAll();
		assertThat(books).isNotEmpty();
		assertThat(books.size()).isEqualTo(4);

		Pageable page = PageRequest.of(0, 5);
		Page<Book> harryBooks = bookRepository.findAllByFilters("harry potter", null, null, null, page);

		assertThat(harryBooks.getContent()).isNotEmpty();
		assertThat(harryBooks.getContent().size()).isEqualTo(2);
		
		assertThat(harryBooks.getContent()).allSatisfy(i -> assertThat(i.getName().contains("harry potter")));
		
//		harryBooks.toList().stream().map(b->b.getName()).collect(Collectors.toList());
//		
//		harryBooks.flatMap(b->b.getName())
//
//		List<Book> hpBooks = List.of(hp2, hp7);
//		Page<Book> hpBooksPaged = new PageImpl<>(hpBooks);
//
//		assertThat(harryBooks).usingRecursiveComparison().isEqualTo(hpBooksPaged);
	}

//  @Test
//  @WithMockUser
//  void getAll__withInputs__success() throws Exception {
//    Page<BookDto> dtoList = new PageImpl<>(Collections.singletonList(TestEntities.bookDtoBuilder().build()));
//    LocalDateTime oneMonthEarlier = TestEntities.PAST_DATETIME.minusMonths(1);
//    LocalDateTime oneMonthLater = TestEntities.PAST_DATETIME.plusMonths(1);
//    Set<BookState> state = Collections.singleton(BookState.ACTIVE);
//
//    this.mockMvc.perform(get("/book/")
//            .param("queryTitle", TestEntities.GENERIC_VALUE)
//            .param("startDate", oneMonthEarlier.toString())
//            .param("endDate", oneMonthLater.toString())
//            .param("state", "ACTIVE")
//            .param("pageable", String.valueOf(PageRequest.of(0, 5)))
//        )
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
//        .andExpect(jsonPath("$.content[0].id").value(dtoList.getContent().get(0).getId())); // Not needed, just a different way to validate json data
//  }
}
