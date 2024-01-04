package fr.insa.UserService.controller;

import fr.insa.UserService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Connection connection;

    @GetMapping("/test")
    public String test() {
        return "UserService is working.";
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

    /*
    @GetMapping("/db/createTable")
    public void dbAddTable() throws Exception {
        connection.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS User (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "role ENUM('Helper', 'Requester', 'Validator', 'Admin') NOT NULL," +
                        "pseudo VARCHAR(255) NOT NULL," +
                        "firstname VARCHAR(255) NOT NULL," +
                        "lastname VARCHAR(255) NOT NULL," +
                        "validatorId INT" +
                ")"
        );

        System.out.println("Table User created.");
    }*/

    /*
    @GetMapping("/test/add")
    public void testAdd() throws Exception {
        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('Helper', 'pseudo', 'firstname', 'lastname')"
        );

        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('Validator', 'pseudo', 'firstname', 'lastname')"
        );

        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname, validatorId) VALUES ('Requester', 'pseudo', 'firstname', 'lastname', '2')"
        );

        System.out.println("User added.");
    }*/

    @PostMapping ("/add/normal")
    public void addUser(@RequestBody User user) throws Exception {
        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('" +
                        user.getRole() + "', '" +
                        user.getPseudo() + "', '" +
                        user.getFirstname() + "', '" +
                        user.getLastname() + "')"
        );

        System.out.println("User added.");
    }

    @PostMapping ("/add/requester")
    public void addRequester(@RequestBody User user) throws Exception {
        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname, validatorId) VALUES ('" +
                        user.getRole() + "', '" +
                        user.getPseudo() + "', '" +
                        user.getFirstname() + "', '" +
                        user.getLastname() + "', '" +
                        user.getValidatorId() + "')"
        );

        System.out.println("User added.");
    }

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable int id) throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM User WHERE id = " + id
        );

        if (rs.next()) {

            if (rs.getInt("validatorId") == 0) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role"))
                );
            } else {
                return new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role")),
                        rs.getInt("validatorId")
                );
            }
        }
        System.out.println("User not found.");
        return null;
    }

    @GetMapping("/pseudo/{pseudo}")
    public User getUserByPseudo(@PathVariable String pseudo) throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM User WHERE pseudo = '" + pseudo + "'"
        );

        if (rs.next()) {

            if (rs.getInt("validatorId") == 0) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role"))
                );
            } else {
                return new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role")),
                        rs.getInt("validatorId")
                );
            }
        }
        System.out.println("User not found.");
        return null;
    }

    @GetMapping("/all")
    public ArrayList<User> getAllUsers() throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM User"
        );

        ArrayList<User> users = new ArrayList<>();

        while (rs.next()) {
            if (rs.getInt("validatorId") == 0) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role"))
                ));
            } else {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("pseudo"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        User.Role.valueOf(rs.getString("role")),
                        rs.getInt("validatorId")
                ));
            }
        }

        return users;
    }

    @GetMapping("/config/{id1}/{id2}")
    public void setValidator(@PathVariable int id1, @PathVariable int id2) throws Exception {
        // Set to the user with id1 the validator with id2
        connection.createStatement().executeUpdate(
                "UPDATE User SET validatorId = " + id2 + " WHERE id = " + id1
        );

        System.out.println("Validator set.");
    }

    @GetMapping("/admin/{id}")
    public void setAdmin(@PathVariable int id) throws Exception {
        // Set the role of the user with id to Admin
        connection.createStatement().executeUpdate(
                "UPDATE User SET role = 'Admin' WHERE id = " + id
        );

        System.out.println("Admin set.");
    }

}
