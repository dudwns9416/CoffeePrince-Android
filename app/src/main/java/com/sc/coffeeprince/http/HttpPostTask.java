package com.sc.coffeeprince.http;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class HttpPostTask extends AsyncTask<Void, Void, String> {

    private String url;
    private RequestBody requestBody;

    public HttpPostTask(String url, RequestBody requestBody) {
        this.url = url;
        this.requestBody = requestBody;
    }

    @Override
    public String doInBackground(Void... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        builder.url(url).post(requestBody);
        Request request = builder.build();

        Log.d("Request", bodyToString(requestBody));

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                response.body().close();
                Log.d("Response", "Response Success");
                return result;
            } else {
                response.body().close();
                Log.e("Response", "Response Fail");
                Log.e("Response", response.toString());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String bodyToString(RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}
