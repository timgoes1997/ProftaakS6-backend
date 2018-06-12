package com.github.fontys.trackingsystem.services.beans.basic;

public abstract class RestrictedServiceImpl implements RestrictedService{

    protected boolean getDefaultAccessPrivileges()
    {
        switch (getCurrentPrivilege()) {
            case BILL_ADMINISTRATOR:
            case GOVERNMENT_EMPLOYEE:
                return true;
            default:
                return false;
        }
    }
}
