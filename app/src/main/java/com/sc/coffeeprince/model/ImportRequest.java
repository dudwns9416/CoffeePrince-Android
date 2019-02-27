package com.sc.coffeeprince.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by fopa on 2017-09-11.
 */

public class ImportRequest {
    private String name;
    private int amount;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;

    public ImportRequest() {
    }

    public ImportRequest(String name, int amount, String buyer_name, String buyer_tel) {
        this.name = name;
        this.amount = amount;
        this.buyer_name = buyer_name;
        this.buyer_tel = buyer_tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_tel() {
        return buyer_tel;
    }

    public void setBuyer_tel(String buyer_tel) {
        this.buyer_tel = buyer_tel;
    }

    public String getBuyer_addr() {
        return buyer_addr;
    }

    public void setBuyer_addr(String buyer_addr) {
        this.buyer_addr = buyer_addr;
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
