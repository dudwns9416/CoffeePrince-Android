package com.sc.coffeeprince.http;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.util.NetworkConnection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

/**
 * Created by deskh on 2017-06-14.
 */

public class RequestServer {
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    public static ResponseResult requestPost(String url, RequestBody requestBody, Context context) {
        if (NetworkConnection.isNetworkWorking(context)) {
            HttpPostTask httpPostTask = new HttpPostTask(url, requestBody);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                User resultUser= objectMapper.readValue(httpPostTask.execute().get(), User.class);
                if(resultUser==null){
                    return  new ResponseResult().setRespMsg("회원가입 실패").setRespCode(FAIL);
                }else{
                    return  new ResponseResult().setRespMsg("회원가입 성공").setRespCode(SUCCESS);
                }
            } catch (InterruptedException | ExecutionException | IOException e) {
                e.printStackTrace();
                return new ResponseResult().setRespMsg("에러가 발생했습니다.").setRespCode(FAIL);
            }
        } else {
            return new ResponseResult().setRespMsg("네트워크가 연결되어 있지 않습니다.").setRespCode(FAIL);
        }
    }
}
