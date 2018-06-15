package com.github.fontys.trackingsystem;

import com.github.fontys.entities.payment.Rate;
import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.*;
import com.github.fontys.entities.payment.Bill;
import com.github.fontys.entities.payment.PaymentStatus;
import com.github.fontys.entities.tracking.Hardware;
import com.github.fontys.entities.tracking.Location;
import com.github.fontys.entities.tracking.TrackedVehicle;
import com.github.fontys.entities.transfer.Transfer;
import com.github.fontys.entities.user.Account;
import com.github.fontys.entities.user.Role;
import com.github.fontys.entities.user.User;
import com.github.fontys.entities.vehicle.RegisteredVehicle;
import com.github.fontys.entities.vehicle.FuelType;
import com.github.fontys.entities.vehicle.Vehicle;

import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.util.*;

@Singleton
@Startup
public class DummyDataGenerator {

    private String[] names = {"Piet", "Henk", "Barry", "Jansen", "Jans", "Henry", "Mikal",
            "Michael", "Tim", "Bart", "Edwin", "Goos", "Sara", "Mina", "Merel", "Daniel", "Ella",
            "Els", "Karel", "Ari", "Arie", "Hanz"};

    @PersistenceContext(name = "Proftaak")
    private EntityManager em;

    @Inject
    private VehicleDAO vehicleDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private TradeDAO tradeDAO;

    @Inject
    private RegionDAO regionDAO;

    @Inject
    private RateDAO rateDAO;

    @Inject
    private RegisteredVehicleDAO registeredVehicleDAO;

    @Inject
    private BillDAO billDAO;

    private static int AMOUNT_TO_GENERATE = 20;

    private static int AMOUNT_LOCATIONS_TO_GENERATE = 20;

    @PostConstruct
    public void init() {

        Date date = new Date();

        // Accounts made for special roles
        // email is xxx@admin.com, password is password
        createSpecialAccount("bills", Role.BILL_ADMINISTRATOR);
        createSpecialAccount("government", Role.GOVERNMENT_EMPLOYEE);
        createSpecialAccount("police", Role.POLICE_EMPLOYEE);
        createSpecialAccount("kilometer", Role.KILOMETER_TRACKER);
        createSpecialAccount("admin", Role.SYSTEM_ADMINISTRATOR);

        createRegions(); //berlijn en ruhr gebied.

        for (int i = 0; i < AMOUNT_TO_GENERATE; i++) {

            String name = getRandom(names);

            Vehicle v = new Vehicle(
                    String.format("Brand '%s", i),
                    date,
                    String.format("Model '%s", i),
                    String.format("Edition '%s'", i),
                    FuelType.ELECTRIC,
                    EnergyLabel.A);
            User u = new User(
                    String.format(name, i),
                    String.format("Address %s", i),
                    String.format("GERMANY", i),
                    Role.CUSTOMER);
            u.setVerified(true);


            Account account = new Account(String.format("email%s@gmail.com", i), name, "password");
            account.setUser(u);
            u.setAccount(account);

            RegisteredVehicle rv = new RegisteredVehicle(
                    (long) i,
                    u,
                    String.format("XXX-00%s", i),
                    v,
                    String.format("Proof %s", i));

            Calendar startdate = new GregorianCalendar();
            startdate.set(2018, i, 1);
            Calendar endDate = new GregorianCalendar();
            endDate.set(2018, i, 28);
            TrackedVehicle tv = new TrackedVehicle(rv, new Location(50 + i / 4, 9 + i / 4, startdate), new Hardware((long) 10, "Hwtype"));

            for (int x = 0; x < AMOUNT_LOCATIONS_TO_GENERATE; x++) {
                Calendar newstartdate = new GregorianCalendar();
                newstartdate.set(2018, i, x);
                Location l = new Location(50 + i + x / 4, 9 + i + x / 4, newstartdate);
                tv.setLastLocation(l);
            }

            Bill b = new Bill(
                    rv,
                    new BigDecimal(i * 200),
                    new BigDecimal(i * 400),
                    startdate,
                    endDate,
                    PaymentStatus.OPEN,
                    1000,
                    false
            );

            em.persist(b);
            accountDAO.create(account);
            em.persist(tv);
        }
    }

    private void createRegions() {
        Region berlin = new Region("berlin", getBorderBerlin());
        Region ruhr = new Region("ruhrgebied", getBorderRuhr());
        List<Rate> rates = new ArrayList<>();
        rates.addAll(generateRatesPrice(berlin, new BigDecimal(0.05d), new BigDecimal(0.04d)));
        rates.addAll(generateRatesPrice(ruhr, new BigDecimal(0.04d), new BigDecimal(0.03d)));
        rates.addAll(generateRatesPrice(null, new BigDecimal(0.04d), new BigDecimal(0.04d)));
        rates.forEach(rateDAO::create);

        regionDAO.create(berlin);
        regionDAO.create(ruhr);
    }

