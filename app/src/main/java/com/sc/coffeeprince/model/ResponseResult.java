package com.sc.coffeeprince.model;

/**
 * Created by young on 2017-06-10.
 */

public class ResponseResult {

    private String respCode;
    private String respMsg;

    public String getRespCode() {
        return respCode;
    }

    public ResponseResult setRespCode(String respCode) {
        this.respCode = respCode;
        return this;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public ResponseResult setRespMsg(String respMsg) {
        this.respMsg = respMsg;
        return this;
    }
}
