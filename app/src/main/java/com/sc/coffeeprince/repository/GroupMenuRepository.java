package com.sc.coffeeprince.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sc.coffeeprince.http.HttpGetTask;
import com.sc.coffeeprince.http.HttpPostTask;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.util.WsConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

/**
 * Created by fopa on 2017-10-21.
 */

public class GroupMenuRepository implements Repository<GroupMenu, Integer> {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void add(GroupMenu groupMenu) {
        RequestBody requestBody = PostUtils.insertGroupMenu(groupMenu);
        new HttpPostTask(WsConfig.GET_GROUPMENU_LIST, requestBody).execute();
    }

    @Override
    public GroupMenu find(Integer id) {
        return null;
    }

    @Override
    public List<GroupMenu> finds(Integer id) {
        try {
            String content = new HttpGetTask(WsConfig.GET_GROUPMENU_LIST + id).execute().get();
            return objectMapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(List.class, GroupMenu.class));

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList();

    }

    @Override
    public List<GroupMenu> findAll() {
        return null;
    }

    @Override
    public void update(GroupMenu groupMenu) {

    }

    @Override
    public void delete(Integer id) {

    }
}
