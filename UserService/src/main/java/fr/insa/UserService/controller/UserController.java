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
    @GetMapping("/db/dropTable")
    public void dbDropTable() throws Exception {
        connection.createStatement().executeUpdate(
                "DROP TABLE IF EXISTS User"
        );

        System.out.println("Table User dropped.");
    }*/

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
                        "validatorPseudo VARCHAR(255)" +
                ")"
        );

        System.out.println("Table User created.");
    }*/

    /*
    @GetMapping("/test/add")
    public void testAdd() throws Exception {
        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('Helper', 'ispex', 'este', 'ban')"
        );

        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('Validator', 'nawa', 'nana', 'gueguer')"
        );

        connection.createStatement().executeUpdate(
                "INSERT INTO User (role, pseudo, firstname, lastname, validatorPseudo) VALUES ('Requester', 'bacchus', 'gab', 'bord', 'nawa')"
        );

        System.out.println("User added.");
    }*/

    @PostMapping ("/add")
    public String addUser(@RequestBody User user) throws Exception {
        if (user.getValidatorPseudo() == null) {
            connection.createStatement().executeUpdate(
                    "INSERT INTO User (role, pseudo, firstname, lastname) VALUES ('" +
                            user.getRole() + "', '" +
                            user.getPseudo() + "', '" +
                            user.getFirstname() + "', '" +
                            user.getLastname() + "')"
            );
        } else {
            connection.createStatement().executeUpdate(
                    "INSERT INTO User (role, pseudo, firstname, lastname, validatorPseudo) VALUES ('" +
                            user.getRole() + "', '" +
                            user.getPseudo() + "', '" +
                            user.getFirstname() + "', '" +
                            user.getLastname() + "', '" +
                            user.getValidatorPseudo() + "')"
            );
        }
        return ("User added.");
    }

    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable int id) throws Exception {
        ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM User WHERE id = " + id
        );

        if (rs.next()) {

            if (rs.getString("validatorPseudo") == null) {
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
                        rs.getString("validatorPseudo)")
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

        System.out.println("pseudo " + pseudo);

        if (rs.next()) {

            System.out.println("User found.");

            return new User(
                    rs.getInt("id"),
                    rs.getString("pseudo"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    User.Role.valueOf(rs.getString("role")),
                    rs.getString("validatorPseudo")
            );

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
            if (rs.getString("validatorPseudo") == null) {
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
                        rs.getString("validatorPseudo")
                ));
            }
        }

        return users;
    }

    @PostMapping("/configureValidator")
    public String configureValidator(@RequestBody User user) throws Exception {
        connection.createStatement().executeUpdate(
                "UPDATE User SET validatorPseudo = '" + user.getValidatorPseudo() + "' WHERE id = " + user.getId()
        );

        return("Validator configured.");
    }

    @PostMapping("/makeAdmin")
    public String makeAdmin(@RequestBody User user) throws Exception {
        connection.createStatement().executeUpdate(
                "UPDATE User SET role = 'Admin' WHERE id = " + user.getId()
        );

        return("User is now admin.");
    }

}
