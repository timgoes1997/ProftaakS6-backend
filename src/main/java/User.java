public abstract class User {
    private String name;
    private String address;
    private String residency;
    private Role role;
    private Account account;

    public User(String name, String address, String residency, Role role, Account account) {
        this.name = name;
        this.address = address;
        this.residency = residency;
        this.role = role;
        this.account = account;
    }
}
