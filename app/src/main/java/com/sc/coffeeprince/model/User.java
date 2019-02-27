package com.sc.coffeeprince.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String password;
    private String name;
    private String birth;
    private Integer gender;
    private String address;
    private String phone;
    private Integer authority;
    private String token;
    private String facebook;
    private List<Cafe> cafeList;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String name, String birth, int gender, String address, String phone, int authority) {
        super();
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.authority = authority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Cafe> getCafeList() {
        return cafeList;
    }

    public void setCafeList(List<Cafe> cafeList) {
        this.cafeList = cafeList;
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