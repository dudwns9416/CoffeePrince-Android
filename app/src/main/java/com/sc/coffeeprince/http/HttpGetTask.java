package com.sc.coffeeprince.http;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by young on 2017-06-04.
 */

public class HttpGetTask extends AsyncTask<Void, Void, String> {

    private String url;

    public HttpGetTask(String url) {
        this.url = url;
    }
    @Override
    protected String doInBackground(Void... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url).get();
        Request request = builder.build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("Response", "Response Success");
                String result = response.body().string();
                response.body().close();
                return result;
            } else {
                response.body().close();
                Log.e("Response", "Response Fail");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
