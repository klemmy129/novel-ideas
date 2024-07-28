package com.klemmy.novelideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterGenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.validation.ConstraintViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
class CharacterGenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CharacterGenderService service;

    @Test
    @WithMockUser
    void getAll__success() throws Exception {
        List<CharacterGenderDto> dtoList = List.of(
                TestEntities.characterGenderDtoBuilder(),
                TestEntities.characterGenderDtoBuilder2());
        when(service.loadAll()).thenReturn(dtoList);

        this.mockMvc.perform(get("/gender/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoList.get(0).id()))
                .andExpect(jsonPath("$[1].id").value(dtoList.get(1).id()));
    }

    @Test
    @WithMockUser
    void getId__validData__success() throws Exception {
        CharacterGenderDto dto = TestEntities.characterGenderDtoBuilder();
        when(service.loadGender(dto.id())).thenReturn(dto);

        this.mockMvc.perform(get("/gender/" + dto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    @WithMockUser
    void getId__inValidId__failure() throws Exception {
        doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Gender with id:%d.",
                TestEntities.NOT_GENERIC_ID))).when(service).loadGender(TestEntities.NOT_GENERIC_ID);

        this.mockMvc.perform(get("/gender/" + TestEntities.NOT_GENERIC_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));

    }

    @Test
    @WithMockUser
    void create__validData__success() throws Exception {
        CharacterGenderDto dto = TestEntities.characterGenderDtoCreateBuilder();
        when(service.create(any(CharacterGenderDto.class))).thenReturn(dto);

        this.mockMvc.perform(post("/gender/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    @WithMockUser
    void create__inValidData__failure() throws Exception {
        CharacterGenderDto dtoBad = TestEntities.characterGenderDtoBuilder();

        this.mockMvc.perform(post("/gender/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtoBad)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(jsonPath("$.detail").value("create.characterGenderDto.id: must be null")) // Normal Message
                .andExpect(jsonPath("$.['create.characterGenderDto.id']").value("must be null"))  // ProblemDetail.setProperty
                .andExpect(jsonPath("$.instance").value("/gender/"));

    }

    @Test
    @WithMockUser
    void update__validData__success() throws Exception {
        CharacterGenderDto dto = TestEntities.characterGenderDtoBuilder();
        when(service.update(any(CharacterGenderDto.class))).thenReturn(dto);

        this.mockMvc.perform(put("/gender/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    @WithMockUser
    void update__nullId__failure() throws Exception {
        CharacterGenderDto dtoWithNullId = TestEntities.characterGenderDtoCreateBuilder();

        this.mockMvc.perform(put("/gender/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtoWithNullId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(jsonPath("$.detail").value("update.characterGenderDto.id: must not be null")) // Normal Message
                .andExpect(jsonPath("$.['update.characterGenderDto.id']").value("must not be null"))  // ProblemDetail.setProperty
                .andExpect(jsonPath("$.instance").value("/gender/"));

    }

    @Test
    @WithMockUser
    void delete__validData__success() throws Exception {
        this.mockMvc.perform(delete("/gender/" + TestEntities.GENERIC_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete__inValidData__failure() throws Exception {

        doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Gender with id:%d, to delete.",
                TestEntities.NOT_GENERIC_ID))).when(service).delete(TestEntities.NOT_GENERIC_ID);

        this.mockMvc.perform(delete("/gender/" + TestEntities.NOT_GENERIC_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));
    }
}