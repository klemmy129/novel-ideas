package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.TestEntities;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.repository.CharacterProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db")
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:CharacterTestData.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:CharacterDataCleanup.sql")
})
class CharacterProfileSpecificationTest {

  @Autowired
  CharacterProfileRepository characterProfileRepository;

  @Test
  @WithMockUser
  void findAll__validData__success() {
    CharacterProfile characterProfileDB1 = TestEntities.characterProfileBuilder().build();
    characterProfileRepository.save(characterProfileDB1);
    List<CharacterProfile> result = characterProfileRepository.findAll();
    assertThat(result).isNotEmpty();
    assertThat(result.size()).isEqualTo(5);
    assertThat(result.stream().findAny().get().getCharacterName()).isEqualTo(TestEntities.CHAR_NAME);
  }
  @Test
  @WithMockUser
  void findAllByFilters__nullFilters__success() {
    Pageable page = PageRequest.of(0, 5);
    Page<CharacterProfile> result = characterProfileRepository.findAllByFilters(null, null, null, page);
    assertThat(result.getContent()).isNotEmpty();
    assertThat(result.getContent()).hasSize(4);
    assertThat(result.getContent().get(0).getId()).isEqualTo(901);
  }

  @Test
  @WithMockUser
  void findAllByFilters__noMatchFilters__success() {
    Pageable page = PageRequest.of(0, 5);

    Page<CharacterProfile> result = characterProfileRepository.findAllByFilters(null, null,  TestEntities.GENERIC_VALUE, page);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @WithMockUser
  void findAllByFilters__allFilters__success() {

    Pageable page = PageRequest.of(0, 5);

    Page<CharacterProfile> result = characterProfileRepository.findAllByFilters("potter", TestEntities.IMPORTANCE_HACK, TestEntities.GENDER_MALE, page);

    assertThat(result.getContent()).isNotEmpty();
    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getContent()).allSatisfy(i -> assertThat(i.getCharacterName().contains("Harry Potter")));
  }

}
