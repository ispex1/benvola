package fr.insa.InfoService.controller;

import fr.insa.InfoService.model.Infos;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class InfosController {
    @GetMapping
    public String test() {
        return "Hello World!";
    }

    @GetMapping("{id}")
    public Infos getInfos(@PathVariable("id") int id) {
        List<Infos> infos = Arrays.asList(
                new Infos(1, "John", "Doe", "01/01/2000"),
                new Infos(2, "Jane", "Doe", "01/01/2000"),
                new Infos(3, "John", "Smith", "01/01/2000"),
                new Infos(4, "Jane", "Smith", "01/01/2000")
        );
        return infos.get(id - 1);
    }
}
