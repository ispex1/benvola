package fr.insa.ListService.controller;


import fr.insa.ListService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class ListController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ids/{idGroup}")
    public List<User> getIds(@PathVariable("idGroup") String Group) {
        IDList users = new IDList();
        users.add(1);
        users.add(2);
        users.add(3);

        int i = 1;
        List<User> listUsers = new ArrayList<User>();

        while (i < users.getList().size()){
            System.out.println("debut");
            Infos infos = restTemplate.getForObject("http://info-service/user/" + i, Infos.class);
            System.out.println("appel1");
            Eval eval = restTemplate.getForObject("http://eval-service/evaluation/" + i, Eval.class);
            listUsers.add(new User(i, Infos.getFirstname(), Infos.getLastname(), Eval.getNote()));
            i++;
        }
        System.out.println(listUsers);

        return listUsers;
    }

}
