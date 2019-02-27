package com.sc.coffeeprince.fragment.webview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sc.coffeeprince.R;


public class WebViewFragment extends Fragment {
    private String filePath;

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_center, container, false);

        webView = (WebView) view.findViewById(R.id.webView);

        init();

        return view;
    }

    private void init() {
        webView.loadUrl(filePath);

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (isKeyActionDown(event))
                    return true;

                if (!isKeyCodeBack(keyCode)) {
                    return false;
                }

                if (webView.canGoBack()) webView.goBack();
                else getActivity().onBackPressed();
                return true;
            }
        });
        webView.setHorizontalScrollBarEnabled(false); // 세로 스크롤 제거
        webView.setVerticalScrollBarEnabled(false); // 가로 세로 제거
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private boolean isKeyCodeBack(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }

    private boolean isKeyActionDown(KeyEvent event) {
        return event.getAction() != KeyEvent.ACTION_DOWN;
    }

    protected void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
