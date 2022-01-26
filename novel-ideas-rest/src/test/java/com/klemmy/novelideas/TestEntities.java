package com.klemmy.novelideas;

import com.klemmy.novelideas.api.CharacterImportanceDto;
import com.klemmy.novelideas.api.CharacterProfileDto;
import com.klemmy.novelideas.api.CharacterGenderDto;
import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.api.BookStateDto;
import com.klemmy.novelideas.jpa.Book;
import com.klemmy.novelideas.jpa.CharacterImportance;
import com.klemmy.novelideas.jpa.CharacterProfile;
import com.klemmy.novelideas.jpa.CharacterGender;
import com.klemmy.novelideas.jpa.BookState;
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
  public static final String CHAR_NAME2 = "Jane Smith";
  public static final String FIRST_NAME2 = "Jane";
  public static final String SURNAME2 = "Smith";
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
        .gender(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static CharacterGender.CharacterGenderBuilder characterGenderBuilder2() {
    return CharacterGender.builder()
        .id(GENERIC_ID2)
        .gender(GENERIC_VALUE2)
        .isDeleted(false);
  }

  public static CharacterGenderDto.CharacterGenderDtoBuilder characterGenderDtoCreateBuilder() {
    return CharacterGenderDto.builder()
        .gender(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static CharacterGenderDto.CharacterGenderDtoBuilder characterGenderDtoBuilder() {
    return CharacterGenderDto.builder()
        .id(GENERIC_ID)
        .gender(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static CharacterGenderDto.CharacterGenderDtoBuilder characterGenderDtoBuilder2() {
    return CharacterGenderDto.builder()
        .id(GENERIC_ID2)
        .gender(GENERIC_VALUE2)
        .isDeleted(false);
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

  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoCreateBuilder() {
    return CharacterImportanceDto.builder()
        .importance(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoBuilder() {
    return CharacterImportanceDto.builder()
        .id(GENERIC_ID)
        .importance(GENERIC_VALUE)
        .isDeleted(false);
  }
  public static CharacterImportanceDto.CharacterImportanceDtoBuilder characterImportanceDtoBuilder2() {
    return CharacterImportanceDto.builder()
        .id(GENERIC_ID2)
        .importance(GENERIC_VALUE2)
        .isDeleted(false);
  }

  public static BookState.BookStateBuilder bookStateBuilder() {
    return BookState.builder()
        .id(GENERIC_ID)
        .state(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static BookState.BookStateBuilder bookStateBuilder2() {
    return BookState.builder()
        .id(GENERIC_ID2)
        .state(GENERIC_VALUE2)
        .isDeleted(false);
  }

  public static BookStateDto.BookStateDtoBuilder bookStateDtoCreateBuilder() {
    return BookStateDto.builder()
        .state(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static BookStateDto.BookStateDtoBuilder bookStateDtoBuilder() {
    return BookStateDto.builder()
        .id(GENERIC_ID)
        .state(GENERIC_VALUE)
        .isDeleted(false);
  }

  public static BookStateDto.BookStateDtoBuilder bookStateDtoBuilder2() {
    return BookStateDto.builder()
        .id(GENERIC_ID2)
        .state(GENERIC_VALUE2)
        .isDeleted(false);
  }

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
        .gender(characterGenderDtoBuilder().build())
        .nickName("Joe")
        .characterImportance(characterImportanceDtoBuilder().build())
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
        .gender(characterGenderDtoBuilder().build())
        .nickName("Joe")
        .characterImportance(characterImportanceDtoBuilder().build())
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
        .gender(characterGenderDtoBuilder().build())
        .nickName("Janie")
        .characterImportance(characterImportanceDtoBuilder2().build())
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
        .state(bookStateBuilder().build())
        .characterProfiles(List.of(TestEntities.characterProfileBuilder().build(),
            TestEntities.characterProfileBuilder2().build()));
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
        .state(bookStateBuilder().build());

  }

  public static BookDto.BookDtoBuilder bookDtoBuilder() {
    return BookDto.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(bookStateDtoBuilder().build())
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));

  }
  public static BookDto.BookDtoBuilder bookDtoBuilder2() {
    return BookDto.builder()
        .id(GENERIC_ID2)
        .name(GENERIC_VALUE2)
        .description(PARA2)
        .startDate(PAST_DATETIME)
        .state(bookStateDtoBuilder().build())
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));
  }

  public static BookDto.BookDtoBuilder bookBuilderDtoOneChars() {
    return BookDto.builder()
        .id(GENERIC_ID)
        .name(GENERIC_VALUE)
        .description(PARA)
        .startDate(PAST_DATETIME)
        .state(bookStateDtoBuilder().build())
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
        .state(bookStateDtoBuilder().build())
        .characterProfiles(List.of(TestEntities.characterProfileDtoBuilder().build(),
            TestEntities.characterProfileDtoBuilder2().build()));

  }

}
