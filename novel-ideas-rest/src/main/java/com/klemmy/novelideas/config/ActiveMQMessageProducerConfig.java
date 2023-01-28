package com.klemmy.novelideas.config;

import jakarta.jms.JMSException;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;

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
  @ConditionalOnProperty(name = "message-bus.type", havingValue = "activemq")
  public ConnectionFactory producerActiveMQConnectionFactory() throws JMSException {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(this.messageBusProperties.brokerUrl());
    // TODO I thing this should be in consumer
    activeMQConnectionFactory.setDeserializationWhiteList("com.klemmy.novelideas.service");

    return activeMQConnectionFactory;
  }

  @Bean
  @ConditionalOnProperty(name = "message-bus.type", havingValue = "activemq")
  public JmsTemplate jmsTemplate() throws JMSException {
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setConnectionFactory(producerActiveMQConnectionFactory());
    jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
    return jmsTemplate;
  }

}
