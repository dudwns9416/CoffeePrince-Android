package com.sc.coffeeprince.model.iamport;

/**
 * Created by fopa on 2017-11-08.
 */

public class Response {
    String access_token;
    long now;
    long expired_at;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(long expired_at) {
        this.expired_at = expired_at;
    }
}
