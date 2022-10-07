package com.klemmy.novelideas.producer;

import com.klemmy.novelideas.api.BookDto;

public interface MessageBus {
  default void sendMessage(BookDto message) {

  }
}
