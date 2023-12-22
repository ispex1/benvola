package fr.insa.ListService.model;

public class User {

    private int id;
    private String firstname;
    private String lastname;

    private Float score;

    public User(int id, String firstname, String lastname, Float score) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.score = score;
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

    public Float getScore() {
        return this.score;
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
    public void setScore(Float score) {
        this.score = score;
    }

}
