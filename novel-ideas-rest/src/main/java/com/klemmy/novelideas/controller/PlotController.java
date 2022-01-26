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
@RequestMapping(value = "/plot", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Plot", description = "TODO: A plot in the novel or script")
public class PlotController {

  // TODO
  @GetMapping("/novel")
  @Operation(summary = "Get all Plots for a novel", description = "List all the plots for a novel or script")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getAll() {
    return Collections.emptyList();
  }

  // TODO
  @GetMapping("/{id}")
  @Operation(summary = "Get a plot", description = "List a plots details")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<String> getNovels() {
    return Collections.emptyList();
  }
}
