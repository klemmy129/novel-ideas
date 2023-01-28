package com.klemmy.novelideas.client3;

import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.CharacterGenderDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface NovelIdeasClient3 {

    @GetExchange("/book")
    Page<BookDto> getAllBooks(@RequestParam(required = false) String queryTitle,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                 @RequestParam(required = false) Set<BookState> state,
                                 @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable);

    @GetExchange("/book/{id}")
    BookDto getBooKById(Integer id);

    @GetExchange("/gender")
    List<CharacterGenderDto> getAllGenders();

}
