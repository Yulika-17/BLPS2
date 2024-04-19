package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.exception.PaymentIsNotProcessedException;
import com.nullnumber1.lab1.exception.not_found.InnDoesntExistException;
import com.nullnumber1.lab1.exception.not_found.PaymentNotFoundException;
import com.nullnumber1.lab1.exception.not_found.UserNotFoundException;
import com.nullnumber1.lab1.model.*;
import com.nullnumber1.lab1.repository.InnRepository;
import com.nullnumber1.lab1.repository.PaymentRepository;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final InnRepository innRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, InnRepository innRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.innRepository = innRepository;
    }

    public Long createPayment(Long userId) {
        if (hasIncompletePayments(userId)) {
            return -1L;
        }
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.INITIATED.name());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        payment.setUser(user);
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment.getId();
    }


    private Payment retrievePayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }


    public boolean hasIncompletePayments(Long userId) {
        List<Payment> payments = paymentRepository.findAllByUserId(userId);
        return payments.stream().anyMatch(payment -> !payment.getStatus().equals(PaymentStatus.PROCESSED.name()));
    }


    public PaymentStatus getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return PaymentStatus.valueOf(payment.getStatus());
    }

    public void updatePaymentStatus(Long paymentId, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.setStatus(newStatus.name());
        paymentRepository.save(payment);
    }

    public void updatePaymentType(Long paymentId, String type, Double amount) {
        Payment payment = retrievePayment(paymentId);

        PaymentType paymentType = new PaymentType();
        paymentType.setType(type);
        paymentType.setAmount(new BigDecimal(amount));
        payment.setPaymentType(paymentType);
        payment.setStatus(PaymentStatus.FILLING_TYPE_AMOUNT.name());

        paymentRepository.save(payment);
    }

    public void updateOKTMO(Long paymentId, String code) {
        Payment payment = retrievePayment(paymentId);

        OKTMO oktmo = new OKTMO();
        oktmo.setCode(code);
        payment.setOktmo(oktmo);
        payment.setStatus(PaymentStatus.FILLING_OKTMO.name());

        paymentRepository.save(payment);
    }

    @Transactional
    public PaymentDocument updatePayer(Long paymentId, String name, Long INN, Boolean forSelf) {
        Payment payment = retrievePayment(paymentId);

        Payer payer = new Payer();
        payer.setName(name);
        payer.setINN(INN);
        payment.setPayer(payer);
        payment.setForSelf(forSelf);
        payment.setStatus(PaymentStatus.FILLING_PAYER.name());

        paymentRepository.save(payment);
        if (forSelf) {
            if (innRepository.existsById(payment.getPayer().getINN())) {
                log.info("INN" + payment.getPayer().getINN() + " exists in DB");
                return generatePaymentDocument(payment);
            } else throw new InnDoesntExistException(payment.getPayer().getINN());
        } else {
            if (innRepository.existsById(payment.getPayer().getINN())) {
                log.info("INN" + payment.getPayer().getINN() + " exists in DB");
                return null;
            } else throw new InnDoesntExistException(payment.getPayer().getINN());
        }
    }

    @Transactional
    public PaymentDocument updatePayee(Long paymentId, String name, Long INN) {
        Payment payment = retrievePayment(paymentId);

        Payee payee = new Payee();
        payee.setName(name);
        payee.setINN(INN);
        payment.setPayee(payee);
        payment.setStatus(PaymentStatus.FILLING_PAYEE.name());

        paymentRepository.save(payment);

        if (innRepository.existsById(payment.getPayee().getINN())) {
            log.info("INN" + payment.getPayee().getINN() + " exists in DB");
            return generatePaymentDocument(payment);
        } else throw new InnDoesntExistException(payment.getPayer().getINN());
    }

    private PaymentDocument generatePaymentDocument(Payment payment) {
        PaymentDocument paymentDocument = new PaymentDocument();
        paymentDocument.setId(payment.getId());
        paymentDocument.setPayerInn(payment.getPayer().getINN());
        if (!payment.getForSelf()) {
            paymentDocument.setPayeeInn(payment.getPayee().getINN());
        }
        paymentDocument.setOrganizationOktmo(payment.getOktmo().getCode());
        paymentDocument.setAmount(payment.getPaymentType().getAmount());
        paymentDocument.setPaymentType(payment.getPaymentType().getType());
        paymentDocument.setDateOfPayment(LocalDate.now());
        return paymentDocument;
    }


    public PaymentDocument getPaymentDocument(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (PaymentStatus.valueOf(payment.getStatus()) == PaymentStatus.PROCESSED) {
            return generatePaymentDocument(payment);
        } else {
            throw new PaymentIsNotProcessedException(paymentId, payment.getStatus());
        }
    }

    @Transactional
    public PaymentStatus reviewPayment(Long paymentId, Boolean decision) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (PaymentStatus.valueOf(payment.getStatus()) == PaymentStatus.PROCESSED) {
            if (decision) {
                updatePaymentStatus(paymentId, PaymentStatus.APPROVED);

                PaymentDocument paymentDocument = generatePaymentDocument(payment);
                payment.setPaymentDocument(paymentDocument);
                paymentRepository.save(payment);

                return PaymentStatus.APPROVED;
            } else {
                updatePaymentStatus(paymentId, PaymentStatus.FAILED);
                return PaymentStatus.FAILED;
            }
        } else {
            throw new PaymentIsNotProcessedException(paymentId, payment.getStatus());
        }
    }
    public boolean processPayment(Long paymentId, String paymentMethod) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        Random random = new Random();
        boolean paymentProcessed = random.nextBoolean();

        if (paymentProcessed) {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentNotFoundException(paymentId));
            payment.setStatus(PaymentStatus.PROCESSED.name());
            paymentRepository.save(payment);
            log.info("Payment processed successfully using " + paymentMethod);
        } else {
            log.warn("Payment failed using " + paymentMethod);
        }

        return paymentProcessed;
    }
}
