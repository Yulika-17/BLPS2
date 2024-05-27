package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.model.Payment;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.service.kafka.ProducerService;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import lombok.AllArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewScheduler {

    private final PaymentService paymentService;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ProducerService producerService;

    @Scheduled(fixedDelay = 50000)
    @SchedulerLock(name = "info_to_admin_lock", lockAtLeastFor = "50s", lockAtMostFor = "55s")
    public void scheduleReview() {
        List<Payment> payments = paymentService.getPayments();
        if (payments.isEmpty()) return;
        List<User> admins = userRepository.getUsersByRolesContaining(RoleEnum.ADMIN);
        if (admins.isEmpty()) return;

        String message = messageService.getNeedReviewBody(payments);
        admins.forEach(admin -> producerService.send("mail-topic", admin.getEmail(), message));
    }
}


