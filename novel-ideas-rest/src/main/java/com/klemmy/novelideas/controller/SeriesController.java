package com.klemmy.novelideas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/series", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Series", description = "TODO: A series of the novel or script")
public class SeriesController {

  // TODO
  @GetMapping("/")
  @Operation(summary = "Get all Series", description = "List all the Series that have novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getAll() {
    return Collections.emptyList();
  }

  // TODO
  @GetMapping("/{id}")
  @Operation(summary = "Get the Books/Novels in a Series", description = "List all the Books/Novels in a Series")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getNovels() {
    return Collections.emptyList();
  }
}
