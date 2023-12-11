package fr.insa.InfoService.model;

public class Infos {
    private int id;
    private String firstname;
    private String lastname;
    private String birthdate;

    public Infos(int id, String firstname, String lastname, String birthdate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
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
    public String getBirthdate() {
        return this.birthdate;
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
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}
