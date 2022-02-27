package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.BookState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/book-state", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Book State", description = "The State of a book eg: Active, Paused, Finished, etc")
public class BookStateController {

  @GetMapping("/")
  @Operation(summary = "List all book states", description = "Get all the States")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<BookState> getAll() {
    return Arrays.asList(BookState.values());
  }

}
