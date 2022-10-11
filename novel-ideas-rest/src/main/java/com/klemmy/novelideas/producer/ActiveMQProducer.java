package com.klemmy.novelideas.producer;

import com.klemmy.novelideas.api.BookDto;
import com.klemmy.novelideas.config.MessageBusProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@EnableConfigurationProperties(MessageBusProperties.class)
@ConditionalOnProperty(name = "message-bus.type", havingValue = "activemq")
public class ActiveMQProducer implements MessageBus {

  private final JmsTemplate jmsTemplate;
  private final MessageBusProperties messageBusProperties;

  public void sendMessage(BookDto message){
    try{
      log.info("Attempting Send message to Topic: "+ messageBusProperties.topic());
      jmsTemplate.convertAndSend(messageBusProperties.topic(), message);
    } catch(Exception e){
      log.error("Received Exception during send Message: ", e);
    }
  }

}
