package com.klemmy.novelideas.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.List;

@Configuration
@EnableJms
@EnableConfigurationProperties(MessageBusProperties.class)
@ConditionalOnProperty(name = "message-bus.type", havingValue = "activemq")
public class ActiveMQMessageProducerConfig {

  private final MessageBusProperties messageBusProperties;

  public ActiveMQMessageProducerConfig(MessageBusProperties messageBusProperties) {
    this.messageBusProperties = messageBusProperties;
  }

  @Bean
  public ConnectionFactory producerActiveMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(this.messageBusProperties.brokerUrl());
    activeMQConnectionFactory.setTrustedPackages(List.of("com.klemmy.novelideas.service"));

    return activeMQConnectionFactory;
  }

  @Bean
  public JmsTemplate jmsTemplate(){
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setConnectionFactory(producerActiveMQConnectionFactory());
    jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
    return jmsTemplate;
  }

}
