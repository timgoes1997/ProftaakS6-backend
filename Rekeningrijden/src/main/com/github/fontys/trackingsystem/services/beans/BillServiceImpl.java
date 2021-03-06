package com.github.fontys.trackingsystem.services.beans;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.github.fontys.security.annotations.inject.CurrentESUser;
import com.github.fontys.entities.security.base.ESUser;
import com.github.fontys.trackingsystem.dao.interfaces.AccountDAO;
import com.github.fontys.trackingsystem.dao.interfaces.BillDAO;
import com.github.fontys.trackingsystem.dao.interfaces.VehicleDAO;
import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.trackingsystem.services.beans.basic.RestrictedServiceImpl;
import com.github.fontys.trackingsystem.services.interfaces.BillService;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.Role;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.Vehicle;
import com.github.fontys.trackingsystem.services.interfaces.LocationService;
import com.github.fontys.trackingsystem.services.interfaces.RegionService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BillServiceImpl extends RestrictedServiceImpl implements BillService {

    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @Inject
    private RegionService regionService;

    @Inject
    private LocationService locationService;

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
    public Bill createBill(Bill bill) {
        billDAO.create(bill);
        return bill;
    }

    @Override
    public List<Bill> getBillsBetweenDatesByVehicleId(long registeredVehicleId, String startDate, String endDate, boolean excludeTotalBill) {

        List<Bill> bills;

        // Parse the time
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;

        // can't parse? Our fault
        try {
            start = parse.parse(startDate);
            end = parse.parse(endDate);
        } catch (ParseException e) {
            throw new BadRequestException();
        }

        bills = this.getBillsByVehicleId((int) registeredVehicleId);

        // filter the map on date, if the map is not empty
        if (!bills.isEmpty()) {
            Iterator<Bill> locIter = bills.iterator();

            // Remove bills that don't fall between start and enddate
            while (locIter.hasNext()) {
                Bill b = locIter.next();
                Date billStartDate = b.getCalendarStartDate().getTime();
                Date billEndDate = b.getCalendarEndDate().getTime();

                // if the date of the Bill falls outside the specified dates, remove the Bill
                if (billStartDate.before(start) ||
                        billStartDate.after(end) ||
                        billEndDate.before(start) ||
                        billEndDate.after(end)) {
                    locIter.remove();
                }

                // If the Bill is an endOfMonthBill while excludeTotalBill == true, remove the Bill
                if (excludeTotalBill && b.isEndOfMonthBill()) {
                    locIter.remove();
                }
            }

            //Sort the list by dates
            Collections.sort(bills, new Comparator<Bill>() {
                public int compare(Bill o1, Bill o2) {
                    return o1.getCalendarStartDate().compareTo(o2.getCalendarStartDate());
                }
            });
        }
        return bills;
    }

    @Override
    public List<Bill> getBillsBetweenDatesByVehicleId(long registeredVehicleId, Calendar startDate, Calendar endDate) {

        List<Bill> bills = this.getBillsByVehicleId((int) registeredVehicleId);

        // filter the map on date, if the map is not empty
        if (!bills.isEmpty()) {
            Iterator<Bill> locIter = bills.iterator();

            // Remove bills that don't fall between start and enddate
            while (locIter.hasNext()) {
                Bill b = locIter.next();
                Date billStartDate = b.getCalendarStartDate().getTime();
                Date billEndDate = b.getCalendarEndDate().getTime();

                // if the date of the Bill falls outside the specified dates, remove the Bill
                if (billStartDate.before(startDate.getTime()) ||
                        billStartDate.after(endDate.getTime()) ||
                        billEndDate.before(startDate.getTime()) ||
                        billEndDate.after(endDate.getTime())) {
                    locIter.remove();
                }
            }
            //Sort the list by dates
            bills.sort(Comparator.comparing(Bill::getCalendarStartDate));
        }
        return bills;
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
        if (!accessAllBillsAllowed()) {
            b = billDAO.findByOwnerId(((User) currentUser).getId());
        } else {
            b = billDAO.getAll();
        }
        return b;
    }

    @Override
    public boolean hasBillRights(Bill b) {
        return // Can access all bills
                accessAllBillsAllowed() || // OR
                        // Owns this bill
                        ((User) currentUser).getId() == b.getRegisteredVehicle().getCustomer().getId();
    }


    @Override
    public List<Rate> getRates(long id) {
        if (!billDAO.exists(id)) {
            throw new NotFoundException("Couldn't find given bill");
        }

        Bill b = billDAO.find(id);
        List<Location> billLocations = locationService.getLocationsBetweenTimesByVehicleLicense(
                b.getLicense(),
                b.getCalendarStartDate().getTime(),
                b.getCalendarEndDate().getTime());

        RegisteredVehicle registeredVehicle = b.getRegisteredVehicle();
        if (registeredVehicle != null) {
            return regionService.getRates(billLocations, b.getRegisteredVehicle().getVehicle().getEnergyLabel());
        } else {
            return regionService.getRates(billLocations, EnergyLabel.H);
        }
    }

    @Override
    public Role getCurrentPrivilege() {
        return (Role) currentUser.getPrivilege();
    }

    private boolean accessAllBillsAllowed() {
        return getDefaultAccessPrivileges();
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

    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }
}
