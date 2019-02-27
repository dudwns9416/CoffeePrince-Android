package com.sc.coffeeprince.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.coffeeprince.http.HttpGetTask;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.util.WsConfig;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by fopa on 2017-10-21.
 */

public class UserRepository implements Repository<User,Integer> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void add(User user) {

    }

    @Override
    public User find(Integer id) {
        return null;
    }

    @Override
    public List<User> finds(Integer id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Integer id) {

    }

    public User find(String id) {
        try {
            String content = new HttpGetTask(WsConfig.UPDATE_LOGIN + id).execute().get();
            return objectMapper.readValue(content, User.class);

        } catch (RuntimeException | InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
