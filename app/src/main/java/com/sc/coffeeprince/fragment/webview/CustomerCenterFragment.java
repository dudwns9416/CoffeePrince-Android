package com.sc.coffeeprince.fragment.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomerCenterFragment extends WebViewFragment {
    private static final String FILE_PATH = "file:android_asset/page_customer_center.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFilePath(FILE_PATH);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
