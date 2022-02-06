package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.BookStateDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.BookStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/book-state", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book State", description = "The State of a book eg: Active, Paused, Finished, etc")
public class BookStateController {

  private final BookStateService bookStateService;

  @GetMapping("/")
  @Operation(summary = "List all book states", description = "Get all the States")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<BookStateDto> getAll() {
    return bookStateService.loadAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a state", description = "Give me a state")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public BookStateDto getBookState(@PathVariable Integer id) throws FindDataException {
    return bookStateService.loadBookState(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a State",
      description = "Create a state that will be assigned to a book eg: Active, Paused, Finished, etc")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  @Validated(OnCreate.class)
  public BookStateDto create(@Valid @RequestBody BookStateDto bookStateDto) {
    return bookStateService.create(bookStateDto);
  }

//    @PutMapping("/")
//    @ApiResponse(responseCode = "400", description = "Invalid")
//    public ProjectStateDto update(@RequestBody @Valid ProjectStateDto projectStateDto){
//        return projectStateService.update(projectStateDto);
//    }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a state", description = "Mark a state as deleted")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public void delete(@PathVariable @Min(1) Integer id) throws FindDataException {
    bookStateService.delete(id);
  }
}
