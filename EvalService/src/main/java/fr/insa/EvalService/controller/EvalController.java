package fr.insa.EvalService.controller;

import fr.insa.EvalService.model.Eval;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/evaluation")
public class EvalController {

    @GetMapping("/{idStudent}")
    public Eval getEval(@PathVariable("idStudent") int idStudent) {

        // Simulate the DB with a list
        List<Eval> evalList = Arrays.asList(
                new Eval(0, 12),
                new Eval(1, 15),
                new Eval(2, 18),
                new Eval(3, 20)
        );

        return evalList.get(idStudent);
    }


}

