package com.nullnumber1.lab1.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "inn")
  private Long INN;
}
