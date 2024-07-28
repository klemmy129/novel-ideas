package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterImportanceService;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/character-importance", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Character Importance", description = "Character Importance Stuff...")
public class CharacterImportanceController {

  private final CharacterImportanceService characterImportanceService;

  @GetMapping("/")
  @Operation(summary = "List all Importance",
      description = "Get all Importance that can be assigned to a character")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public List<CharacterImportanceDto> getAll() {
    return characterImportanceService.loadAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an Importance", description = "Get an Importance for a Character")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public CharacterImportanceDto getCharacterImportance(@PathVariable Long id) throws FindDataException {
    return characterImportanceService.loadCharacterImportance(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a CharacterImportance",
      description = "Create an Importance for a Character eg: Main, Critical, Minor, Bady")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  @Validated(OnCreate.class)
  public CharacterImportanceDto create(@Valid @RequestBody CharacterImportanceDto characterImportanceDto) {
    return characterImportanceService.create(characterImportanceDto);
  }

//    @PutMapping("/")
//    @ApiResponse(responseCode = "400", description = "Invalid")
//    public CharacterImportanceDto update(@RequestBody @Valid CharacterImportanceDto characterImportanceDto){
//        return characterImportanceService.update(characterImportanceDto);
//    }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an Importance of a Character", description = "Mark an Importance as deleted")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public void delete(@PathVariable @Min(1) Long id) throws FindDataException {
    characterImportanceService.delete(id);
  }
}
