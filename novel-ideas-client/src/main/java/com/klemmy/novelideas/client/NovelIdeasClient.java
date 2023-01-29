package com.klemmy.novelideas.client;

import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.util.RestPage;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.ws.rs.DefaultValue;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class NovelIdeasClient {

  static final String BOOK_BASE = "/book/";
  static final String BOOK_ID = BOOK_BASE + "{id}";
  static final String GENDER_BASE = "/gender/";
  private final String baseUrl;
  private final RestTemplate restTemplate;

  public ResponseEntity<Page<BookDto>> getAllBooks(String queryTitle,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   BookState state,
                                                   @DefaultValue(value = "0") String page,
                                                   @DefaultValue(value = "20") String size,
                                                   @DefaultValue(value = "name:ASC") Sort sort) {

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .path(BOOK_BASE)
        .queryParamIfPresent("queryTitle", Optional.ofNullable(queryTitle))
        .queryParamIfPresent("startDate", Optional.ofNullable(startDate))
        .queryParamIfPresent("endDate", Optional.ofNullable(endDate))
        .queryParamIfPresent("state", Optional.ofNullable(state == null ? null : state.name()))
        .queryParam("page", page)
        .queryParam("size", size);

    sort.forEach(order -> uriComponentsBuilder.queryParam("sort", String.format("%s%s%s", order.getProperty(), "%2C", order.getDirection())));
    URI uri = uriComponentsBuilder.build(true).toUri();

    ParameterizedTypeReference<RestPage<BookDto>> restPageBookDto = new ParameterizedTypeReference<>() {
    };
    ResponseEntity<RestPage<BookDto>> response = restTemplate.exchange(uri, HttpMethod.GET, null, restPageBookDto);

    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  public ResponseEntity<BookDto> getBook(Integer id) {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .path(BOOK_ID)
        .buildAndExpand(id)
        .toUri();

    return restTemplate.exchange(uri, HttpMethod.GET, null, BookDto.class);
  }

  public ResponseEntity<List<CharacterGenderDto>> getAllGenders() {
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .path(GENDER_BASE)
        .build().toUri();

    ParameterizedTypeReference<List<CharacterGenderDto>> restListGendersDto = new ParameterizedTypeReference<List<CharacterGenderDto>>() {
    };
    return restTemplate.exchange(uri, HttpMethod.GET, null, restListGendersDto);
  }

}
