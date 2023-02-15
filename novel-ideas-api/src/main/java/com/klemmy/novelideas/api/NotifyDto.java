package com.klemmy.novelideas.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public record NotifyDto(
    String title,
    String body,
    String from,
    List<String> to,
    Date created
    ) implements Serializable {
}
