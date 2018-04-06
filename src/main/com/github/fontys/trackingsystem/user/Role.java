package com.github.fontys.trackingsystem.user;

import com.github.fontys.security.base.ESRole;

public enum Role implements ESRole{
    CUSTOMER("CUSTOMER"),
    SYSTEM_ADMINISTRATOR("SYSTEM_ADMINISTRATOR"),
    GOVERNMENT_EMPLOYEE("GOVERNMENT_EMPLOYEE"),
    KILOMETER_TRACKER("KILOMETER_TRACKER"),
    POLICE_EMPLOYEE("POLICE_EMPLOYEE"),
    BILL_ADMINISTRATOR("BILL_ADMINISTRATOR");

    public String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getValue()
    {
        return this.value;
    }
}
