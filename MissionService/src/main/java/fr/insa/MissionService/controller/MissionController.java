package fr.insa.MissionService.controller;

import fr.insa.MissionService.model.Mission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;

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
    public String db() throws Exception {
        // I want to return the name of the tables in the database
        return connection.getMetaData().getTables(null, null, "%", null).toString();
    }

}
