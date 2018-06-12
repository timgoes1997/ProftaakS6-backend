package com.github.fontys.trackingsystem.services.beans.basic;

import com.github.fontys.entities.user.Role;

public interface RestrictedService {
    default Role getCurrentPrivilege() {
        return Role.NONE;
    }
}
