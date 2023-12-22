package fr.insa.ListService.model;

public class Eval {
    private int id;
    private static float note;

    public Eval(int id, float note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return this.id;
    }
    public static float getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNote(float note) {
        this.note = note;
    }
}
