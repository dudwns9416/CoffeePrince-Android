package com.sc.coffeeprince.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.coffeeprince.http.HttpGetTask;
import com.sc.coffeeprince.http.HttpPostTask;
import com.sc.coffeeprince.http.HttpPutTask;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.util.WsConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

public class CafeRepository implements Repository<Cafe, Integer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void add(Cafe cafe) {
        RequestBody requestBody = PostUtils.insertCafe(cafe);
        new HttpPostTask(WsConfig.CAFE, requestBody).execute();
    }

    @Override
    public Cafe find(Integer id) {
        return null;
    }

    @Override
    public List<Cafe> finds(Integer id) {
        return null;
    }

    @Override
    public List<Cafe> findAll() throws RuntimeException {
        try {
            String content = new HttpGetTask(WsConfig.CAFE).execute().get();
            return objectMapper.readValue(content, new TypeReference<List<Cafe>>() {
            });
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList();
    }

    @Override
    public void update(Cafe cafe) {
        RequestBody requestBody = PostUtils.insertCafe(cafe);
        new HttpPutTask(WsConfig.CAFE_UPDATE + cafe.getId(), requestBody).execute();
    }

    @Override
    public void delete(Integer id) {
    }

    public String addImage(File file) {
        RequestBody requestBody = PostUtils.insertCafeImage(file);
        HttpPostTask httpPostTask = new HttpPostTask(WsConfig.CAFE_IMAGE, requestBody);
        String result = null;
        try {
            result = httpPostTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }
}
