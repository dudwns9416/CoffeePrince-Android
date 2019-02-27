package com.sc.coffeeprince.model.iamport;

/**
 * Created by fopa on 2017-11-08.
 */

public class Token {
    int code;
    String message;
    Response response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
