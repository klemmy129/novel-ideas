package com.klemmy.novelideas.producer;

import com.klemmy.novelideas.api.BookDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/*
 This is a stub bean, used when a message bus is not configured.
 */
@Component
@ConditionalOnProperty(name = "message-bus.type", havingValue = "none", matchIfMissing = true)
public class NoMessageBus implements MessageBus {

  @Override
  public void sendMessage(BookDto message) { /* This method is empty, there is no message but configured */ }

}
