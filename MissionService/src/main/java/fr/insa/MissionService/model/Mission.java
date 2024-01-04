package fr.insa.MissionService.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Mission {

    public enum StateMission {
        WAITING,
        WAITING_FOR_VALIDATION,
        IN_PROGRESS,
        DONE
    }
    private int id;
    private StateMission state;
    private String title;
    private String description;
    private int helper;
    private int requester;
    private int validator;

    @JsonCreator
    public Mission(StateMission state, String title, String description, int helper) {
        this.state = state;
        this.title = title;
        this.description = description;
        this.helper = helper;
    }

    @JsonCreator
    public Mission(StateMission state, String title, String description, int requester, int validator) {
        this.state = state;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.validator = validator;
    }


    public int getId() {
        return id;
    }

    public StateMission getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getHelper() {
        return helper;
    }

    public int getRequester() {
        return requester;
    }

    public int getValidator() {
        return validator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(StateMission state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHelper(int helper) {
        this.helper = helper;
    }

    public void setRequester(int requester) {
            this.requester = requester;
        }

    public void setValidator(int validator) {
            this.validator = validator;
        }
}
