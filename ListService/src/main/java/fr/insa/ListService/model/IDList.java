package fr.insa.ListService.model;

import java.util.ArrayList;
import java.util.List;

public class IDList {
    List<Integer> list = new ArrayList<Integer>();

    public IDList() {
        this.list = new ArrayList<Integer>();
    }
    public IDList(List<Integer> list) {
        this.list = list;
    }

    public void add(Integer id) {
        this.list.add(id);
    }

    public List<Integer> getList() {
        return this.list;
    }

}
