package com.klemmy.novelideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterProfileService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CharacterProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CharacterProfileService service;

  @Test
  @WithMockUser
  void getAll__success() throws Exception {
    List<CharacterProfileDto> dtoList = List.of(
        TestEntities.characterProfileDtoBuilder().build(),
        TestEntities.characterProfileDtoBuilder2().build());
    when(service.loadAll()).thenReturn(dtoList);

    this.mockMvc.perform(get("/character-profile/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(dtoList.get(0).getId()))
        .andExpect(jsonPath("$[1].id").value(dtoList.get(1).getId()));
  }

  @Test
  @WithMockUser
  void getId__validData__success() throws Exception {
    CharacterProfileDto dto = TestEntities.characterProfileDtoBuilder().build();
    when(service.loadCharacterProfile(dto.getId())).thenReturn(dto);

    this.mockMvc.perform(get("/character-profile/" + dto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(dto.getId()));
  }

  @Test
  @WithMockUser
  void getId__inValidId__failure() throws Exception {
    doThrow(new FindDataException(String.format("Could not find Character Profile with id:%d.",
        TestEntities.NOT_GENERIC_ID))).when(service).loadCharacterProfile(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(get("/character-profile/" + TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));

  }

  @Test
  @WithMockUser
  void create__validData__success() throws Exception {
    CharacterProfileDto dto = TestEntities.characterProfileDtoCreateBuilder().build();
    when(service.create(any(CharacterProfileDto.class))).thenReturn(dto);

    this.mockMvc.perform(post("/character-profile/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(dto.getId()));
  }

  @Test
  @WithMockUser
  void create__inValidData__failure() throws Exception {
    CharacterProfileDto dtoBad = TestEntities.characterProfileDtoBuilder().build();

    this.mockMvc.perform(post("/character-profile/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dtoBad)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
        .andExpect(jsonPath("$.violations[0].fieldName").value("create.characterProfileDto.id"))
        .andExpect(jsonPath("$.violations[0].message").value("must be null"));

  }

  @Disabled
  @Test
  @WithMockUser
  void update__validData__success() {
  }

  @Disabled
  @Test
  @WithMockUser
  void update__nullId__failure() throws Exception {
    CharacterProfileDto dtoWithNullId = TestEntities.characterProfileDtoCreateBuilder().build();

    this.mockMvc.perform(put("/character-profile/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dtoWithNullId)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
        .andExpect(jsonPath("$.violations[0].fieldName").value("update.characterProfileDto.id"))
        .andExpect(jsonPath("$.violations[0].message").value("must not be null"));

  }

  @Test
  @WithMockUser
  void delete__validData__success() throws Exception {
    this.mockMvc.perform(delete("/character-profile/{id}", TestEntities.GENERIC_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void delete__inValidData__failure() throws Exception {
    doThrow(new FindDataException(String.format("Could not find Character Profile with id:%d, to delete.",
        TestEntities.NOT_GENERIC_ID))).when(service).delete(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(delete("/character-profile/{id}", TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));
  }

}