package com.nullnumber1.lab1.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    CUSTOMER(Set.of(
            PrivilegeEnum.CREATE_PAYMENT, PrivilegeEnum.VIEW_PAYMENT_STATUS, PrivilegeEnum.FILL_PAYMENT_TYPE_AMOUNT, PrivilegeEnum.FILL_OKTMO, PrivilegeEnum.FILL_PAYER_INFO, PrivilegeEnum.FILL_PAYEE_INFO, PrivilegeEnum.PROCESS_PAYMENT)),

    ADMIN(Set.of(PrivilegeEnum.VIEW_PAYMENT_DOCUMENT, PrivilegeEnum.APPROVE_OR_REJECT_PAYMENT, PrivilegeEnum.REGISTER_OTHER_ADMINS, PrivilegeEnum.GIVE_NEW_ROLE));

    private final Set<PrivilegeEnum> authorities;

    public Set<PrivilegeEnum> getAuthorities() {
        return authorities;
    }
}

