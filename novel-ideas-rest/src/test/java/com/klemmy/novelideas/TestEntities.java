package com.klemmy.novelideas;

import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.BookState;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterGender;
import com.klemmy.novelideas.jpa.CharacterImportance;
import com.klemmy.novelideas.jpa.CharacterProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TestEntities {

  public static final Integer GENERIC_ID = 1;
  public static final Integer GENERIC_ID2 = 2;
  public static final Integer NOT_GENERIC_ID = 999;
  public static final String GENERIC_VALUE = "AAAA";
  public static final String GENERIC_VALUE2 = "BBBB";
  public static final String CHAR_NAME = "John Doe";
  public static final String FIRST_NAME = "John";
  public static final String SURNAME = "Doe";
  public static final String CHAR_NAME2 = "Jane Doe";
  public static final String FIRST_NAME2 = "Jane";
  public static final String SURNAME2 = "Doe";
  public static final String GENDER_MALE = "Male";
  public static final String IMPORTANCE_HACK = "hack";
  public static final String PARA = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ullamcorper " +
      "sodales eleifend. Morbi odio elit, pretium sit amet diam vitae.";
  public static final String PARA2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent iaculis " +
      "interdum sapien. Aliquam vestibulum dolor tristique gravida sodales. Ut aliquam rutrum nisi, at porta erat.";

  public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  static String dateString = "14/07/1980";
  public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  static String dateTimeString = "14/07/1980 13:40:00";

  public static LocalDate PAST_DATE = LocalDate.parse(dateString, dateFormatter);
  public static LocalDateTime PAST_DATETIME = LocalDateTime.parse(dateTimeString, dateTimeFormatter);


  public static CharacterGender.CharacterGenderBuilder characterGenderBuilder() {
    return CharacterGender.builder()
        .id(GENERIC_ID)
        .gender(GENDER_MALE)
        .isDeleted(false);
  }

  public static CharacterGender.CharacterGenderBuilder characterGenderBuilder2() {
    return CharacterGender.builder()
        .id(GENERIC_ID2)
        .gender(GENERIC_VALUE2)
        .isDeleted(false);
  }

  public static CharacterGenderDto characterGenderDtoCreateBuilder() {
    return new CharacterGenderDto(null, GENDER_MALE, false);
  }

  public static CharacterGenderDto characterGenderDtoBuilder() {
    return new CharacterGenderDto(GENERIC_ID, GENDER_MALE, false);
  }

  public static CharacterGenderDto characterGenderDtoBuilder2() {
    return new CharacterGenderDto(GENERIC_ID2, GENERIC_VALUE2, false);
  }

  public static CharacterGenderDto characterGenderDtoBadCreateBuilder() {
    return new CharacterGenderDto(NOT_GENERIC_ID, GENDER_MALE, false);
  }

  public static CharacterGenderDto characterGenderDtoBadNullIdCreateBuilder() {
    return new CharacterGenderDto(null, GENDER_MALE, false);
  }

  public static CharacterImportance.CharacterImportanceBuilder characterImportanceBuilder() {
    return CharacterImportance.builder()
        .id(GENERIC_ID)
        .importance(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static CharacterImportance.CharacterImportanceBuilder characterImportanceBuilder2() {
    return CharacterImportance.builder()
        .id(GENERIC_ID2)
        .importance(GENERIC_VALUE2)
        .isDeleted(false);
  }
  public static CharacterImportanceDto characterImportanceDtoCreateBuilder() {
    return new CharacterImportanceDto(null,GENERIC_VALUE,false);
  }

  public static CharacterImportanceDto characterImportanceDtoBuilder() {
    return new CharacterImportanceDto(
        GENERIC_ID,
        GENERIC_VALUE,
        false);
  }

  public static CharacterImportanceDto characterImportanceDtoBuilder2() {
    return new CharacterImportanceDto(
        GENERIC_ID2,GENERIC_VALUE2,false);
  }

//  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoCreateBuilder() {
//    return CharacterImportanceDto.builder()
//        .importance(GENERIC_VALUE)
//        .isDeleted(false);
//  }
//
//  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoBuilder() {
//    return CharacterImportanceDto.builder()
//        .id(GENERIC_ID)
//        .importance(GENERIC_VALUE)
//        .isDeleted(false);
//  }
//
//  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoBuilder2() {
//    return CharacterImportanceDto.builder()
//        .id(GENERIC_ID2)
//        .importance(GENERIC_VALUE2)
//        .isDeleted(false);
//  }

  public static CharacterProfile.CharacterProfileBuilder characterProfileBuilder() {
    return CharacterProfile.builder()
        .id(GENERIC_ID)
        .characterName(CHAR_NAME)
        .title("Mr")
        .firstName(FIRST_NAME)
        .middleName("X")
        .surname(SURNAME)
        .characterGender(characterGenderBuilder().build())
        .nickName("Joe")
        .characterImportance(characterImportanceBuilder().build())
        .dateOfBirth(PAST_DATE)
        .functionInStory(PARA)
        .innerGoal(PARA)
        .outerGoal(PARA2)
        .physicalDescription(PARA2);
  }

  public static CharacterProfile.CharacterProfileBuilder characterProfileBuilder2() {
    return CharacterProfile.builder()
        .id(GENERIC_ID2)
        .characterName(CHAR_NAME2)
        .title("Miss")
        .firstName(FIRST_NAME2)
        .middleName("X")
        .surname(SURNAME2)
        .characterGender(characterGenderBuilder().build())
        .nickName("Janie")
        .characterImportance(characterImportanceBuilder2().build())
        .dateOfBirth(PAST_DATE)
        .functionInStory(PARA2)
        .innerGoal(PARA2)
        .outerGoal(PARA)
        .physicalDescription(PARA);
  }

  public static CharacterProfileDto.CharacterProfileDtoBuilder characterProfileDtoCreateBuilder() {
    return CharacterProfileDto.builder()
        .characterName(CHAR_NAME)
        .title("Mr")
        .firstName(FIRST_NAME)
        .middleName("X")
        .surname(SURNAME)
        .gender(characterGenderDtoBuilder())
        .nickName("Joe")
        .characterImportance(characterImportanceDtoBuilder())
        .dateOfBirth(PAST_DATE)
        .functionInStory(PARA)
        .innerGoal(PARA)
        .outerGoal(PARA2)
        .physicalDescription(PARA2);
  }

  public static CharacterProfileDto.CharacterProfileDtoBuilder characterProfileDtoBuilder() {
    return CharacterProfileDto.builder()
        .id(GENERIC_ID)
        .characterName(CHAR_NAME)
        .title("Mr")
        .firstName(FIRST_NAME)
        .middleName("X")
        .surname(SURNAME)
        .gender(characterGenderDtoBuilder())
        .nickName("Joe")
        .characterImportance(characterImportanceDtoBuilder())
        .dateOfBirth(PAST_DATE)
        .functionInStory(PARA)
        .innerGoal(PARA)
        .outerGoal(PARA2)
        .physicalDescription(PARA2);
  }

  public static CharacterProfileDto.CharacterProfileDtoBuilder characterProfileDtoBuilder2() {
    return CharacterProfileDto.builder()
        .id(GENERIC_ID2)
        .characterName(CHAR_NAME2)
        .title("Miss")
        .firstName(FIRST_NAME2)
        .middleName("X")
        .surname(SURNAME2)
        .gender(characterGenderDtoBuilder())
        .nickName("Janie")
        .characterImportance(characterImportanceDtoBuilder2())
        .dateOfBirth(PAST_DATE)
        .functionInStory(PARA2)
        .innerGoal(PARA2)
        .outerGoal(PARA)
        .physicalDescription(PARA);
  }

  public static Book.BookBuilder bookBuilder() {
    return Book.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE)
        .characterProfiles(List.of(TestEntities.characterProfileBuilder().build(),
            TestEntities.characterProfileBuilder2().build()));
  }
  public static Book.BookBuilder bookNewBuilder() {
    return Book.builder()
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE);
  }

  public static Book.BookBuilder bookSmallerBuilder() {
    return Book.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME);
  }

  public static Book.BookBuilder bookBuilderNoChars() {
    return Book.builder()
        .id(GENERIC_ID2)
        .name(GENERIC_VALUE2)
        .description(PARA2)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE);

  }

  public static BookDto.BookDtoBuilder bookDtoBuilder() {
    return BookDto.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE)
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));

  }

  public static BookDto.BookDtoBuilder bookDtoBuilder2() {
    return BookDto.builder()
        .id(GENERIC_ID2)
        .name(GENERIC_VALUE2)
        .description(PARA2)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE)
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));
  }

  public static BookDto.BookDtoBuilder bookBuilderDtoOneChars() {
    return BookDto.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE)
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build()));

  }

  public static BookDto.BookDtoBuilder bookDtoSmallerBuilder() {
    return BookDto.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME);

  }

  public static BookDto.BookDtoBuilder bookDtoCreateBuilder() {
    return BookDto.builder()
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(BookState.ACTIVE)
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));

  }

}
