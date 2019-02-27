package com.sc.coffeeprince.model.iamport;

/**
 * Created by fopa on 2017-11-08.
 */

public class Pay {
    String imp_uid;
    String Authorization;
    Double amount;

    public String getImp_uid() {
        return imp_uid;
    }

    public void setImp_uid(String imp_uid) {
        this.imp_uid = imp_uid;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        String result = "{\"Authorization\": \"" + getAuthorization() + "\", " +
                "\"X-ImpTokenHeader\": \"" + getAuthorization() + "\"," +
                "\"imp_uid\":\""+getImp_uid() +"\"," +
                "\"amount\":"+getAmount()+"}";
        return result;
    }
}
