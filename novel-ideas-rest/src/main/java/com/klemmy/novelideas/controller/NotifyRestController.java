package com.klemmy.novelideas.controller;

import com.klemmy.novelideas.api.NotifyDto;
import com.klemmy.novelideas.config.WebSocketConfig;
import com.klemmy.novelideas.controller.websocket.NotifyController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@ConditionalOnClass(NotifyController.class)
@AllArgsConstructor
@RequestMapping(value = "/notify", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notify", description = "Send a STOMP message over WebSockets")
public class NotifyRestController {

  private final NotifyController notifyController;

  @PostMapping("/ws")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Send STOMP message", description = "This creates a book that represents a novel or script")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Send"),
      @ApiResponse(responseCode = "400", description = "Invalid")})
  public void send(@RequestBody NotifyDto mesg) {
    notifyController.send(mesg);
  }
}
