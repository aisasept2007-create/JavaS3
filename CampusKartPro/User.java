package store;

public abstract class User {
    private final int id;
    private final String username;
    public User(int id, String username) { this.id = id; this.username = username; }
    public int getId() { return id; }
    public String getUsername() { return username; }
    public abstract String getRole();
}
class AdminUser extends User {
    public AdminUser(int id, String username) { super(id, username); }
    @Override public String getRole() { return "ADMIN"; }
}
class StaffUser extends User {
    public StaffUser(int id, String username) { super(id, username); }
    @Override public String getRole() { return "STAFF"; }
}
