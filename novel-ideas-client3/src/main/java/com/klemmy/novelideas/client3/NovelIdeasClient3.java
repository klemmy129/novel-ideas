package com.klemmy.novelideas.client3;


import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.CharacterGenderDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface NovelIdeasClient3 {

//TODO Pageable is not working
//  @GetExchange("/book/")
//  Page<BookDto> getAllBooks(@RequestParam(required = false) String queryTitle,
//                                @RequestParam(required = false)
//                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//                                @RequestParam(required = false)
//                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
//                                @RequestParam(required = false) BookState state,
//                                @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable);

  @GetExchange("/book/{id}")
  BookDto getBooKById(@PathVariable Integer id);

  @GetExchange("/gender/")
  List<CharacterGenderDto> getAllGenders();

}
