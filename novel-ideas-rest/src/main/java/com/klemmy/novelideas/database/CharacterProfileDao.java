package com.klemmy.novelideas.database;

import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.CharacterProfileGridDto;
import com.klemmy.novelideas.error.FindDataException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class CharacterProfileDao implements Crud<CharacterProfileGridDto,CharacterProfileDto, Long> {

  private final JdbcClient jdbcClient;

  private enum CharacterProfileSql {
    LOAD_ALL("""
            SELECT cp.id,
                   cp.character_name,
                   cp.title,
                   cp.first_name,
                   cp.middle_name,
                   cp.surname,
                   cp.nick_name,
                   cg.gender,
                   ci.importance,
                   cp.physical_description,
                   cp.inner_goal,
                   cp.outer_goal,
                   cp.function_in_story,
                   cp.date_of_birth
            FROM character_profile cp
                     JOIN character_importance ci
                          ON cp.character_importance_id = ci.id
                     JOIN character_gender cg
                          ON cp.character_gender_id = cg.id
            ORDER BY character_name
        """),
    LOAD_BY_ID("""
        SELECT cp.id,
               cp.character_name,
               cp.title,
               cp.first_name,
               cp.middle_name,
               cp.surname,
               cp.nick_name,
               cg.gender,
               ci.importance,
               cp.physical_description,
               cp.inner_goal,
               cp.outer_goal,
               cp.function_in_story,
               cp.date_of_birth
        FROM character_profile cp
                 JOIN character_importance ci
                      ON cp.character_importance_id = ci.id
                 JOIN character_gender cg
                      ON cp.character_gender_id = cg.id
        WHERE cp.id = :id
        """),
    FILTER("""
        SELECT cp.id,
               cp.character_name,
               cp.title,
               cp.first_name,
               cp.middle_name,
               cp.surname,
               cp.nick_name,
               cg.gender,
               ci.importance,
               cp.physical_description,
               cp.inner_goal,
               cp.outer_goal,
               cp.function_in_story,
               cp.date_of_birth
        FROM character_profile cp
                 JOIN character_importance ci
                      ON cp.character_importance_id = ci.id
                 JOIN character_gender cg
                      ON cp.character_gender_id = cg.id
        WHERE
        (:queryName IS NULL OR LOWER(cp.character_name) LIKE :queryName) AND
        (:queryName IS NULL OR LOWER(cp.first_name) LIKE :queryName) AND
        (:queryName IS NULL OR LOWER(cp.surname) LIKE :queryName) AND
        (:queryName IS NULL OR LOWER(cp.nick_name) LIKE :queryName) AND
        (:importance IS NULL OR LOWER(ci.importance) LIKE LOWER(:importance)) AND
        (:gender IS NULL OR LOWER(cg.gender) LIKE LOWER(:gender))
        OFFSET :offset ROWS FETCH NEXT :fetch ROWS ONLY
        """),
    CREATE("""
        INSERT INTO character_profile cp (cp.id,
            cp.character_name,
            cp.title,
            cp.first_name,
            cp.middle_name,
            cp.surname,
            cp.nick_name,
            cp.character_gender_id,
            cp.character_importance_id,
            cp.physical_description,
            cp.inner_goal,
            cp.outer_goal,
            cp.function_in_story,
            cp.date_of_birth)
            VALUES (
            :id,
            :character_name,
            :title,
            :first_name,
            :middle_name,
            :surname,
            :nick_name,
            :character_gender_id,
            :character_importance_id,
            :physical_description,
            :inner_goal,
            :outer_goal,
            :function_in_story,
            :date_of_birth
            )
        """),
    UPDATE("""
        UPDATE character_profile cp SET (
            cp.character_name = :character_name,
            cp.title = :title,
            cp.first_name = :first_name,
            cp.middle_name = :middle_name,
            cp.surname = :surname,
            cp.nick_name = :nick_name,
            cp.character_gender_id = :character_gender_id,
            cp.character_importance_id = :character_importance_id,
            cp.physical_description = :physical_description,
            cp.inner_goal = :inner_goal,
            cp.outer_goal = :outer_goal,
            cp.function_in_story = :function_in_story,
            cp.date_of_birth = :date_of_birth
            )
        WHERE cp.id = :id
        """),
    DELETE("""
        DELETE FROM character_profile 
        WHERE cp.id = :id
        """);

    private final String sqlStatement;

    private CharacterProfileSql(String sqlStatement) {
      this.sqlStatement = sqlStatement;
    }

    public String statement() {
      return sqlStatement;
    }
  }

  // endregion SQL

  @Override
  public Long create(CharacterProfileDto entity) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcClient.sql(CharacterProfileSql.CREATE.statement())
        .param(entity)
        .update(keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  @Override
  public List<CharacterProfileGridDto> findAll() {
    return jdbcClient.sql(CharacterProfileSql.LOAD_ALL.statement())
        .query(CharacterProfileGridDto.class)
        .list();
  }

  @Override
  public CharacterProfileGridDto findById(Long id) throws FindDataException {
    return jdbcClient.sql(CharacterProfileSql.LOAD_BY_ID.statement())
        .param("id", id)
        .query(CharacterProfileGridDto.class)
        .optional()
        .orElseThrow(() ->
            new FindDataException(id, "Could not find Character Profile with id:/{id}."));
  }

  public Page<CharacterProfileGridDto> findAllByFilters(String queryName,
                                                    String importance,
                                                    String gender,
                                                    Pageable pageable) {
    if (queryName != null && !queryName.isEmpty()) {
      queryName = queryName.toLowerCase();
    }

    List<CharacterProfileGridDto> characterProfileDtoList = jdbcClient.sql(CharacterProfileSql.FILTER.statement())
        .param("queryName", queryName)
        .param("importance", importance)
        .param("gender", gender)
        .param("offset", pageable.getOffset())
        .param("fetch", pageable.getPageSize())
        .query(CharacterProfileGridDto.class)
        .list();
    return new PageImpl<>(characterProfileDtoList, pageable, characterProfileDtoList.size());
  }

  @Override
  public void update(CharacterProfileDto entity, Long id) throws FindDataException {
    try {
      int updatedRows = jdbcClient.sql(CharacterProfileSql.UPDATE.statement())
          .param(entity)
          .update();
      if (updatedRows < 1) {
        throw new FindDataException(id, "Could not update Character Profile with id:/{id}.");
      }
    } catch (DataAccessException e) {
      throw new FindDataException(id, e.getMessage());
    }
  }

  @Override
  public void delete(Long id) throws FindDataException {
    try {
      int deletedRows = jdbcClient.sql(CharacterProfileSql.DELETE.statement())
          .param("id", id)
          .update();
      if (deletedRows < 1) {
        throw new FindDataException(id, "Could not delete Character Profile with id:/{id}.");
      }
    } catch (DataAccessException e) {
      throw new FindDataException(id, e.getMessage());
    }
  }

}
