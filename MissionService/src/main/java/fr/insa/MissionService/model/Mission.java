package fr.insa.MissionService.model;

public class Mission {

    private enum TypeMission {
        REQUEST,
        OFFER
    }
    private enum StateMission {
        WAITING,
        WAITING_FOR_VALIDATION,
        IN_PROGRESS,
        DONE
    }
    private int id;
    private TypeMission type;
    private StateMission state;
    private String title;
    private String description;
    private int idUser;

    public Mission(int id, TypeMission type, StateMission state, String title, String description, int idUser) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.title = title;
        this.description = description;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public TypeMission getType() {
        return type;
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

    public int getIdUser() {
        return idUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TypeMission type) {
        this.type = type;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

}
