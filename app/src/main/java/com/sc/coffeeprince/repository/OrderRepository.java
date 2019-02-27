package com.sc.coffeeprince.repository;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.coffeeprince.http.HttpGetTask;
import com.sc.coffeeprince.http.HttpPostTask;
import com.sc.coffeeprince.http.HttpPutTask;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.model.Order;
import com.sc.coffeeprince.model.iamport.Pay;
import com.sc.coffeeprince.model.iamport.Token;
import com.sc.coffeeprince.util.WsConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

public class OrderRepository implements Repository<Order, Integer> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void add(Order order) {

    }

    @Override
    public Order find(Integer id) {
        return null;
    }

    @Override
    public List<Order> finds(Integer id) {
        return null;
    }

    @Override
    public List<Order> findAll() throws RuntimeException {
        try {
            String content = new HttpGetTask(WsConfig.ORDER).execute().get();
            return objectMapper.readValue(content, new TypeReference<List<Order>>() {
            });
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList();
    }

    @Override
    public void update(Order order) {
        RequestBody requestBody = PostUtils.insertOrder(order);
        new HttpPutTask(WsConfig.UPDATE_ORDER + order.getId(), requestBody).execute();
    }

    @Override
    public void delete(Integer id) {
    }

    public List<Order> findByUserId(String userId) throws RuntimeException {
        try {
            Log.e("userId", userId);
            String content = new HttpGetTask(WsConfig.ORDER_LIST + userId).execute().get();
            return objectMapper.readValue(content, new TypeReference<List<Order>>() {
            });
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList();
    }

    public void cancelOrder(Order order){
        RequestBody tokenBody = PostUtils.getToken();
        Token token = new Token();
        try {
            String content = new HttpPostTask(WsConfig.GET_TOKEN, tokenBody).execute().get();
            token = objectMapper.readValue(content,Token.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        Pay pay = new Pay();
        pay.setImp_uid(order.getUid());
        pay.setAuthorization(token.getResponse().getAccess_token());
        RequestBody requestBody = PostUtils.cancelPay(pay);
        new HttpPostTask(WsConfig.CANCEL_ORDER + pay.getAuthorization(),requestBody).execute();
    }

}
