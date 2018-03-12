package com.github.fontys.trackingsystem.user;

public class Employee extends User{
    private Department department;

    public Employee(String name, String address, String residency, Role role, Department department) {
        super(name, address, residency, role);
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
