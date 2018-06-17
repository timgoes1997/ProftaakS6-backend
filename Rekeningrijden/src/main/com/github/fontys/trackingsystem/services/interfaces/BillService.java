package com.github.fontys.trackingsystem.services.interfaces;

import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.entities.payment.Rate;

import java.util.List;

public interface BillService {
    List<Bill> getBillsByTime(int year, int month);
    List<Bill> getAllBills();
    Bill setBillStatus(int id, String status);
    List<Bill> getBillsByOwnerId(int ownerId);
    List<Bill> getBillsByVehicleId(int vehicleId);

    Bill createBill(Bill bill);

    List<Rate> getRates(long id);

    List<Bill> getBillsBetweenDatesByVehicleId(long registeredVehicleId, String startDate, String endDate, boolean excludeTotalBill);

    List<Bill> getBillsByStatus(String status);
    Bill getBillByID(int id);
    List<Bill> getCurrentUserBills();

    PaymentStatus getPaymentStatusByString(String paymentStatus);

    boolean compareYearAndMonth(Bill b, int year, int month);

    /**
     * Handles found bills -- return 404 if none found, 403 if no rights or a list of bills
     * @param bills
     * @return
     */
    List<Bill> handleFoundBills(List<Bill> bills);

    /**
     * Function to return either ALL or OWN invoices
     * @return
     */
    List<Bill> getMaxAllowedBills();

    /**
     * Checks if a bill belongs to the current user
     * @param b
     * @return
     */
    boolean hasBillRights(Bill b);
}
