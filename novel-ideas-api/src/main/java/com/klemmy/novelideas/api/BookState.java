package com.klemmy.novelideas.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

@Getter
@Schema(type= "string", allowableValues = {"ACTIVE", "ON_HOLD", "FINISHED", "ARCHIVED"} , description = "BookState")
public enum BookState {
  ACTIVE("Active"),
  ON_HOLD("On Hold"),
  FINISHED("Finished"),
  ARCHIVED("Archived");

  private final String displayValue;

  BookState(@NonNull final String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return this.displayValue;
  }

  public static BookState getEnum(String displayValue) {
    return Arrays.stream(values()).filter(e ->
        e.toString().equalsIgnoreCase(displayValue)
    ).findAny().orElseThrow(() ->
        new IllegalArgumentException(String.format("The value of: %s is not found.", displayValue))
    );
  }

}
