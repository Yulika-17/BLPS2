package com.nullnumber1.lab1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_document")
public class PaymentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "payer_inn")
    private Long payerInn;
    @Column(name = "payee_inn")
    private Long payeeInn;
    @Column(name = "organization_oktmo")
    private String organizationOktmo;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "date_of_payment")
    private LocalDate dateOfPayment;
    @Column(name = "payment_type")
    private String paymentType;
}
