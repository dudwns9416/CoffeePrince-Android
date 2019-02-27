package com.sc.coffeeprince.model.iamport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by fopa on 2017-11-08.
 */

public class IamKey {
    String imp_key;
    String imp_secret;

    public String getImp_key() {
        return imp_key;
    }

    public void setImp_key(String imp_key) {
        this.imp_key = imp_key;
    }

    public String getImp_secret() {
        return imp_secret;
    }

    public void setImp_secret(String imp_secret) {
        this.imp_secret = imp_secret;
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
