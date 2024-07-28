package com.klemmy.novelideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.CharacterProfileGridDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterProfileService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
  void getAll__withNoInputs__success() throws Exception {
    Pageable pageable = PageRequest.of(0, 5);
    Page<CharacterProfileGridDto> dtoList = new PageImpl<>(List.of(
        TestEntities.characterProfileFlatDtoBuilder().build(),
        TestEntities.characterProfileFlatDtoBuilder2().build()),
        pageable, 2);
    when(service.loadAll(eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(dtoList);

    this.mockMvc.perform(get("/character-profile/"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
        .andExpect(jsonPath("$.content[0].id").value(dtoList.getContent().get(0).getId())) // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.content[1].id").value(dtoList.getContent().get(1).getId())); // Not needed, just a different way to validate json data
  }

  @Test
  @WithMockUser
  void getAll__withInputs__success() throws Exception {
    Pageable pageable = PageRequest.of(0, 5);
    Page<CharacterProfileGridDto> dtoList = new PageImpl<>(Collections.singletonList(TestEntities.characterProfileFlatDtoBuilder().build()), pageable, 1);

    //when(service.loadAll(eq(TestEntities.GENERIC_VALUE), eq(TestEntities.GENERIC_VALUE), eq(TestEntities.GENDER_MALE), any(Pageable.class))).thenReturn(dtoList);
    when(service.loadAll(anyString(), anyString(), anyString(), any())).thenReturn(dtoList);


    this.mockMvc.perform(get("/character-profile/")
                .param("queryName", TestEntities.GENERIC_VALUE)
                .param("importance", TestEntities.GENERIC_VALUE)
                .param("gender", TestEntities.GENDER_MALE)
//            .param("pageable", String.valueOf(PageRequest.of(0, 5)))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
        .andExpect(jsonPath("$.content[0].id").value(dtoList.getContent().get(0).getId())); // Not needed, just a different way to validate json data
  }

  @Test
  @WithMockUser
  void getId__validData__success() throws Exception {
    CharacterProfileGridDto dto = TestEntities.characterProfileFlatDtoBuilder().build();
    when(service.loadCharacterProfile(dto.getId())).thenReturn(dto);

    this.mockMvc.perform(get("/character-profile/" + dto.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(dto.getId()));
  }

  @Test
  @WithMockUser
  void getId__inValidId__failure() throws Exception {
    doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Character Profile with id:%d.",
        TestEntities.NOT_GENERIC_ID))).when(service).loadCharacterProfile(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(get("/character-profile/" + TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));

  }

  @Test
  @WithMockUser
  void create__validData__success() throws Exception {
    CharacterProfileDto dto = TestEntities.characterProfileDtoCreateBuilder().build();
    CharacterProfileGridDto result = TestEntities.characterProfileFlatDtoBuilder().build();
    when(service.create(any(CharacterProfileDto.class))).thenReturn(result);

    this.mockMvc.perform(post("/character-profile/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(result.getId()));
  }

  @Test
  @WithMockUser
  void create__inValidData__failure() throws Exception {
    CharacterProfileDto dtoBad = TestEntities.characterProfileDtoBuilder().build();

    this.mockMvc.perform(post("/character-profile/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dtoBad)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
        .andExpect(jsonPath("$.detail").value("create.characterProfileDto.id: must be null")) // Normal Message
        .andExpect(jsonPath("$.['create.characterProfileDto.id']").value("must be null"))  // ProblemDetail.setProperty
        .andExpect(jsonPath("$.instance").value("/character-profile/"));


  }

  @Disabled("Been lazy")
  @Test
  @WithMockUser
  void update__validData__success() {
  }

  @Disabled("Been lazy")
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
    doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Character Profile with id:%d, to delete.",
        TestEntities.NOT_GENERIC_ID))).when(service).delete(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(delete("/character-profile/{id}", TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));
  }

}