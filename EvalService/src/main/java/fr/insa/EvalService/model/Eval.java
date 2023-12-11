package fr.insa.EvalService.model;

public class Eval {
    private int id;
    private float note;

    public Eval(int id, float note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return this.id;
    }
    public float getNote() {
        return this.note;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNote(float note) {
        this.note = note;
    }
}
