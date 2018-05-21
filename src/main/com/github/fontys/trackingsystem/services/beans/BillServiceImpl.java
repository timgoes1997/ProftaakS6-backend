package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.trackingsystem.payment.Bill;
import com.github.fontys.trackingsystem.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.trackingsystem.user.Account;
import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;
import com.github.fontys.trackingsystem.vehicle.Vehicle;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BillServiceImpl implements BillService {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private BillDAO billDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private VehicleDAO vehicleDAO;


    @Override
    public List<Bill> getBillsByTime(int year, int month) {
        List<Bill> bills = new ArrayList<>();
        for (Bill b : getMaxAllowedBills()) {
            boolean result = compareYearAndMonth(b, year, month - 1);
            if (result) {
                bills.add(b);
            }
        }
        return bills;
    }

    @Override
    public List<Bill> getAllBills() {
        return getMaxAllowedBills();
    }

    @Override
    public Bill setBillStatus(int id, String status) {
        // Get single bill by id
        Bill b = billDAO.find(id);

        if (b == null) {
            throw new NotFoundException("Couldn't find given bill");
        }

        if (!hasBillRights(b)) {
            throw new NotAuthorizedException("User has no right to edit this bill");
        }

        if (b.getStatus() == PaymentStatus.PAID) {
            // mag niet naar open of cancelled gaan
            if (status.equals("cancelled") || status.equals("open")) {
                throw new NotAcceptableException("Given status is not acceptable");
            }
        } else if (b.getStatus() == PaymentStatus.CANCELED) {
            // mag niet naar open of cancelled gaan
            if (status.equals("open") || status.equals("cancelled")) {
                throw new NotAcceptableException("Given status is not acceptable");
            }
        }

        // Set new status
        b.setStatus(getPaymentStatusByString(status));

        // Update in db
        billDAO.edit(b);
        return b;
    }

    @Override
    public List<Bill> getBillsByOwnerId(int ownerId) {
        // Check if owner exists
        Account a = accountDAO.find(ownerId);
        if (a == null) {
            // According to swagger endpoint, return 400 because of invalid owner id
            throw new NotFoundException("Couldn't find given account");
        }

        // Owner exists, get bills for owner
        List<Bill> bills = billDAO.findByOwnerId((long) ownerId);

        return handleFoundBills(bills);
    }

    @Override
    public List<Bill> getBillsByVehicleId(int vehicleId) {
        // Check if owner exists
        Vehicle v = vehicleDAO.find(vehicleId);
        if (v == null) {
            // According to swagger endpoint, return 400 because of invalid vehicle id
            throw new NotFoundException("Couldn't find given vehicle");
        }

        // Owner exists, get bills for owner
        List<Bill> bills = billDAO.findByVehicleId(vehicleId);
        return handleFoundBills(bills);
    }

    @Override
    public List<Bill> getBillsByStatus(String status) {
        if (getPaymentStatusByString(status) != null) {
            throw new BadRequestException("Invalid payment status given");
        }

        // Owner exists, get bills for owner
        return handleFoundBills(billDAO.findByStatus(status));
    }

    @Override
    public Bill getBillByID(int id) {
        // Get single bill by id
        Bill b = billDAO.find(id);
        if (b == null) {
            throw new NotFoundException("Bill couldn't be found");
        }

        if (!hasBillRights(b)) {
            throw new NotAuthorizedException("You have no authorization to check this bill");
        }
        return b;
    }

    @Override
    public List<Bill> getCurrentUserBills() {
        return billDAO.findByOwnerId(((User) currentUser).getId());
    }

    @Override
    public PaymentStatus getPaymentStatusByString(String paymentStatus) {
        for (PaymentStatus c : PaymentStatus.values()) {
            if (c.name().equals(paymentStatus)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean compareYearAndMonth(Bill b, int year, int month) {
        System.out.println(String.format("Comparing year %s month %s", year, month));
        // Check if start date is within the same year/month
        Calendar cal1 = b.getCalendarStartDate();
        if (cal1.get(Calendar.YEAR) == year &&
                cal1.get(Calendar.MONTH) == month) {
            return true;
        }
        System.out.println(String.format("Calendar year %s month %s", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)));

        // Check if end date is within the same year/month
        Calendar cal2 = b.getCalendarEndDate();
        if (cal2.get(Calendar.YEAR) == year &&
                cal2.get(Calendar.MONTH) == month) {
            return true;
        }

        // Neither are within the same year/month, return false
        return false;
    }

    @Override
    public List<Bill> handleFoundBills(List<Bill> bills) {
        if (bills.size() > 0) {
            // Check if the current user owns or has rights to the bills
            if (!hasBillRights(bills.get(0))) {
                throw new NotAuthorizedException("User not allowed to view bills");
            }

            return bills;
        } else {
            throw new NotFoundException("You haven't found any bills you may handle");
        }

    }


    @Override
    public List<Bill> getMaxAllowedBills() {
        List<Bill> b;
        // RETURN OWN BILLS FOR;
        // CUSTOMERS
        if (currentUser.getPrivilege() == Role.CUSTOMER) {
            b = billDAO.findByOwnerId(((User) currentUser).getId());
        } else {
            // FOR ANY OTHER ROLE, RETURN ALL
            b = billDAO.getAll();
        }
        return b;
    }

    @Override
    public boolean hasBillRights(Bill b) {
        Role priv = (Role) currentUser.getPrivilege();
        switch (priv) {
            case CUSTOMER:
                // check if you're the owner
                if (((User) currentUser).getId() != b.getRegisteredVehicle().getCustomer().getId()) {
                    // not the owner
                    return false;
                }
                break;
            case BILL_ADMINISTRATOR:
                // allowed. proceed.
                break;
            default:
                return false;
        }
        return true;
    }

    public void setCurrentUser(ESUser currentUser) {
        this.currentUser = currentUser;
    }

    public void setBillDAO(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setVehicleDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }
}
