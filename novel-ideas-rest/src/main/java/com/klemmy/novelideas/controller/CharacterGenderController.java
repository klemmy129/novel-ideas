package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.api.OnUpdate;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterGenderService;
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
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(value = "/gender", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "CharacterGender", description = "Older applications would have hard coded Male and Female, but this will allow choices")
public class CharacterGenderController {

  private final CharacterGenderService characterGenderService;

  @GetMapping("/")
  @Operation(summary = "List all the genders", description = "Get all the genders")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<CharacterGenderDto> getAll() {
    return characterGenderService.loadAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a gender", description = "Get a gender. This is probable linked to a Character")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public CharacterGenderDto getGender(@PathVariable Integer id) throws FindDataException {
    return characterGenderService.loadGender(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Register a gender", description = "Register a gender. This is probable linked to a Character")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  @Validated(OnCreate.class)
  public CharacterGenderDto create(@Valid @RequestBody CharacterGenderDto characterGenderDto) {
    return characterGenderService.create(characterGenderDto);
  }

  @PutMapping("/")
  @Operation(summary = "Update a gender", description = "Update a gender. This is typos")
  @ApiResponse(responseCode = "400", description = "Invalid")
  @Validated(OnUpdate.class)
  public CharacterGenderDto update(@Valid @RequestBody CharacterGenderDto characterGenderDto) throws FindDataException {
    return characterGenderService.update(characterGenderDto);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a gender", description = "Deletes a gender. Not sure why?")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public void delete(@PathVariable @Min(1) Integer id) throws FindDataException {
    characterGenderService.delete(id);
  }

}
