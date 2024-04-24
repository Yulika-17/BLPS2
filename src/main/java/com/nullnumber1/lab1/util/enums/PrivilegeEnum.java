package com.nullnumber1.lab1.util.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PrivilegeEnum implements GrantedAuthority {
    CREATE_PAYMENT, //Создание платежа
    VIEW_PAYMENT_STATUS, //Просмотр статуса платежа
    FILL_PAYMENT_TYPE_AMOUNT, //Заполнение вида и суммы платежа
    FILL_OKTMO, //Заполнение ОКТМО
    FILL_PAYER_INFO, //Заполнение информации о плательщике
    FILL_PAYEE_INFO, //Заполнение информации о получателе
    PROCESS_PAYMENT,//Обработка платежа
    VIEW_PAYMENT_DOCUMENT, //Просмотр платежного документа
    APPROVE_OR_REJECT_PAYMENT, //Может одобрять или отклонять платежи
    REGISTER_OTHER_ADMINS, //Регистрировать других админов
    GIVE_NEW_ROLE; //Может дать новую роль
    @Override
    public String getAuthority() {
        return this.name();
    }

}
