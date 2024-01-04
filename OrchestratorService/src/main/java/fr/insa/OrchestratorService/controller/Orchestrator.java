package fr.insa.OrchestratorService.controller;

import fr.insa.OrchestratorService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/benvola")
public class Orchestrator {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public String test() {
        return "Orchestrator is working !";
    }

    /* CREATION AND CONFIGURATION OF USER */

    // Check if there is a user with this pseudo, if not, create it
    @PostMapping("createAccount")
    public String createUser(@RequestBody User user) {
        User userCheck = restTemplate.getForObject("http://user-service/user/pseudo/" + user.getPseudo(), User.class);

        if (userCheck == null) {
            return restTemplate.postForObject("http://user-service/user/add", user, String.class);
        }

        return "This pseudo is already used !";
    }

    // Configure the pseudo of the validator of the user
    @PostMapping("/{pseudo}/config/{validatorPseudo}")
    public String configureValidator(@PathVariable("pseudo") String pseudo, @PathVariable("validatorPseudo") String validatorPseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        assert user != null;

        User Validator= restTemplate.getForObject("http://user-service/user/pseudo/" + validatorPseudo, User.class);
        assert Validator != null;

        if (Validator.getRole() == User.Role.Validator) {
            user.setValidatorPseudo(validatorPseudo);
            return restTemplate.postForObject("http://user-service/user/configureValidator", user, String.class);
        }

        return "Fail to configure validator !";
    }

    /* CREATION AND CONFIGURATION OF MISSION */

    // Create a mission with only the Title and the Description in the body
    @PostMapping("/{pseudo}/createMission")
    public String createMission(@RequestBody Mission mission, @PathVariable("pseudo") String pseudo) {
        User user= restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        assert user != null;
        if (user.getRole() == User.Role.Requester){
            if (user.getValidatorPseudo() != null) {
                User validator = restTemplate.getForObject("http://user-service/user/pseudo/" + user.getValidatorPseudo(), User.class);
                assert validator != null;
                if (validator.getRole() != User.Role.Validator) {
                    return "You need to configure a correct validator before creating a request !";
                }
                mission.setRequester(user.getId());
                mission.setValidator(validator.getId());
                mission.setState(Mission.StateMission.WAITING_FOR_VALIDATION);
                restTemplate.postForObject("http://mission-service/mission/add", mission, Mission.class);
                return "Request created with success !";
            }
            return "You need to configure a validator before creating a request !";
        }

        else if (user.getRole() == User.Role.Helper){
            mission.setHelper(user.getId());
            mission.setState(Mission.StateMission.WAITING);
            restTemplate.postForObject("http://mission-service/mission/add", mission, Mission.class);
            return "Mission created with success !";
        }

        return "Fail to create mission !";
    }

    // If pseudo is :
    // - Requester : accept the mission "id"
    // - Helper : accept the request "id"
    // - Validator : validate the request "id"
    @PostMapping("/{pseudo}/mission/{id}/accept")
    public String acceptMission(@PathVariable("pseudo") String pseudo, @PathVariable("id") int id) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        Mission mission = restTemplate.getForObject("http://mission-service/mission/id/" + id, Mission.class);

        assert user != null;
        assert mission != null;

        if (user.getRole() == User.Role.Requester) {
            if (mission.getState() == Mission.StateMission.WAITING) {
                restTemplate.postForObject("http://mission-service/mission/update/requester/{id}/{requester}", null, String.class, mission.getId(), user.getId());
                return restTemplate.postForObject("http://mission-service/mission/update/inprogress/{id}", null, String.class, mission.getId());
            }
            return "Fail to accept mission !";
        }

        else if (user.getRole() == User.Role.Helper) {
            if (mission.getState() == Mission.StateMission.WAITING) {
                restTemplate.postForObject("http://mission-service/mission/update/helper/{id}/{helper}", null, String.class, mission.getId(), user.getId());
                return restTemplate.postForObject("http://mission-service/mission/update/inprogress/{id}", null, String.class,mission.getId());
            }
            return "Fail to accept request !";
        }

        // Check if the user is the validator of the mission and if the mission is waiting for his validation
        else if (user.getRole() == User.Role.Validator) {
            if (mission.getValidator() == user.getId() && (mission.getState() == Mission.StateMission.WAITING_FOR_VALIDATION)) {
                return restTemplate.postForObject("http://mission-service/mission/update/waiting/{id}", null, String.class, mission.getId());
            }
            return "Fail to validate request !";
        }

        return "Fail to accept mission !";
    }

    /* SHOW MISSIONS */

    @GetMapping("/{pseudo}/missions")
    public String[] getMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        assert user != null;

        if (user.getRole() == User.Role.Requester) {
            return restTemplate.getForObject("http://mission-service/mission/requester/" + user.getId(), String[].class);
        }

        else if (user.getRole() == User.Role.Helper) {
            return restTemplate.getForObject("http://mission-service/mission/helper/" + user.getId(), String[].class);
        }

        else if (user.getRole() == User.Role.Validator) {
            return restTemplate.getForObject("http://mission-service/mission/validator/" + user.getId(), String[].class);
        }

        return null;
    }









}
