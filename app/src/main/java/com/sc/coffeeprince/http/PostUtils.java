package com.sc.coffeeprince.http;


import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.Menus;
import com.sc.coffeeprince.model.Order;
import com.sc.coffeeprince.model.iamport.IamKey;
import com.sc.coffeeprince.model.iamport.Pay;
import com.sc.coffeeprince.model.User;

import java.io.File;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by CoreJin on 2017-02-21.
 */

public class PostUtils {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

    // 유저 로그인 정보 생성
    public static RequestBody selectUser(User user) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, user.toString());
        return requestBody;
    }

    public static RequestBody insertUser(User user){
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON,user.toString());
        return requestBody;
    }

    public static RequestBody insertOrder(Order order) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, order.toString());
        return requestBody;
    }

    public static RequestBody insertCafeImage(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", new Date().getTime() + file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

        return builder.build();
    }

    public static RequestBody insertCafe(Cafe cafe) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, cafe.toString());
        return requestBody;
    }

    public static RequestBody insertGroupMenu(GroupMenu groupMenu) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, groupMenu.toString());
        return requestBody;
    }

    public static RequestBody inserMenus(Menus menus) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, menus.toString());
        return requestBody;
    }

    public static RequestBody cancelPay(Pay pay){
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, pay.toString());

        return requestBody;
    }

    public static RequestBody getToken(){
        IamKey iamKey = new IamKey();
        iamKey.setImp_key("0260273955003843");
        iamKey.setImp_secret("rZ1nQ1MTeqXsakqIPvOcnhyvE4KjQTfiTgFKisDwTBZ3VpMLUBXiSvOxAyQrs6Jmnp6GSKcLlbPhBC2w");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, iamKey.toString());
        return requestBody;
    }

}

