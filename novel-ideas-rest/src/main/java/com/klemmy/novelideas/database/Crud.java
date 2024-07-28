package com.klemmy.novelideas.database;

import com.klemmy.novelideas.error.FindDataException;

import java.util.List;

public interface Crud<T, S, ID> {

  ID create(S entity);

  List<T> findAll();

  T findById(ID id) throws FindDataException;

  void update(S entity, ID id) throws FindDataException;

  void delete(ID id) throws FindDataException;

}
