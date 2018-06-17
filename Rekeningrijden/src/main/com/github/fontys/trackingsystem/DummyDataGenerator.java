package com.github.fontys.trackingsystem;

import com.github.fontys.entities.payment.*;
import com.github.fontys.entities.region.BorderLocation;
import com.github.fontys.entities.region.Region;
import com.github.fontys.entities.vehicle.EnergyLabel;
import com.github.fontys.trackingsystem.dao.interfaces.*;
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

            if(i == 0){
                ForeignBill foreignBill = new ForeignBill(
                        rv,
                        new BigDecimal(i * 213),
                        new BigDecimal(i * 437),
                        startdate,
                        endDate,
                        PaymentStatus.OPEN,
                        3200,
                        false, "NL");
                em.persist(foreignBill);
            }

            em.persist(b);
            accountDAO.create(account);
            em.persist(tv);
        }

        Route route = new Route(Calendar.getInstance(), Calendar.getInstance(), null,  1.0d, new BigDecimal(1.0));
        em.persist(route);
        RouteDetail routeDetail = new RouteDetail(Calendar.getInstance(), Calendar.getInstance(), 0.7d, new BigDecimal(0.5), rateDAO.findDefaultRates().get(0));
        RouteDetail routeDetail2 = new RouteDetail(Calendar.getInstance(), Calendar.getInstance(), 0.3d, new BigDecimal(0.5), rateDAO.findDefaultRates().get(1));

        List<RouteDetail> details = new ArrayList<>();
        details.add(routeDetail);
        details.add(routeDetail2);
        route.setRouteDetails(details);
        em.persist(route);
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

    public List<Location> generateRuhrBerlin(){
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(51.498724d,7.468356d, new GregorianCalendar(2012, 2, 1, 7, 30)));
        locations.add(new Location(51.500774d,7.484463d, new GregorianCalendar(2012, 2, 1, 7, 34)));
        locations.add(new Location(51.503833d,7.503406d, new GregorianCalendar(2012, 2, 1, 7, 36)));
        locations.add(new Location(51.50367d,7.515332d, new GregorianCalendar(2012, 2, 1, 7, 45)));
        locations.add(new Location(51.509558d,7.550977d, new GregorianCalendar(2012, 2, 1, 7, 47)));
        locations.add(new Location(51.509351d,7.556183d, new GregorianCalendar(2012, 2, 1, 7, 48)));
        locations.add(new Location(51.505558d,7.580894d, new GregorianCalendar(2012, 2, 1, 7, 52)));
        locations.add(new Location(51.50782d,7.602183d, new GregorianCalendar(2012, 2, 1, 7, 55)));
        locations.add(new Location(51.52179d,7.649905d, new GregorianCalendar(2012, 2, 1, 7, 59)));
        locations.add(new Location(51.520386d,7.691604d, new GregorianCalendar(2012, 2, 1, 8, 2)));
        locations.add(new Location(51.528449d,7.733748d, new GregorianCalendar(2012, 2, 1, 8, 5)));
        locations.add(new Location(51.52442d,7.787681d, new GregorianCalendar(2012, 2, 1, 8, 12)));
        locations.add(new Location(51.531549d,7.82012d, new GregorianCalendar(2012, 2, 1, 8, 13)));
        locations.add(new Location(51.533657d,7.84153d, new GregorianCalendar(2012, 2, 1, 8, 15)));
        locations.add(new Location(51.532536d,7.87149d, new GregorianCalendar(2012, 2, 1, 8, 19)));
        locations.add(new Location(51.53545d,7.909125d, new GregorianCalendar(2012, 2, 1, 8, 22)));
        locations.add(new Location(51.536144d,7.984119d, new GregorianCalendar(2012, 2, 1, 8, 26)));
        locations.add(new Location(51.541525d,8.020308d, new GregorianCalendar(2012, 2, 1, 8, 33)));
        locations.add(new Location(51.546889d,8.060785d, new GregorianCalendar(2012, 2, 1, 8, 34)));
        locations.add(new Location(51.544761d,8.111917d, new GregorianCalendar(2012, 2, 1, 8, 37)));
        locations.add(new Location(51.554812d,8.170205d, new GregorianCalendar(2012, 2, 1, 8, 39)));
        locations.add(new Location(51.580683d,8.260228d, new GregorianCalendar(2012, 2, 1, 8, 42)));
        locations.add(new Location(51.592031d,8.472083d, new GregorianCalendar(2012, 2, 1, 8, 48)));
        locations.add(new Location(51.597052d,8.516347d, new GregorianCalendar(2012, 2, 1, 8, 50)));
        locations.add(new Location(51.597219d,8.5851d, new GregorianCalendar(2012, 2, 1, 8, 51)));
        locations.add(new Location(51.582221d,8.71903d,new GregorianCalendar(2012, 2, 1, 8, 56) ));
        locations.add(new Location(51.632532d,8.722297d, new GregorianCalendar(2012, 2, 1, 9, 1)));
        locations.add(new Location(51.664466d,8.707696d, new GregorianCalendar(2012, 2, 1, 9, 3)));
        locations.add(new Location(51.678469d,8.713878d, new GregorianCalendar(2012, 2, 1, 9, 5)));
        locations.add(new Location(51.704654d,8.705308d, new GregorianCalendar(2012, 2, 1, 9, 6)));
        locations.add(new Location(51.819971d,8.701273d, new GregorianCalendar(2012, 2, 1, 9, 7)));
        locations.add(new Location(51.833844d,8.69045d, new GregorianCalendar(2012, 2, 1, 9, 8)));
        locations.add(new Location(51.85515d,8.66831d, new GregorianCalendar(2012, 2, 1, 9, 10)));
        locations.add(new Location(51.913426d,8.618095d, new GregorianCalendar(2012, 2, 1, 9, 12)));
        locations.add(new Location(51.924306d,8.607209d, new GregorianCalendar(2012, 2, 1, 9, 13)));
        locations.add(new Location(51.924817d,8.590218d, new GregorianCalendar(2012, 2, 1, 9, 14)));
        locations.add(new Location(51.943019d,8.546746d, new GregorianCalendar(2012, 2, 1, 9, 17)));
        locations.add(new Location(51.947351d,8.553513d, new GregorianCalendar(2012, 2, 1, 9, 18)));
        locations.add(new Location(51.972354d,8.61376d, new GregorianCalendar(2012, 2, 1, 9, 21)));
        locations.add(new Location(51.988685d,8.615729d, new GregorianCalendar(2012, 2, 1, 9, 22)));
        locations.add(new Location(52.008917d,8.627538d, new GregorianCalendar(2012, 2, 1, 9, 22)));
        locations.add(new Location(52.043311d,8.650761d, new GregorianCalendar(2012, 2, 1, 9, 23)));
        locations.add(new Location(52.075717d,8.671793d, new GregorianCalendar(2012, 2, 1, 9, 25)));
        locations.add(new Location(52.108315d,8.729962d, new GregorianCalendar(2012, 2, 1, 9, 27)));
        locations.add(new Location(52.120821d,8.737068d, new GregorianCalendar(2012, 2, 1, 9, 29)));
        locations.add(new Location(52.130007d,8.736981d, new GregorianCalendar(2012, 2, 1, 9, 30)));
        locations.add(new Location(52.136924d,8.746358d, new GregorianCalendar(2012, 2, 1, 9, 31)));
        locations.add(new Location(52.141148d,8.77567d, new GregorianCalendar(2012, 2, 1, 9, 33)));
        locations.add(new Location(52.151506d,8.806616d, new GregorianCalendar(2012, 2, 1, 9, 36)));
        locations.add(new Location(52.204009d,8.83942d, new GregorianCalendar(2012, 2, 1, 9, 37)));
        locations.add(new Location(52.211195d,8.867687d, new GregorianCalendar(2012, 2, 1, 9, 38)));
        locations.add(new Location(52.216675d,8.964473d, new GregorianCalendar(2012, 2, 1, 9, 44)));
        locations.add(new Location(52.222218d,9.020398d, new GregorianCalendar(2012, 2, 1, 9, 47)));
        locations.add(new Location(52.218113d,9.050724d, new GregorianCalendar(2012, 2, 1, 9, 49)));
        locations.add(new Location(52.22153d,9.068235d, new GregorianCalendar(2012, 2, 1, 9, 50)));
        locations.add(new Location(52.220498d,9.092522d, new GregorianCalendar(2012, 2, 1, 9, 51)));
        locations.add(new Location(52.221247d,9.110329d, new GregorianCalendar(2012, 2, 1, 9, 53)));
        locations.add(new Location(52.219933d,9.120373d, new GregorianCalendar(2012, 2, 1, 9, 53)));
        locations.add(new Location(52.220723d,9.129641d, new GregorianCalendar(2012, 2, 1, 9, 54)));
        locations.add(new Location(52.215915d,9.169139d, new GregorianCalendar(2012, 2, 1, 9, 57)));
        locations.add(new Location(52.23798d,9.276573d, new GregorianCalendar(2012, 2, 1, 10, 2)));
        locations.add(new Location(52.412986d,9.516537d, new GregorianCalendar(2012, 2, 1, 10, 13)));
        locations.add(new Location(52.421069d,9.630516d, new GregorianCalendar(2012, 2, 1, 10, 37)));
        locations.add(new Location(52.336923d,10.23959d, new GregorianCalendar(2012, 2, 1, 10, 47)));
        locations.add(new Location(52.25335d,10.997346d, new GregorianCalendar(2012, 2, 1, 11, 1)));
        locations.add(new Location(52.167694d,11.544265d, new GregorianCalendar(2012, 2, 1, 11, 47)));
        locations.add(new Location(52.233073d,11.895736d, new GregorianCalendar(2012, 2, 1, 11, 54)));
        locations.add(new Location(52.277213d,12.431507d, new GregorianCalendar(2012, 2, 1, 12, 4)));
        locations.add(new Location(52.341146d,12.515248d, new GregorianCalendar(2012, 2, 1, 12, 8)));
        locations.add(new Location(52.350876d,12.547552d, new GregorianCalendar(2012, 2, 1, 12, 12)));
        locations.add(new Location(52.346805d,12.61671d, new GregorianCalendar(2012, 2, 1, 12, 14)));
        locations.add(new Location(52.334654d,12.815106d, new GregorianCalendar(2012, 2, 1, 12, 21)));
        locations.add(new Location(52.397479d,12.851049d, new GregorianCalendar(2012, 2, 1, 12, 25)));
        locations.add(new Location(52.425356d,12.92914d, new GregorianCalendar(2012, 2, 1, 12, 30)));
        locations.add(new Location(52.466258d,12.942101d, new GregorianCalendar(2012, 2, 1, 12, 32)));
        locations.add(new Location(52.53843d,12.97875d, new GregorianCalendar(2012, 2, 1, 12, 34)));
        locations.add(new Location(52.507111d,13.235397d, new GregorianCalendar(2012, 2, 1, 12, 43)));
        locations.add(new Location(52.510957d,13.297467d, new GregorianCalendar(2012, 2, 1, 12, 47)));
        locations.add(new Location(52.513189d,13.332009d, new GregorianCalendar(2012, 2, 1, 12, 52)));
        locations.add(new Location(52.515872d,13.37296d, new GregorianCalendar(2012, 2, 1, 12, 59)));
        locations.add(new Location(52.519062d,13.402757d, new GregorianCalendar(2012, 2, 1, 13, 3)));
        locations.add(new Location(52.554425d,13.467784d, new GregorianCalendar(2012, 2, 1, 13, 7)));
        locations.add(new Location(52.589267d,13.487361d, new GregorianCalendar(2012, 2, 1, 13, 12)));
        locations.add(new Location(52.614866d,13.559798d, new GregorianCalendar(2012, 2, 1, 13, 14)));
        locations.add(new Location(52.758208d,13.545122d, new GregorianCalendar(2012, 2, 1, 13, 15)));
        return locations;
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
