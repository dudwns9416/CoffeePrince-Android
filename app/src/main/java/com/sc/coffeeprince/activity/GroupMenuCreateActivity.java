package com.sc.coffeeprince.activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.ResponseResult;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.repository.GroupMenuRepository;
import com.sc.coffeeprince.repository.UserRepository;
import com.sc.coffeeprince.sqlite.UserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupMenuCreateActivity extends AppCompatActivity {
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.inputGroupMenuName)
    EditText inputGroupMenuName;

    @BindView(R.id.btnGroupMenuCreate)
    Button btnGroupMenuCreate;

    private GroupMenuRepository groupMenuRepository = new GroupMenuRepository();
    private UserRepository userRepository = new UserRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_menu_create);
        ButterKnife.bind(this);

        setTitle("메뉴 분류 등록");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    @OnClick(R.id.btnGroupMenuCreate)
    void createGroupMenu(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        GroupMenu groupMenu = new GroupMenu();
        groupMenu.setName(inputGroupMenuName.getText().toString());
        groupMenu.setCafeId(getCafeId());
        ResponseResult responseResult = getResponseResult();
        Snackbar.make(view, responseResult.getRespMsg(), Snackbar.LENGTH_SHORT).show();
        if (responseResult.getRespCode()==SUCCESS){
            groupMenuRepository.add(groupMenu);
        }
    }

    private ResponseResult getResponseResult() {
        ResponseResult responseResult = null;
        if (inputGroupMenuName.getText().toString().equals("")) {
            responseResult = new ResponseResult().setRespMsg("카페 분류을 입력하세요.").setRespCode(FAIL);
        } else {
            responseResult = new ResponseResult().setRespMsg("분류 등록이 완료 되었습니다").setRespCode(SUCCESS);
        }
        return responseResult;
    }


    private int getCafeId(){
        UserHelper userHelper = new UserHelper(this);
        User user = userRepository.find(userHelper.selectUser().getId());
        userHelper.close();
        return user.getCafeList().get(0).getId();
    }
}
