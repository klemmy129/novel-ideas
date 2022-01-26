package com.klemmy.novelideas.jpa.repository;

import com.klemmy.novelideas.jpa.BookState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookStateRepository extends JpaRepository<BookState, Integer> {

  List<BookState> findByIsDeletedFalse();

  //Look up deleted entities
  @Query("select e from #{#entityName} e where e.isDeleted=true")
  public List<BookState> recycleBin();

}
