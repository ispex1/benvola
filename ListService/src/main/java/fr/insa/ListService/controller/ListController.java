package fr.insa.ListService.controller;


import fr.insa.ListService.model.IDList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class ListController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ids/{idGroup}")
    public IDList getIds(@PathVariable("idGroup") String Group) {
        IDList ids = new IDList();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        int i = 0;

        return ids;
    }

}
