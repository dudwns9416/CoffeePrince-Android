package com.sc.coffeeprince.fragment.webview;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.coffeeprince.activity.AccountActivity;
import com.sc.coffeeprince.activity.KakaoWebViewActivity;
import com.sc.coffeeprince.activity.PayCompleteActivity;
import com.sc.coffeeprince.model.Order;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.sqlite.UserHelper;
import com.sc.coffeeprince.util.WsConfig;
import com.sc.coffeeprince.view.CustomDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sc.coffeeprince.activity.MenuListActivity.orderCartList;

/**
 * Created by constant on 2017-06-23.
 */

public class IamportWebviewClient extends WebViewClient implements CustomDialog.CustomDialogListener {

    private KakaoWebViewActivity mActivity;
    private ObjectMapper objectMapper = new ObjectMapper();

    public IamportWebviewClient(KakaoWebViewActivity activity) {
        mActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (isOverridableUrl(url)) {

            Intent intent = null;
            try {

                if (url.startsWith(Scheme.BANKPAY)) {
                    String params = getRequestParamsOfBankPay(url);
                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.kftc.bankpay.android","com.kftc.bankpay.android.activity.MainActivity"));
                    intent.putExtra("requestInfo", params);
                    mActivity.startActivityForResult(intent, mActivity.REQUEST_CODE);

                    return true;
                } else if ( url.startsWith(Scheme.LGU_BANKPAY)) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, mActivity.getPackageName());
                    mActivity.startActivity(intent);

                    return true;
                }else if(url.startsWith("app://payComplete")){
                    intent = new Intent(mActivity.getApplicationContext(), PayCompleteActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }else if(url.startsWith("app://orderList")){
                    mActivity.finish();
                    Toast.makeText(mActivity.getApplicationContext(), "결제가 취소 되었습니다.",Toast.LENGTH_SHORT).show();
                }

                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Uri uri = Uri.parse(intent.getDataString());
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            } catch (URISyntaxException e) {
            } catch (ActivityNotFoundException e) {
                return handleNotFoundPaymentScheme(intent);
            }
        }

        return false;
    }

    private boolean isOverridableUrl(String url) {
        String[] schemas = new String[]{"http://", "https://", "javascript://"};
        for (String schema : schemas) {
            if (url.startsWith(schema)) {
                return false;
            }
        }
        return true;
    }

    private boolean handleNotFoundPaymentScheme(Intent intent) {
        if (intent != null) {
            String id = null;
            String scheme = intent.getScheme();
            if (!TextUtils.isEmpty(scheme)) {
                if (scheme.equalsIgnoreCase(Scheme.ISP)) {
                    id = Scheme.PACKAGE_ISP;
                } else if (scheme.equalsIgnoreCase(Scheme.BANKPAY)) {
                    id = Scheme.PACKAGE_BANKPAY;
                } else if ( scheme.equalsIgnoreCase(Scheme.LGU_BANKPAY)) {
                    id = Scheme.PACKAGE_LGU_BANKPAY;
                }else if ( scheme.equalsIgnoreCase(Scheme.KAKAO)) {
                    id = Scheme.KAKAO;
                }
            }

            if (TextUtils.isEmpty(id)) {
                id = intent.getPackage();
            }

            if (!TextUtils.isEmpty(id)) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + id)));
                return true;
            }
        }

        return false;
    }

    private String getRequestParamsOfBankPay(String rawUrl) throws URISyntaxException {

        Uri uri = Uri.parse(rawUrl);
        Map<String, String> query = getQuery(uri.getQuery());
        List<String> allowedKeys = Arrays.asList("user_key", "firm_name", "amount", "serial_no", "approve_no", "receipt_yn", "user_key", "callbackparam2", "");
        StringBuilder params = new StringBuilder();

        for (String key : query.keySet()) {
            String value = query.get(key);
            if (allowedKeys.contains(key)) {
                if (key.equals("user_key")) {
                    mActivity.setBankTid(value);
                    params.append(String.format("&%s=%s",key, value));
                }
            }
        }

        params.append(String.format("&%s=%s","callbackparam1", "nothing"));
        params.append(String.format("&%s=%s","callbackparam3", "nothing"));

        return params.toString();
    }

    private Map<String, String> getQuery(String rawQuery) {
        Map<String, String> query = new HashMap<String, String>();
        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            String[] partials = pair.split("=");
            if (partials.length > 2) {
                query.put(partials[0], partials[1]);
            }
        }
        return query;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(url.equals(WsConfig.INSERT_PAY)){
            String script3 = "javascript:var ordername = '커피프린스 결제';";
            int amount = 0;
            try {
                List<Order> ordersList = new ArrayList<>();
                UserHelper userHelper = new UserHelper(mActivity);
                User user = userHelper.selectUser();
                if(user == null){
                    CustomDialog customDialog = new CustomDialog(mActivity,this);
                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                    customDialog.setTxtAlertTitle("로그인");
                    customDialog.setTxtAlertMessage("로그인을 해주세요");
                    return;
                }
                for(int i = 0; i < orderCartList.size() ; i++) {
                    Order order = orderCartList.get(i);
                    order.setUsers(user);
                    order.setResultCode(1);
                    ordersList.add(order);
                    amount += order.getTotal() * order.getMenus().getPrice();
                }
                String orderList = "javascript:var orderList = "+ objectMapper.writeValueAsString(ordersList) +";";
                Log.e("orderList",orderList);
                view.loadUrl(orderList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String script2 = "javascript:function afterLoad2() {"
                    + "document.getElementById('amounts').value = '" + amount + "';"
                    + "};"
                    + "afterLoad2();"
                    + "showMyOrder(orderList);";

            view.loadUrl(script3);
            view.loadUrl(script2);


        }
    }

    @Override
    public void btnAlertClicked(String title) {
        if (title.equals("로그인")){
            Intent intent = new Intent(mActivity,AccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }
}
