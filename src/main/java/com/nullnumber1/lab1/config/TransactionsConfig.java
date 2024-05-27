package com.nullnumber1.lab1.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.SystemException;

@Configuration
public class TransactionsConfig {
    @Bean
    /*создает и конфигурирует Bitronix Configuration для управления транзакциями.*/
    public bitronix.tm.Configuration transactionManagerServices() {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();

        // Получение активного профиля
        String activeProfile = System.getProperty("spring.profiles.active");

        // Установка уникального serverId и файлов журнала для каждого профиля
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
    /*Этот метод создает и настраивает BitronixTransactionManager, который управляет транзакциями.*/
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
