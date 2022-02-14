package com.klemmy.novelideas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.error.FindDataException;
import com.klemmy.novelideas.service.BookService;
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

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BookService service;

  @Test
  @WithMockUser
  void getAll__withNoInputs__success() throws Exception {
    Page<BookDto> dtoList = new PageImpl<>(List.of(
        TestEntities.bookDtoBuilder().build(),
        TestEntities.bookDtoBuilder2().build()));

    when(service.loadAll(eq(null), eq(null), eq(null), eq(null), any(Pageable.class))).thenReturn(dtoList);

    this.mockMvc.perform(get("/book/")
            .param("pageable", String.valueOf(PageRequest.of(0, 5)))
        )
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
        .andExpect(jsonPath("$.content[0].id").value(dtoList.getContent().get(0).getId()))  // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.content[1].id").value(dtoList.getContent().get(1).getId()))  // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.content[1].name").value(TestEntities.GENERIC_VALUE2));  // Not needed, just a different way to validate json data

  }

  @Test
  @WithMockUser
  void getAll__withInputs__success() throws Exception {
    Page<BookDto> dtoList = new PageImpl<>(Collections.singletonList(TestEntities.bookDtoBuilder().build()));
    LocalDateTime oneMonthEarlier = TestEntities.PAST_DATETIME.minusMonths(1);
    LocalDateTime oneMonthLater = TestEntities.PAST_DATETIME.plusMonths(1);
    Set<BookState> state = Collections.singleton(BookState.ACTIVE);
    when(service.loadAll(eq(TestEntities.GENERIC_VALUE), eq(oneMonthEarlier), eq(oneMonthLater), eq(state), any(Pageable.class))).thenReturn(dtoList);

    this.mockMvc.perform(get("/book/")
            .param("queryTitle", TestEntities.GENERIC_VALUE)
            .param("startDate", oneMonthEarlier.toString())
            .param("endDate", oneMonthLater.toString())
            .param("state", "ACTIVE")
            .param("pageable", String.valueOf(PageRequest.of(0, 5)))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(dtoList)))
        .andExpect(jsonPath("$.content[0].id").value(dtoList.getContent().get(0).getId())); // Not needed, just a different way to validate json data
  }

  @Test
  @WithMockUser
  void getId__validData__success() throws Exception {
    BookDto dto = TestEntities.bookDtoBuilder().build();
    when(service.loadBook(dto.getId())).thenReturn(dto);

    this.mockMvc.perform(get("/book/{id}", dto.getId()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(dto)))
        .andExpect(jsonPath("$.id").value(dto.getId()))  // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.name").value(dto.getName()));
  }

  @Test
  @WithMockUser
  void getId__inValidId__failure() throws Exception {
    doThrow(new FindDataException(String.format("Could not find Book with id:%d.",
        TestEntities.NOT_GENERIC_ID))).when(service).loadBook(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(get("/book/" + TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));

  }

  @Test
  @WithMockUser
  void getBookCharacters__validData__success() throws Exception {
    BookDto dto = TestEntities.bookDtoBuilder().build();
    List<CharacterProfileDto> characterProfileDtos = dto.getCharacterProfiles();
    when(service.getBookCharacter(dto.getId()))
        .thenReturn(characterProfileDtos);

    this.mockMvc.perform(get("/book/{id}/characters", dto.getId()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(characterProfileDtos)))
        .andExpect(jsonPath("$[0].id")                   // Not needed, just a different way to validate json data
            .value(characterProfileDtos.stream().findFirst().get().getId()))
        .andExpect(jsonPath("$[0].characterName")
            .value(characterProfileDtos.stream().findFirst().get().getCharacterName()));
  }

  @Test
  @WithMockUser
  void create__validData__success() throws Exception {
    BookDto createDto = TestEntities.bookDtoCreateBuilder().build();
    BookDto ResultDto = createDto.toBuilder().id(TestEntities.GENERIC_ID).build();
    when(service.create(any(BookDto.class))).thenReturn(ResultDto);

    this.mockMvc.perform(post("/book/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ResultDto.getId()))
        .andExpect(jsonPath("$.name").value(ResultDto.getName()));
  }

  @Test
  @WithMockUser
  void create__inValidData__failure() throws Exception {
    BookDto dtoBad = TestEntities.bookDtoBuilder().build();

    this.mockMvc.perform(post("/book/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dtoBad)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
        .andExpect(jsonPath("$.violations[0].fieldName").value("create.bookDto.id"))
        .andExpect(jsonPath("$.violations[0].message").value("must be null"));

  }

  @Test
  void update__validData__success() throws Exception {
    BookDto dto = TestEntities.bookDtoBuilder().build();
    when(service.update(any(BookDto.class))).thenReturn(dto);

    this.mockMvc.perform(put("/book/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(dto.getId()))
        .andExpect(jsonPath("$.name").value(dto.getName()));
  }

  @Test
  @WithMockUser
  void update__nullId__failure() throws Exception {
    BookDto dtoWithNullId = TestEntities.bookDtoCreateBuilder().build();

    this.mockMvc.perform(put("/book/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dtoWithNullId)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ConstraintViolationException.class))
        .andExpect(jsonPath("$.violations[0].fieldName").value("update.bookDto.id"))
        .andExpect(jsonPath("$.violations[0].message").value("must not be null"));

  }

  @Test
  @WithMockUser
  void addCharacterToBook__validData__success() throws Exception {
    CharacterProfileDto CharacterDto = TestEntities.characterProfileDtoBuilder().build();
    BookDto bookDto = TestEntities.bookBuilderDtoOneChars().build();
    when(service.addCharacter(anyInt(), any(CharacterProfileDto.class))).thenReturn(bookDto);

    this.mockMvc.perform(put("/book/{bookId}/character", TestEntities.GENERIC_ID)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(CharacterDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(bookDto)))
        .andExpect(jsonPath("$.id").value(bookDto.getId()))  // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.name").value(bookDto.getName()))
        .andExpect(jsonPath("$.characterProfiles[0].id")
            .value(bookDto.getCharacterProfiles().stream().findFirst().get().getId()))
        .andExpect(jsonPath("$.characterProfiles[0].characterName")
            .value(bookDto.getCharacterProfiles().stream().findFirst().get().getCharacterName()));
  }

  @Test
  @WithMockUser
  void removeCharacterToBook__validData__success() throws Exception {
    BookDto bookDto = TestEntities.bookBuilderDtoOneChars().build();
    when(service.removeCharacter(anyInt(), anyInt())).thenReturn(bookDto);

    this.mockMvc.perform(put("/book/{bookId}/character/{characterId}",
            TestEntities.GENERIC_ID, TestEntities.GENERIC_ID2)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(bookDto)))
        .andExpect(jsonPath("$.id").value(bookDto.getId()))   // Not needed, just a different way to validate json data
        .andExpect(jsonPath("$.name").value(bookDto.getName()))
        .andExpect(jsonPath("$.characterProfiles[0].id")
            .value(bookDto.getCharacterProfiles().stream().findFirst().get().getId()))
        .andExpect(jsonPath("$.characterProfiles[0].characterName")
            .value(bookDto.getCharacterProfiles().stream().findFirst().get().getCharacterName()));
  }

  @Test
  @WithMockUser
  void delete__validData__success() throws Exception {
    this.mockMvc.perform(delete("/book/" + TestEntities.GENERIC_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser
  void delete__inValidData__failure() throws Exception {
    doThrow(new FindDataException(String.format("Could not find Book with id:%d, to delete.",
        TestEntities.NOT_GENERIC_ID))).when(service).delete(TestEntities.NOT_GENERIC_ID);

    this.mockMvc.perform(delete("/book/" + TestEntities.NOT_GENERIC_ID))
        .andExpect(status().isNotFound())
        .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(FindDataException.class));
  }

}
