package com.sc.coffeeprince.fragment.account;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.http.PostUtils;
import com.sc.coffeeprince.http.RequestServer;
import com.sc.coffeeprince.listener.OnRegisterListener;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.util.Configs;
import com.sc.coffeeprince.util.WsConfig;

import okhttp3.RequestBody;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.inputEmail)
    EditText inputEmail;
    @BindView(R.id.inputPassword)
    EditText inputPassword;
    @BindView(R.id.inputCheckPassword)
    EditText inputCheckPassword;
    @BindView(R.id.inputName)
    EditText inputName;
    @BindView(R.id.rgManWomen)
    RadioGroup rgManWomen;
    @BindView(R.id.inputPhone)
    EditText inputPhone;
    @BindView(R.id.btnRegister)
    AppCompatButton btnRegister;

    private OnRegisterListener onRegisterListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this,view);
        btnRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnRegister:
                ResponseResult responseResult = registerUser();
                Snackbar.make(view, responseResult.getRespMsg(), Snackbar.LENGTH_SHORT).show();

                if (responseResult.getRespCode() != null && responseResult.getRespCode().equals(Configs.FLAG_SUCCESS)) {
                    User user = getAccountUser();
                    onRegisterListener.onSuccess(user.getId());
                }
                break;
        }
    }

    public void setOnRegisterListener(OnRegisterListener onRegisterListener) {
        this.onRegisterListener = onRegisterListener;
    }

    private ResponseResult registerUser() {
        User user = getAccountUser();
        ResponseResult responseResult = null;

        if (user.getId().equals("")) {
            responseResult = new ResponseResult().setRespMsg("이메일을 입력하세요.");
        } else if (user.getPassword().equals("")) {
            responseResult = new ResponseResult().setRespMsg("비밀번호를 입력하세요.");
        } else if (!user.getPassword().equals(inputCheckPassword.getText().toString())) {
            responseResult = new ResponseResult().setRespMsg("비밀번호 확인이 동일하지 않습니다.");
        }  else if (user.getName().equals("")) {
            responseResult = new ResponseResult().setRespMsg("이름을 입력하세요");
        } else if (user.getGender() == -1) {
            responseResult = new ResponseResult().setRespMsg("성별을 입력하세요");
        } else if (user.getPhone().equals("")) {
            responseResult = new ResponseResult().setRespMsg("핸드폰 번호를 입력하세요");
        } else {
            RequestBody requestBody = PostUtils.insertUser(user);
            responseResult = RequestServer.requestPost(WsConfig.INSERT_USER, requestBody, getActivity());
        }
        return responseResult;
    }

    private User getAccountUser() {
        User user = new User();
        int gender = -1;
        user.setId(inputEmail.getText().toString());
        user.setPassword(inputPassword.getText().toString());
        user.setName(inputName.getText().toString());
        if (rgManWomen.getCheckedRadioButtonId() == R.id.rbMan) {
            gender = 1;
        } else if (rgManWomen.getCheckedRadioButtonId() == R.id.rbWoman) {
            gender = 2;
        }
        user.setGender(gender);
        user.setPhone(inputPhone.getText().toString());
        user.setToken(FirebaseInstanceId.getInstance().getToken());
        return user;
    }
}