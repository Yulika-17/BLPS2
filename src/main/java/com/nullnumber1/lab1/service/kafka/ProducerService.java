package com.nullnumber1.lab1.service.kafka;

import com.nullnumber1.lab1.dto.MailDTO;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProducerService {
  private final KafkaTemplate<String, MailDTO> template;

  public void send(String topicName, String userEmail) {
    MailDTO mailDTO = new MailDTO(userEmail, null);
    template.send(topicName, mailDTO);
  }

  public void send(String topicName, String userEmail, String message) {
    MailDTO mailDTO = new MailDTO(userEmail, message);
    template.send(topicName, mailDTO);
  }
}
