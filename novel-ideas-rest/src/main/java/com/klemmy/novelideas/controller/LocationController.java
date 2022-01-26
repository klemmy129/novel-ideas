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
@RequestMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Location", description = "TODO: A Location in the novel or script")
public class LocationController {

  // TODO
  @GetMapping("/")
  @Operation(summary = "Get all Locations", description = "List all the location.")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getAll() {
    return Collections.emptyList();
  }

  // TODO
  @GetMapping("/{id}")
  @Operation(summary = "Get the Books/Novels in a Series", description = "List all the BooksNovels in a Series")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getLocation() {
    return Collections.emptyList();
  }

  // TODO
  @GetMapping("/novel/{id}")
  @Operation(summary = "Get all the Locations in Novel", description = "List all the Locations in Novel")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getNovelLocations() {
    return Collections.emptyList();
  }
}
