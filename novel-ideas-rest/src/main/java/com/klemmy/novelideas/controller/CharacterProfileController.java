package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.OnCreate;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/character-profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Character Profile", description = "Character profiles make a novel")
public class CharacterProfileController {

  private final CharacterProfileService characterProfileService;

  @GetMapping("/")
  @Operation(summary = "Get all Characters", description = "List all the characters")
  @ApiResponse(responseCode = "400", description = "Invalid")
  public Page<CharacterProfileDto> getAll(@RequestParam(required = false) String queryName,
                                          @RequestParam(required = false) String importance,
                                          @RequestParam(required = false) String gender,
                                          @ParameterObject @PageableDefault(size = 20, sort = "characterName") Pageable pageable) {
    return characterProfileService.loadAll(queryName, importance, gender, pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a Character", description = "Get a single character that in a novel or script")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public CharacterProfileDto getCharacterProfile(@PathVariable Integer id) throws FindDataException {
    return characterProfileService.loadCharacterProfile(id);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a Character Profile", description = "This creates a character profile that will by in a novel or script")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  @Validated(OnCreate.class)
  public CharacterProfileDto create(@Valid @RequestBody CharacterProfileDto characterProfileDto) {
    return characterProfileService.create(characterProfileDto);
  }

//    @PutMapping("/")
//    @ApiResponse(responseCode = "400", description = "Invalid")
//    public CharacterProfileDto update(@Valid @RequestBody CharacterProfileDto characterProfileDto){
//        return characterProfileService.update(characterProfileDto);
//    }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a Character",
      description = "Are you sure you want to delete this or is it that character bad?")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "Invalid"),
      @ApiResponse(responseCode = "404", description = "Not Found")})
  public void delete(@PathVariable Integer id) throws FindDataException {
    characterProfileService.delete(id);
  }
}
