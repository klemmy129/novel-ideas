package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.BookStateDto;
import com.klemmy.novelideas.jpa.BookState;
import org.springframework.stereotype.Component;

@Component
public class BookStateFactory {

  public static BookStateDto toDTO(BookState entity) {
    return BookStateDto.builder()
        .id(entity.getId())
        .state(entity.getState())
        .isDeleted(entity.isDeleted())
        .build();
  }

  public static BookState toEntity(BookStateDto dto) {
    return BookState.builder()
        .id(dto.getId())
        .state(dto.getState())
        .isDeleted(dto.isDeleted())
        .build();
  }

}
