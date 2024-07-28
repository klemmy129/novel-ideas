package com.klemmy.novelideas.dto;

import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.jpa.Book;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class BookFactory {

  public static BookDto toDTO(Book entity) {
    return BookDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .description(entity.getDescription())
        .startDate(entity.getStartDate())
        .state(entity.getState())
        .characterProfiles(entity.getCharacterProfiles() != null ? entity.getCharacterProfiles().stream()
            .map(CharacterProfileFactory::toDTO).toList() : Collections.emptyList())
        .build();
  }

  public static Book toEntity(BookDto dto) {
    return Book.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .startDate(dto.getStartDate())
        .state(dto.getState())
        .characterProfiles(dto.getCharacterProfiles() != null ? dto.getCharacterProfiles().stream()
            .map(CharacterProfileFactory::toEntity).toList() : Collections.emptyList())
        .build();
  }

}
