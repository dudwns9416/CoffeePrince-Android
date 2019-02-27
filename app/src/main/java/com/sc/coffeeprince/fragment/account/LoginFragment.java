package com.sc.coffeeprince.fragment.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.activity.HomeActivity;
import com.sc.coffeeprince.http.HttpPostTask;
import com.sc.coffeeprince.http.HttpPutTask;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.http.RequestServer;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.repository.UserRepository;
import com.sc.coffeeprince.sqlite.UserHelper;
import com.sc.coffeeprince.util.NetworkConnection;
import com.sc.coffeeprince.util.WsConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;

public class LoginFragment extends Fragment {
    public static final String FAIL = "Fail";
    public static final String SUCCESS = "Success";

    @BindView(R.id.inputEmail)
    EditText inputEmail;
    @BindView(R.id.inputPassword)
    EditText inputPassword;
    @BindView(R.id.btnNotAccountLogin)
    Button btnNotAccountLogin;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnFacebookLogin)
    LoginButton btnFacebookLogin;

    CallbackManager callbackManager;
    String email;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this,view);
        LoginManager.getInstance().logOut();
        doFacebookLogin();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btnNotAccountLogin)
    public void tryNotAccountLogin(){
        UserHelper userHelper = new UserHelper(getActivity());
        userHelper.deleteUser();
        userHelper.close();
        Intent intent = new Intent(getActivity(),HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.btnLogin)
    public void tryLogin() {
        ResponseResult result = doLogin();

        if (result == null)
            return;

        showLoginStatus(result);
    }

    public void showLoginStatus(ResponseResult result) {
        Snackbar.make(getView(), result.getRespMsg(), Snackbar.LENGTH_SHORT).show();
        if (result.getRespCode().equals(SUCCESS)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }, 250);
        }
    }

    private ResponseResult doLogin() {
        email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (lengthMoreThanZero(email)) {
            return new ResponseResult().setRespMsg("아이디를 입력하세요.").setRespCode(FAIL);
        } else if (lengthMoreThanZero(password)) {
            return new ResponseResult().setRespMsg("비밀번호를 입력하세요.").setRespCode(FAIL);
        }

        if (NetworkConnection.isNetworkWorking(getActivity())) {
            try {
                User user = new User(email);
                user.setPassword(password);

                RequestBody requestBody = PostUtils.selectUser(user);
                HttpPostTask httpPostTask = new HttpPostTask(WsConfig.GET_LOGIN, requestBody);
                User resultUser = objectMapper.readValue(httpPostTask.execute().get(), User.class);
                Log.d("resultUser",resultUser.toString());

                if (resultUser.getToken() == null){
                    resultUser.setToken(FirebaseInstanceId.getInstance().getToken());
                    requestBody = PostUtils.selectUser(resultUser);
                    HttpPutTask httpPutTask = new HttpPutTask(WsConfig.UPDATE_LOGIN + user.getId(), requestBody);
                    resultUser = objectMapper.readValue(httpPutTask.execute().get(), User.class);
                }

                if(resultUser == null) {
                    return new ResponseResult().setRespMsg("로그인을 실패했습니다.").setRespCode(FAIL);
                } else {
                    UserHelper userHelper = new UserHelper(getActivity());
                    userHelper.deleteUser();
                    userHelper.insertUser(resultUser);
                    userHelper.close();
                    return new ResponseResult().setRespMsg("로그인을 성공했습니다.").setRespCode(SUCCESS);
                }
            } catch (InterruptedException | ExecutionException | IOException | NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            return new ResponseResult().setRespMsg("네트워크가 연결되어 있지 않습니다.").setRespCode(FAIL);
        }
        return new ResponseResult().setRespMsg("로그인을 실패했습니다.").setRespCode(FAIL);
    }

    private boolean lengthMoreThanZero(String email) {
        return email.length() == 0;
    }
    public void setInputEmail(String email) {
        inputEmail.setText(email);
    }

    public void doFacebookLogin() {

        callbackManager = CallbackManager.Factory.create();
        btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFacebookLogin.setFragment(this);
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("result",object.toString());
                                try {
                                    ResponseResult responseResult;
                                    String id = object.getString("id");
                                    String email = object.getString("email").replaceAll(".com","");
                                    String gender = object.getString("gender");
                                    String name = object.getString("name");
                                    int gen = 2;
                                    if (gender.equals("male")){
                                        gen = 1;
                                    }
                                    UserRepository userRepository = new UserRepository();
                                    User resultUser = userRepository.find(email);
                                    if(resultUser == null) {
                                        resultUser = new User();
                                        resultUser.setId(email);
                                        resultUser.setName(name);
                                        resultUser.setAuthority(1);
                                        resultUser.setGender(1);
                                        resultUser.setToken(FirebaseInstanceId.getInstance().getToken());

                                        RequestBody requestBody = PostUtils.insertUser(resultUser);
                                        responseResult = RequestServer.requestPost(WsConfig.INSERT_USER, requestBody, getActivity());
                                    }else {
                                        responseResult = new ResponseResult().setRespMsg("로그인을 성공했습니다.").setRespCode(SUCCESS);
                                    }
                                    showLoginStatus(responseResult);
                                    UserHelper userHelper = new UserHelper(getActivity());
                                    userHelper.deleteUser();
                                    userHelper.insertUser(resultUser,id);
                                    userHelper.close();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                ResponseResult result = new ResponseResult().setRespMsg("로그인을 실패했습니다.").setRespCode(FAIL);
                showLoginStatus(result);
            }
        });
    }
}
