package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.model.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    public String getReviewTitle() {
        return "Напоминание о ревью!";
    }

    public String getModeratorTitle() {
        return "Твой платёж оценил администратор";
    }
    public String getNeedReviewBody(List<Payment> payments) {
        StringBuilder message = new StringBuilder("Добрый вечер, администратор!\n");
        message.append("Пора бы уже проверить вот эти платежи:\n");
        message.append("Id\n");
        for (Payment payment : payments) {
            message.append(payment.getId()).append("\n");
        }
        return String.valueOf(message);
    }
        public String getModerBody() {
            return "Пора бы зайти и посмотреть что там!!!";
        }
}