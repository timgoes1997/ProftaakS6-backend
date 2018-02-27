package user;

public class Employee extends User{
    private Department department;

    public Employee(String name, String address, String residency, Role role, Department department) {
        super(name, address, residency, role);
        this.department = department;
    }
}
