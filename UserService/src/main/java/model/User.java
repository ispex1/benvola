package model;

public class User {

    enum Role {
        Helper,
        Requester,
        Validator,
        Admin
    }

    private int id;
    private String firstname;
    private String lastname;
    private Role role;
    // private Float score;

    public User(int id, String firstname, String lastname, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }
    public String getFirstname() {
        return this.firstname;
    }
    public String getLastname() {
        return this.lastname;
    }
    public Role getRole() {
        return this.role;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
