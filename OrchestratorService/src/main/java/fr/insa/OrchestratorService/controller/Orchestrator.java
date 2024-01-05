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
        if (user == null) {
            return "This user doesn't exist !";
        }

        User Validator= restTemplate.getForObject("http://user-service/user/pseudo/" + validatorPseudo, User.class);
        if (Validator == null) {
            return "This validator doesn't exist !";
        }

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

        if (user == null) {
            return "This user doesn't exist !";
        }

        if (user.getRole() == User.Role.Requester){
            if (user.getValidatorPseudo() != null) {
                System.out.println(user.getValidatorPseudo());
                User validator = restTemplate.getForObject("http://user-service/user/pseudo/" + user.getValidatorPseudo(), User.class);
                if ((validator == null) || (validator.getRole() != User.Role.Validator)){
                    return "You need to configure a correct validator before creating a request !";
                }
                mission.setRequester(user.getId());
                mission.setValidator(validator.getId());
                mission.setState(Mission.StateMission.WAITING_FOR_VALIDATION);
                return restTemplate.postForObject("http://mission-service/mission/add", mission, String.class);
            }
            return "You need to configure a validator before creating a request !";
        }

        else if (user.getRole() == User.Role.Helper){
            mission.setHelper(user.getId());
            mission.setState(Mission.StateMission.WAITING);
            return restTemplate.postForObject("http://mission-service/mission/add", mission, String.class);
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
        Mission mission = restTemplate.getForObject("http://mission-service/mission/show/" + id, Mission.class);

        if (user == null) {
            return "This user doesn't exist !";
        }

        if (mission == null) {
            return "This mission doesn't exist !";
        }

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
            if ( (mission.getValidator() == user.getId()) && (mission.getState() == Mission.StateMission.WAITING_FOR_VALIDATION)) {
                return restTemplate.postForObject("http://mission-service/mission/update/waiting/{id}", null, String.class, mission.getId());
            }
            return "Fail to validate request !";
        }

        return "Fail to accept mission !";
    }

    // For the validator, refuse the mission "id"
    @PostMapping("/{pseudo}/mission/{id}/cancel")
    public String refuseMission(@PathVariable("pseudo") String pseudo, @PathVariable("id") int id, @RequestBody String reason) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        Mission mission = restTemplate.getForObject("http://mission-service/mission/show/" + id, Mission.class);

        if (user == null) {
            return "This user doesn't exist !";
        }

        if (mission == null) {
            return "This mission doesn't exist !";
        }

        if (user.getRole() == User.Role.Validator) {
            if (mission.getState() == Mission.StateMission.WAITING_FOR_VALIDATION) {
                return restTemplate.postForObject("http://mission-service/mission/update/cancel/{id}", reason, String.class, mission.getId());
            }
            return "Fail to refuse mission !";
        }

        return "Fail to refuse mission !";
    }

    // The requester can change the state of a mission to DONE
    @PostMapping("/{pseudo}/mission/{id}/done")
    public String doneMission(@PathVariable("pseudo") String pseudo, @PathVariable("id") int id) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        Mission mission = restTemplate.getForObject("http://mission-service/mission/show/" + id, Mission.class);

        if (user == null) {
            return "This user doesn't exist !";
        }

        if (mission == null) {
            return "This mission doesn't exist !";
        }

        if (user.getRole() == User.Role.Requester) {
            if (mission.getState() == Mission.StateMission.IN_PROGRESS) {
                return restTemplate.postForObject("http://mission-service/mission/update/done/{id}", null, String.class, mission.getId());
            }
            return "Fail to change the state of the mission !";
        }

        return "Fail to change the state of the mission !";
    }

    /* SHOW MISSIONS */

    // Show all the missions the user can accept
    @GetMapping("/{pseudo}/missions")
    public Mission[] getMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        String url = "http://mission-service/mission/show/offer/";

        switch (user.getRole()) {
            case Requester -> url += "requester/" + user.getId();
            case Helper -> url += "helper/" + user.getId();
            case Validator -> url += "validator/" + user.getId();
            default -> {
                return null;
            }
        }

        return restTemplate.getForObject(url, Mission[].class);
    }

    // Show all the missions the user is related to
    @GetMapping("/{pseudo}/missions/all")
    public Mission[] getAllMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        return restTemplate.getForObject("http://mission-service/mission/show/all/" + user.getId(), Mission[].class);
    }

    // Show all the done mission the user is related to
    @GetMapping("/{pseudo}/missions/done")
    public Mission[] getDoneMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        return restTemplate.getForObject("http://mission-service/mission/show/done/" + user.getId(), Mission[].class);
    }

    // Show all the in progress mission the user is related to
    @GetMapping("/{pseudo}/missions/inprogress")
    public Mission[] getInProgressMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        return restTemplate.getForObject("http://mission-service/mission/show/inprogress/" + user.getId(), Mission[].class);
    }

    // Show all the waiting mission the user is related to
    @GetMapping("/{pseudo}/missions/waiting")
    public Mission[] getWaitingMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        return restTemplate.getForObject("http://mission-service/mission/show/waiting/" + user.getId(), Mission[].class);
    }

    // Show all the waiting for validation mission the user is related to
    @GetMapping("/{pseudo}/missions/waitingforvalidation")
    public Mission[] getWaitingForValidationMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        if (user.getRole() == User.Role.Requester) {
            return restTemplate.getForObject("http://mission-service/mission/show/waitingforvalidation/" + user.getId(), Mission[].class);
        }

        return null;
    }

    // Show all the cancelled mission the user is related to
    @GetMapping("/{pseudo}/missions/cancelled")
    public Mission[] getCancelledMissions(@PathVariable("pseudo") String pseudo) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);

        if (user == null) {
            return null;
        }

        if (user.getRole() != User.Role.Helper) {
            return restTemplate.getForObject("http://mission-service/mission/show/cancelled/" + user.getId(), Mission[].class);
        }

        return null;
    }

    // Show a specific mission
    @GetMapping("/{pseudo}/mission/{id}")
    public Mission getMission(@PathVariable("pseudo") String pseudo, @PathVariable("id") int id) {
        User user = restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
        Mission mission = restTemplate.getForObject("http://mission-service/mission/show/" + id, Mission.class);

        if ((user == null) || (mission == null)) {
            return null;
        }
        return switch (user.getRole()) {
            case Requester -> (mission.getRequester() == user.getId()) ? mission : null;
            case Helper -> (mission.getHelper() == user.getId()) ? mission : null;
            case Validator -> (mission.getValidator() == user.getId()) ? mission : null;
            default -> null;
        };
    }

    /* SHOW USERS */

    // Show the current user
    @GetMapping("/{pseudo}")
    public User getUser(@PathVariable("pseudo") String pseudo) {
        return restTemplate.getForObject("http://user-service/user/pseudo/" + pseudo, User.class);
    }

    // Show the user with the id "id" if the current user can see it
    @GetMapping("/{pseudo}/user/{id}")
    public User getUserById(@PathVariable("pseudo") String pseudo, @PathVariable("id") int id) {
        User myUser = restTemplate.getForObject("http://user-service/user/pseudo" + pseudo, User.class);
        User user = restTemplate.getForObject("http://user-service/user/id/" + id, User.class);
        if ((myUser == null) || (user == null)) {
            return null;
        }

        if (myUser.getRole() == User.Role.Requester) {
            if (user.getRole() == User.Role.Helper) {
                return user;
            }
            return null;
        }

        else if (myUser.getRole() == User.Role.Helper) {
            if (user.getRole() == User.Role.Requester) {
                return user;
            }
            return null;
        }

        else return user;
    }
}
