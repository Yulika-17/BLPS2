package com.nullnumber1.lab1.service.kafka;

import com.nullnumber1.lab1.dto.MailDTO;
import com.nullnumber1.lab1.service.EmailService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "mail-topic", groupId ="1")
    public void listenTopic(ConsumerRecord<String, MailDTO> record) {
        MailDTO mailDTO = record.value();
        String userEmail = mailDTO.getUserEmail();
        String message = mailDTO.getMessage();
        if (message == null || message.isEmpty()) {
            emailService.sendModeratorMessage(userEmail);
        } else {
            emailService.sendReviewNotification(userEmail, message);
        }
    }
}
