package com.sc.coffeeprince.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sc.coffeeprince.http.HttpGetTask;
import com.sc.coffeeprince.http.HttpPostTask;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.Menus;
import com.sc.coffeeprince.util.WsConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

public class MenusRepository implements Repository<Menus, Integer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void add(Menus menus) {
        RequestBody requestBody = PostUtils.inserMenus(menus);
        new HttpPostTask(WsConfig.MENU, requestBody).execute();
    }

    @Override
    public Menus find(Integer id) {
        return null;
    }

    @Override
    public List<Menus> finds(Integer id) {
        try {
            String content = new HttpGetTask(WsConfig.GET_GROUPMENU_LIST + id).execute().get();
            return objectMapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(List.class, GroupMenu.class));

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList();
    }

    @Override
    public List<Menus> findAll() {
        return null;
    }

    @Override
    public void update(Menus menus) {

    }

    @Override
    public void delete(Integer id) {

    }

    public String addImage(File file) {
        RequestBody requestBody = PostUtils.insertCafeImage(file);
        HttpPostTask httpPostTask = new HttpPostTask(WsConfig.MENU_IMAGE, requestBody);
        String result = null;
        try {
            result = httpPostTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }
}
