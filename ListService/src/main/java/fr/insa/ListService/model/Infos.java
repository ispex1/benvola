package fr.insa.ListService.model;

public class Infos {
    private int id;
    private static String firstname;
    private static String lastname;
    private static String birthdate;

    public Infos(int id, String firstname, String lastname, String birthdate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public int getId() {
        return this.id;
    }
    public static String getFirstname() {
        return firstname;
    }
    public static String getLastname() {
        return lastname;
    }
    public static String getBirthdate() {
        return birthdate;
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
