package com.nullnumber1.lab1.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import javax.transaction.SystemException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionsConfig {
  @Bean
  public bitronix.tm.Configuration transactionManagerServices() {
    bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();

    String activeProfile = System.getProperty("spring.profiles.active");

    if ("master".equals(activeProfile)) {
      configuration.setServerId("masterServer");
      configuration.setLogPart1Filename("btm1-master.log");
      configuration.setLogPart2Filename("btm2-master.log");
    } else if ("slave".equals(activeProfile)) {
      configuration.setServerId("slaveServer");
      configuration.setLogPart1Filename("btm1-slave.log");
      configuration.setLogPart2Filename("btm2-slave.log");
    } else {
      throw new IllegalStateException("Неизвестный профиль: " + activeProfile);
    }

    return configuration;
  }

  @Bean(name = "bitronixTransactionManager")
  public BitronixTransactionManager transactionManager(bitronix.tm.Configuration _c) {
    BitronixTransactionManager trans = TransactionManagerServices.getTransactionManager();
    try {
      trans.setTransactionTimeout(60);
    } catch (SystemException e) {
      throw new RuntimeException(e);
    }
    return trans;
  }
}
