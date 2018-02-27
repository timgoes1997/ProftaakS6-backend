package user;

import javax.management.relation.Role;
import java.util.List;

public class Customer extends User{
    private List<CustomerVehicle> customerVehicles;

    public Customer(String name, String address, String residency, Role role) {
        super(name, address, residency, role);
    }
}
