package fr.insa.UserService.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class User {

    public enum Role {
        Helper,
        Requester,
        Validator,
        Admin
    }

    private int id;
    private String pseudo;
    private String firstname;
    private String lastname;
    private Role role;
    private String validatorPseudo;
    // private Float score;

    @JsonCreator
    public User(int id, String pseudo,String firstname, String lastname, Role role) {
        this.id = id;
        this.pseudo = pseudo;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    @JsonCreator
    public User(int id, String pseudo,String firstname, String lastname, Role role, String validatorPseudo) {
        this.id = id;
        this.pseudo = pseudo;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.validatorPseudo = validatorPseudo;
    }

    public int getId() {
        return this.id;
    }
    public String getPseudo() {
        return this.pseudo;
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
    public String getValidatorPseudo() {
        return this.validatorPseudo;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
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
    public void setValidatorPseudo(String validatorPseudo) {
        this.validatorPseudo = validatorPseudo;
    }
}
