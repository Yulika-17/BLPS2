package com.nullnumber1.lab1.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payment_type_id", referencedColumnName = "id")
  private PaymentType paymentType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "oktmo_id", referencedColumnName = "id")
  private OKTMO oktmo;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payer_id", referencedColumnName = "id")
  private Payer payer;

  @Column(name = "for_self")
  private Boolean forSelf;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payee_id", referencedColumnName = "id")
  private Payee payee;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "payment_document", referencedColumnName = "id")
  private PaymentDocument paymentDocument;

  @Column(name = "status")
  private String status;
}
