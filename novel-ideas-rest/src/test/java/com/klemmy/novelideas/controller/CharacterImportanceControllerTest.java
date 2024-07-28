package com.klemmy.novelideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.CharacterImportanceService;
import org.junit.jupiter.api.Disabled;
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
class CharacterImportanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CharacterImportanceService service;

    @Test
    @WithMockUser
    void getAll__success() throws Exception {
        List<CharacterImportanceDto> dtoList = List.of(
                TestEntities.characterImportanceDtoBuilder(),
                TestEntities.characterImportanceDtoBuilder2());
        when(service.loadAll()).thenReturn(dtoList);

        this.mockMvc.perform(get("/character-importance/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoList.get(0).id()))
                .andExpect(jsonPath("$[1].id").value(dtoList.get(1).id()));
    }

    @Test
    @WithMockUser
    void getId__validData__success() throws Exception {
        CharacterImportanceDto dto = TestEntities.characterImportanceDtoBuilder();
        when(service.loadCharacterImportance(dto.id())).thenReturn(dto);

        this.mockMvc.perform(get("/character-importance/{id}", dto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    @WithMockUser
    void getId__inValidId__failure() throws Exception {
        doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Character Importance with id:%d.",
                TestEntities.NOT_GENERIC_ID))).when(service).loadCharacterImportance(TestEntities.NOT_GENERIC_ID);

        this.mockMvc.perform(get("/character-importance/{id}", TestEntities.NOT_GENERIC_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));

    }

    @Test
    @WithMockUser
    void create__validData__success() throws Exception {
        CharacterImportanceDto dto = TestEntities.characterImportanceDtoCreateBuilder();
        when(service.create(any(CharacterImportanceDto.class))).thenReturn(dto);

        this.mockMvc.perform(post("/character-importance/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.id()));
    }

    @Test
    @WithMockUser
    void create__inValidData__failure() throws Exception {
        CharacterImportanceDto dtoBad = TestEntities.characterImportanceDtoBuilder();

        this.mockMvc.perform(post("/character-importance/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtoBad)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(jsonPath("$.detail").value("create.characterImportanceDto.id: must be null")) // Normal Message
                .andExpect(jsonPath("$.['create.characterImportanceDto.id']").value("must be null"))  // ProblemDetail.setProperty
                .andExpect(jsonPath("$.instance").value("/character-importance/"));

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
        CharacterImportanceDto dtoWithNullId = TestEntities.characterImportanceDtoCreateBuilder();

        this.mockMvc.perform(put("/character-importance/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dtoWithNullId)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
                .andExpect(jsonPath("$.violations[0].fieldName").value("update.characterImportanceDto.id"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"));

    }

    @Test
    @WithMockUser
    void delete__validData__success() throws Exception {
        this.mockMvc.perform(delete("/character-importance/" + TestEntities.GENERIC_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete__inValidData__failure() throws Exception {

        doThrow(new FindDataException(TestEntities.NOT_GENERIC_ID, String.format("Could not find Character Importance with id:%d, to delete.",
                TestEntities.NOT_GENERIC_ID))).when(service).delete(TestEntities.NOT_GENERIC_ID);

        this.mockMvc.perform(delete("/character-importance/" + TestEntities.NOT_GENERIC_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));
    }

}
