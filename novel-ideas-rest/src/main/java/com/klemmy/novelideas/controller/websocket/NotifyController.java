//package com.klemmy.novelideas.controller.websocket;
//
//import com.klemmy.novelideas.api.NotifyDto;
//import com.klemmy.novelideas.config.WebSocketConfig;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import java.security.Principal;
//
//@Controller
//@AllArgsConstructor
//public class NotifyController {
//
//  private final SimpMessagingTemplate simpMessagingTemplate;
//
//  @MessageMapping("/notify")
//  public void send(NotifyDto mesg) {
//
//    this.simpMessagingTemplate.convertAndSend("/all/mesgs", mesg);
//
//  }
//// TODO
////  @MessageMapping("/notify")
////  public void sendSpecific(@Payload String mesg, Principal user){
////    this.simpMessagingTemplate.convertAndSendToUser("hh","/specific/msgs",mesg);
////
////  }
//}
