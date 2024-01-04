package fr.insa.MissionService.controller;

import fr.insa.MissionService.model.Mission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
@RequestMapping("/mission")
public class MissionController {

    @Autowired
    private Connection connection;

    @GetMapping("/test")
    public String test() {
        return "MissionService is working.";
    }

    @GetMapping("/db")
    public void db() throws Exception {
        // Return the name of the tables in the database
        ResultSet rs = connection.createStatement().executeQuery(
                "SHOW TABLES"
        );

        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    @GetMapping("/db/dropTable")
    public void dbDropTable() throws Exception {
        connection.createStatement().executeUpdate(
                "DROP TABLE IF EXISTS Mission"
        );

        System.out.println("Table dropped.");
    }

    @GetMapping("/db/createTable")
    public void dbTable() throws Exception {
        connection.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS Mission (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "state ENUM('WAITING', 'WAITING_FOR_VALIDATION', 'IN_PROGRESS', 'DONE', 'CANCELLED'), " +
                        "title VARCHAR(255), " +
                        "description TEXT, " +
                        "Helper INT, " +
                        "Requester INT, " +
                        "Validator INT)"
        );

        System.out.println("Table created.");
    }

    /*
    @GetMapping("/testInsert")
    public String testInsert() throws Exception {
        Mission mission = new Mission(Mission.StateMission.WAITING, "AIDE", "QUI VEUT MON AIDE ?????? JE SUIS GENTIL", 1);
        //Mission mission2 = new Mission(Mission.StateMission.WAITING_FOR_VALIDATION, "SVP??", "J'AI BESOIN DE VOUS", 3, 2);
        dbInsert(mission);
        //dbInsert(mission2);

        return "Mission inserted.";
    }*/

    @PostMapping("/add")
    public String missionAdd(@RequestBody Mission mission) throws Exception {
        // Insert the mission "mission" in the table Mission
        if (mission.getHelper() != 0) {
            connection.createStatement().executeUpdate(
                "INSERT INTO Mission (state, title, description, Helper) VALUES (" +
                        "'" + mission.getState() + "', " +
                        "'" + mission.getTitle() + "', " +
                        "'" + mission.getDescription() + "', " +
                        "'" + mission.getHelper() + "')"
            );
        }
        else {
            connection.createStatement().executeUpdate(
                "INSERT INTO Mission (state, title, description, Requester, Validator) VALUES (" +
                        "'" + mission.getState() + "', " +
                        "'" + mission.getTitle() + "', " +
                        "'" + mission.getDescription() + "', " +
                        "'" + mission.getRequester() + "', " +
                        "'" + mission.getValidator() + "')"
            );
        }

        return ("The mission " + mission.getTitle() + " has been created.");
    }

    @PostMapping("/delete/{id}")
    public String missionDelete(@PathVariable int id) throws Exception {
        // Delete the mission with the id "id" from the table Mission
        connection.createStatement().executeUpdate(
                "DELETE FROM Mission WHERE id = " + id
        );

        return ("The mission with id " + id + " has been deleted.");
    }

    /* UPDATE STATE */

    @PostMapping("/update/cancelled/{id}/{justification}")
    public String dbUpdateCancelled(@PathVariable int id, @PathVariable String justification) throws Exception {
        // Update the state of the mission with the id "id" to CANCELLED + give a justification in the description of the mission
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET state = 'CANCELLED', description = 'CANCELLED BECAUSE : " + justification + "' WHERE id = " + id
        );

        return ("The mission with id " + id + " has been updated to CANCELLED.");
    }

    @PostMapping("/update/waiting/{id}")
    public String dbUpdateWaiting(@PathVariable int id) throws Exception {
        // Update the state of the mission with the id "id" to WAITING
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET state = 'WAITING' WHERE id = " + id
        );

