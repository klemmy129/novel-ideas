package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.BookState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookStateControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
  void getAll__success() throws Exception {
    List<BookState> bookStates = Arrays.asList(BookState.values());
    this.mockMvc.perform(get("/book-state/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").value("ACTIVE"))
        .andExpect(jsonPath("$[1]").value("ON_HOLD"));
  }

}
