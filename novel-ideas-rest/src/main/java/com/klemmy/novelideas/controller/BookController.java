
package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/book", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book", description = "A book represents a novel or script")
public class BookController {

  private final BookService bookService;

  @GetMapping("/")
  @Operation(summary = "Get all Books", description = "List all the Books that represents a novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public Page<BookDto> getAll(@RequestParam(required = false) String queryTitle,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                              @RequestParam(required = false) BookState state,
                              @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable) {
    return bookService.loadAll(queryTitle, startDate, endDate, state, pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a Book", description = "Get a single book that represents a novel or script")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public BookDto getBook(@PathVariable Long id) throws FindDataException {
    return bookService.loadBook(id);
  }

  @GetMapping("/{id}/characters")
  @Operation(summary = "Get a Book's characters", description = "Get all characters for a single book")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public List<CharacterProfileDto> getBookCharacters(@PathVariable Long id) throws FindDataException {
    return bookService.getBookCharacter(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a Book", description = "This creates a book that represents a novel or script")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  @Validated(OnCreate.class)
  public BookDto create(@Valid @RequestBody BookDto bookDto) {
    return bookService.create(bookDto);
  }

  @PutMapping("/")
  @Operation(summary = "Update a Book", description = "Update a book that represents a novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  @Validated(OnUpdate.class)
  public BookDto update(@Valid @RequestBody BookDto bookDto) throws FindDataException {
    return bookService.update(bookDto);
  }

  @PutMapping("/{bookId}/character")
  @Operation(summary = "Add a Character to a Book",
      description = "Add a Character to a book that represents a novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public BookDto addCharacterToBook(@PathVariable @Parameter(required = true, description = "Book Id") Long bookId,
                                    @RequestBody @Parameter(required = true, description = "Character Profile") @Valid CharacterProfileDto characterProfileDto) throws FindDataException {
    return bookService.addCharacter(bookId, characterProfileDto);
  }

  @PutMapping("/{bookId}/character/{characterId}")
  @Operation(summary = "Remove a Character from a Book",
      description = "Remove a Character from a book that represents a novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public BookDto removeCharacterToBook(
      @PathVariable @Parameter(required = true, description = "Book Id") Long bookId,
      @PathVariable @Parameter(required = true, description = "Character Id") Long characterId) throws FindDataException {
    return bookService.removeCharacter(bookId, characterId);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a Book", description = "Are you sure you want to delete this or is it that bad?")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public void deleteBook(@PathVariable Long id) throws FindDataException {
    bookService.delete(id);
  }

}