        return ("The mission with id " + id + " has been updated to WAITING.");
    }

    @PostMapping("/update/inprogress/{id}")
    public String  dbUpdateInProgress(@PathVariable int id) throws Exception {
        // Update the state of the mission with the id "id" to IN_PROGRESS
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET state = 'IN_PROGRESS' WHERE id = " + id
        );

        return ("The mission with id " + id + " has been updated to IN_PROGRESS.");
    }

    @PostMapping("/update/done/{id}")
    public String dbUpdateDone(@PathVariable int id) throws Exception {
        // Update the state of the mission with the id "id" to DONE
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET state = 'DONE' WHERE id = " + id
        );

        return("The mission with id " + id + " has been updated to DONE.");
    }

    /* UPDATE ACTORS */

    @PostMapping("/update/helper/{id}/{helper}")
    public String dbUpdateHelper(@PathVariable int id, @PathVariable int helper) throws Exception {
        // Update the helper of the mission with the id "id" to "helper"
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET Helper = " + helper + " WHERE id = " + id
        );

        return("The mission with id " + id + " has been updated to have helper " + helper + ".");
    }

    @GetMapping("/update/requester/{id}/{requester}")
    public String dbUpdateRequester(@PathVariable int id, @PathVariable int requester) throws Exception {
        // Update the requester of the mission with the id "id" to "requester"
        connection.createStatement().executeUpdate(
                "UPDATE Mission SET Requester = " + requester + " WHERE id = " + id
        );

        return("The mission with id " + id + " has been updated to have requester " + requester + ".");
    }

    /* SHOW MISSIONS */

    @GetMapping("/show/waitingforvalidation/{id}")
    public Mission[] dbShowWaitingValidation(@PathVariable int id) throws Exception {
        // Return the missions waiting for validation of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Requester = " + id + ") AND state = 'WAITING_FOR_VALIDATION'"
        );

        ArrayList<Mission> list = new ArrayList<Mission>();

        while (rs.next()) {
            list.add(new Mission(rs.getInt("id"),
                    Mission.StateMission.valueOf(rs.getString("state")),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("Helper"),
                    rs.getInt("Requester"),
                    rs.getInt("Validator")
            ));
        }

        return list.toArray(new Mission[0]);
    }

    @GetMapping("/show/waiting/{id}")
    public ArrayList<String> dbShowActive(@PathVariable int id) throws Exception {
        // Return the missions in waiting status of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Helper = " + id + " OR Requester = " + id + ") AND state = 'WAITING'"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }

    @GetMapping("/show/inprogress/{id}")
    public ArrayList<String> dbShowInProgress(@PathVariable int id) throws Exception {
        // Return the missions in progress of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Helper = " + id + " OR Requester = " + id + ") AND state = 'IN_PROGRESS'"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }

    @GetMapping("/show/done/{id}")
    public ArrayList<String> dbShowDone(@PathVariable int id) throws Exception {
       // Return the done missions of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Helper = " + id + " OR Requester = " + id + ") AND state = 'DONE'"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }

    @GetMapping ("/show/cancelled/{id}")
    public ArrayList<String> dbShowCancelled(@PathVariable int id) throws Exception {
        // Return the cancelled missions of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Requester = " + id + ") AND state = 'CANCELLED'"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;


        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }

    @GetMapping("/show/all/{id}")
    public ArrayList<String> dbShowAll(@PathVariable int id) throws Exception {
        // Return all the missions of the user with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE (Helper = " + id + " OR Requester = " + id + ")"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
               str = rs.getString("id") + " | " +
                                rs.getString("state") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("description") + " | " +
                                rs.getString("Helper") + " | " +
                                rs.getString("Requester") + " | " +
                                rs.getString("Validator");
                System.out.println(str);
                list.add(str);
            }

        return list;
    }

    @GetMapping("/show/validator/{pseudo}")
    public ArrayList<String> dbShowValidator(@PathVariable String pseudo) throws Exception {
        // Return the missions waiting for validation of the user with the pseudo "pseudo"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE Validator = (SELECT id FROM User WHERE pseudo = '" + pseudo + "') AND state = 'WAITING_FOR_VALIDATION'"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }


    @GetMapping("/show/{id}")
    public ArrayList<String> dbShow(@PathVariable int id) throws Exception {
        // Return the mission with the id "id"
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission WHERE id = " + id
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }

    @GetMapping("/show/all")
    public ArrayList<String> dbShowAll() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM Mission"
        );

        ArrayList<String> list = new ArrayList<String>();
        String str;

        while (rs.next()) {
            str = rs.getString("id") + " | " +
                            rs.getString("state") + " | " +
                            rs.getString("title") + " | " +
                            rs.getString("description") + " | " +
                            rs.getString("Helper") + " | " +
                            rs.getString("Requester") + " | " +
                            rs.getString("Validator");
            System.out.println(str);
            list.add(str);
        }

        return list;
    }



}
