package com.klemmy.novelideas.database;

import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.error.FindDataException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class CharacterGenderDao {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  // region SQL

  private static final String LOAD_ALL = """
      SELECT id, gender
      FROM character_gender
      WHERE is_deleted = 0
      ORDER BY gender
      """;

  private static final String LOAD_GENDER = """
      SELECT id, gender
      FROM character_gender
      WHERE is_deleted = 0
      AND id = :id
      """;

  private static final String CREATE_GENDER = """
      INSERT INTO character_gender
        (gender, is_deleted )
      VALUES (:gender, :is_deleted)
      """;

  private static final String UPDATE_GENDER = """
      UPDATE character_gender
      SET gender = :gender,
          is_deleted = :is_deleted
      WHERE  id = :id
      """;
  private static final String DELETE_GENDER = """
      UPDATE character_gender
      SET is_deleted = :is_deleted
      WHERE  id = :id
      """;

// endregion SQL

  static RowMapper<CharacterGenderDto> rowMapper = (rs, rowNum) -> new CharacterGenderDto(
      rs.getLong("ID"),
      rs.getString("GENDER"),
      false
  );

  public List<CharacterGenderDto> findAll() {
    return jdbcTemplate.query(LOAD_ALL, rowMapper);
  }

  public CharacterGenderDto findById(Long id) {
    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
    return jdbcTemplate.queryForObject(LOAD_GENDER, namedParameters, (rs, rowNum) ->
        new CharacterGenderDto(rs.getLong("ID"),
            rs.getString("GENDER"),
            false));
  }

  public Long create(CharacterGenderDto characterGender) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    SqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("gender", characterGender.gender())
        .addValue("is_deleted", false);
    jdbcTemplate.update(CREATE_GENDER, namedParameters, keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).longValue();
  }

  public void update(CharacterGenderDto characterGender, Long id) throws FindDataException {
    SqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("gender", characterGender.gender())
        .addValue("is_deleted", false)
        .addValue("id,", id);
    int updatedRows = jdbcTemplate.update(UPDATE_GENDER, namedParameters);
    if (updatedRows < 1) {
      throw new FindDataException(id, "Could not update Character Profile with id:/{id}.");
    }
  }

  public void delete(Long id) throws FindDataException {
    SqlParameterSource namedParameters = new MapSqlParameterSource()
        .addValue("is_deleted", true)
        .addValue("id,", id);
    int deletedRows = jdbcTemplate.update(DELETE_GENDER, namedParameters);
    if (deletedRows < 1) {
      throw new FindDataException(id, "Could not delete Character Profile with id:/{id}.");
    }
  }

}
