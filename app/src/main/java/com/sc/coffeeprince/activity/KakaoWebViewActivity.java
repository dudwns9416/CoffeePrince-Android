package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.fragment.webview.IamportWebviewClient;
import com.sc.coffeeprince.fragment.webview.Scheme;
import com.sc.coffeeprince.util.WsConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KakaoWebViewActivity extends AppCompatActivity {

    private static final String APP_SCHEMA = "iamport://";
    private static final String TAG = "iamport";
    public final int REQUEST_CODE = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webViewKakao)
    WebView webVIew;


    private String mBankTid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_webview);

        ButterKnife.bind(this);

        setTitle("결제");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initWebview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            onReceiveIntent(intent);
        }
    }

    private void initWebview() {
        webVIew.setWebViewClient(new IamportWebviewClient(this));
        WebSettings settings = webVIew.getSettings();
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webVIew, true);
        }

        webVIew.loadUrl(WsConfig.INSERT_PAY);
    }

    private boolean isRedirectableUrl(String url) {
        List<String> schemes = Arrays.asList("https://", "http://");
        for (String scheme : schemes) {
            if (url.startsWith(APP_SCHEMA + "://" + scheme)) {
                return true;
            }
        }
        return false;
    }

    private void onReceiveIntent(Intent intent) {
        String url = intent.getData().toString();
        if (url.startsWith(APP_SCHEMA)) {

            //nice, inicis, danalpay
            if (isRedirectableUrl(url)) {
                String redirectUrl = url.substring(APP_SCHEMA.length() + 3);
                webVIew.loadUrl(redirectUrl);
            } else { //kakao
                String path = url.substring(APP_SCHEMA.length());
                String result = path.equalsIgnoreCase("process") ? "process" : "cancel";
                webVIew.loadUrl(String.format("javascript:IMP.communicate({result: '%s'})", result));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onReceiveIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            String value = data.getExtras().getString("bankpay_value");
            String code = data.getExtras().getString("bankpay_code");

            if (!TextUtils.isEmpty(code)) {
                switch (code) {
                    case "000":
                        try {
                            String params = String.format("callbackparam2=%s&bankpay_code=%s&bankpay_value=%s", mBankTid, code, value);
                            webVIew.postUrl(Scheme.NICE_BANK_URL, URLEncoder.encode(params, "euc-kr").getBytes());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "091":
                        Log.e(TAG, "계좌이체 결제를 취소하였습니다.");
                        break;
                    case "060":
                        Log.e(TAG, "타임아웃");
                        break;
                    case "050":
                        Log.e(TAG, "전자서명 실패");
                        break;
                    case "040":
                        Log.e(TAG, "OTP/보안카드 처리 실패");
                        break;
                    case "030":
                        Log.e(TAG, "인증모듈 초기화 오류");
                        break;
                }
            }
        }
    }

    public void setBankTid(String bankTid) {
        mBankTid = bankTid;
    }
}
