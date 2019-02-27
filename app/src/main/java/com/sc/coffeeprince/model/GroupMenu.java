package com.sc.coffeeprince.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class GroupMenu {

    private Integer id;
    private String name;
    private Integer mindex;
    private Integer cafeId;
    private List<Menus> menusList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMindex() {
        return mindex;
    }

    public void setMindex(Integer mindex) {
        this.mindex = mindex;
    }

    public Integer getCafeId() {
        return cafeId;
    }

    public void setCafeId(Integer cafeId) {
        this.cafeId = cafeId;
    }

    public List<Menus> getMenusList() {
        return menusList;
    }

    public void setMenusList(List<Menus> menusList) {
        this.menusList = menusList;
    }

    public void addMenus(Menus m){
        if(menusList == null) {
            menusList = new ArrayList<>();
        }
        menusList.add(m);
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