    private List<Rate> generateRatesPrice(Region region, BigDecimal startPrice, BigDecimal steps) {
        User authorizer = accountDAO.findByEmail("kilometer@admin.com").getUser();
        return generateRateDaysLabels(region, startPrice, steps, authorizer);
    }

    private List<Rate> generateRateDaysLabels(Region region, BigDecimal startPrice, BigDecimal steps, User authorizer) {
        List<Rate> rates = new ArrayList<>();
        BigDecimal currentPrice = startPrice.round(new MathContext(1));
        currentPrice = currentPrice.setScale(3, BigDecimal.ROUND_HALF_UP);
        for (EnergyLabel label : EnergyLabel.values()) {
            rates.addAll(generateRatesDays(region, currentPrice, label, authorizer));
            currentPrice = currentPrice.add(steps).setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        return rates;
    }

    private List<Rate> generateRatesDays(Region region, BigDecimal price, EnergyLabel label, User authorizer) {
        List<Rate> rates = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            rates.add(new Rate(region, price, label, day.getValue(), 0, 0, day.getValue(), 23, 59, authorizer));
        }
        return rates;
    }

    private List<BorderLocation> getBorderBerlin() {
        BorderLocation r1 = new BorderLocation(13.25d, 52.6d, 1L);
        BorderLocation r2 = new BorderLocation(13.25d, 52.4d, 2L);
        BorderLocation r3 = new BorderLocation(13.6d, 52.4d, 3L);
        BorderLocation r4 = new BorderLocation(13.6d, 52.6d, 4L);
        List<BorderLocation> squareBorderBerlin = new ArrayList<BorderLocation>();
        squareBorderBerlin.add(r1);
        squareBorderBerlin.add(r2);
        squareBorderBerlin.add(r3);
        squareBorderBerlin.add(r4);
        return squareBorderBerlin;
    }

    private List<BorderLocation> getBorderRuhr() {
        BorderLocation r1 = new BorderLocation(6.5d, 51.7d, 1L);
        BorderLocation r2 = new BorderLocation(6.5d, 50.4d, 2L);
        BorderLocation r3 = new BorderLocation(7.8d, 50.4d, 3L);
        BorderLocation r4 = new BorderLocation(7.8d, 52.5d, 4L);
        List<BorderLocation> squareBorderBerlin = new ArrayList<BorderLocation>();
        squareBorderBerlin.add(r1);
        squareBorderBerlin.add(r2);
        squareBorderBerlin.add(r3);
        squareBorderBerlin.add(r4);
        return squareBorderBerlin;
    }

    private void createSpecialAccount(String emailsuffix, Role role) {
        User admin = new User("A. D'min", "Rekeningrijderstraat 241a", "GERMANY", role);
        Account adminAcc = new Account(emailsuffix + "@admin.com", "admin", "password");
        admin.setVerified(true);
        adminAcc.setUser(admin);
        accountDAO.create(adminAcc);
    }

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private void generateSimpleTransfer() {
        Date date = new Date();
        User transferFrom = new User("transferFrom", "trans", "trans", Role.BILL_ADMINISTRATOR);
        Account transferFromAcc = new Account("transfer@from.com", "transferFrom", "transferFrom");
        transferFrom.setVerified(true);
        transferFromAcc.setUser(transferFrom);
        accountDAO.create(transferFromAcc);

        User transferTo = new User("transferTo", "trans", "trans", Role.BILL_ADMINISTRATOR);
        Account transferToAcc = new Account("transfer@to.com", "transferTo", "transferTo");
        transferTo.setVerified(true);
        transferToAcc.setUser(transferTo);
        accountDAO.create(transferToAcc);

        Vehicle trans = new Vehicle(
                String.format("Brand trans"),
                date,
                String.format("Model trans"),
                String.format("Edition trans"),
                FuelType.ELECTRIC,
                EnergyLabel.A);

        RegisteredVehicle rv = new RegisteredVehicle(transferFrom,
                String.format("XXX-FROM"),
                trans,
                String.format("Proof trans"));
        registeredVehicleDAO.create(rv);

        Transfer transfer = new Transfer(transferFrom, rv, "CXCXCXCXCXCX");
        tradeDAO.create(transfer);
    }

    public static int getAmountToGenerate() {
        return AMOUNT_TO_GENERATE;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void setVehicleDAO(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setRegisteredVehicleDAO(RegisteredVehicleDAO registeredVehicleDAO) {
        this.registeredVehicleDAO = registeredVehicleDAO;
    }

    public void setBillDAO(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    public void setTradeDAO(TradeDAO tradeDAO) {
        this.tradeDAO = tradeDAO;
    }

    public void setRegionDAO(RegionDAO regionDAO) {
        this.regionDAO = regionDAO;
    }

    public void setRateDAO(RateDAO rateDAO) {
        this.rateDAO = rateDAO;
    }
}
