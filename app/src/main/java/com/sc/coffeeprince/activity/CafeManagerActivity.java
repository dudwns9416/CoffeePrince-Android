package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.repository.UserRepository;
import com.sc.coffeeprince.sqlite.UserHelper;
import com.sc.coffeeprince.view.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CafeManagerActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layoutCafeCreateActivity)
    LinearLayout layoutCafeCreateActivity;

    @BindView(R.id.layoutGroupMenuCreate)
    LinearLayout layoutGroupMenuCreate;

    @BindView(R.id.layoutMenusCreate)
    LinearLayout layoutMenusCreate;

    @BindView(R.id.imageViewCreateMenu)
    ImageView imageViewCreateMenu;

    @BindView(R.id.imageViewCreateGroupMenu)
    ImageView imageViewCreateGroupMenu;

    @BindView(R.id.imageViewCreateCafe)
    ImageView imageViewCreateCafe;

    @BindView(R.id.imageViewCafeUpdate)
    ImageView imageViewCafeUpdate;

    public static final int HAVECAFE = 1;
    public static final int NOTHAVECAFE = 0;

    public static CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_manager);
        ButterKnife.bind(this);

        setTitle("사장님 페이지");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setArraowImage();
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

    private void setArraowImage() {
        imageViewCreateMenu.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right));
        imageViewCreateMenu.setColorFilter(ContextCompat.getColor(this,R.color.right_arraw));
        imageViewCafeUpdate.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right));
        imageViewCafeUpdate.setColorFilter(ContextCompat.getColor(this,R.color.right_arraw));
        imageViewCreateGroupMenu.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right));
        imageViewCreateGroupMenu.setColorFilter(ContextCompat.getColor(this,R.color.right_arraw));
        imageViewCreateCafe.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_right));
        imageViewCreateCafe.setColorFilter(ContextCompat.getColor(this,R.color.right_arraw));

    }

    @OnClick(R.id.layoutCafeCreateActivity)
    void setLayoutCafeCreateActivity(){
        if (getCafeByUserId(HAVECAFE)) {
            Intent intent = new Intent(this, CafeCreateActivity.class);
            startActivity(intent);
        }

    }
    @OnClick(R.id.layoutCafeUpdateActivity)
    void  setLayoutCafeUpdateActivity(){
        if(getCafeByUserId(NOTHAVECAFE)){
            Intent intent = new Intent(this, CafeUpdateActivity.class);
            startActivity(intent);
        }
    }
    @OnClick(R.id.layoutGroupMenuCreate)
    void setLayoutGroupMenuCreate(){
        if(getCafeByUserId(NOTHAVECAFE)) {
            Intent intent = new Intent(this, GroupMenuCreateActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.layoutMenusCreate)
    void setLayoutMenusCreate(){
        if(getCafeByUserId(NOTHAVECAFE)) {
            Intent intent = new Intent(this, MenuCreateActivity.class);
            startActivity(intent);
        }
    }
    private String getUserId() {
        UserHelper userHelper = new UserHelper(this);
        User user = userHelper.selectUser();
        return user.getId();
    }
    private boolean getCafeByUserId(int have){
        UserRepository userRepository = new UserRepository();
        String userId = getUserId();
        if(userId == null){
            showAlert("로그인", "로그인을 해주세요");
        }
        User user = userRepository.find(getUserId());
        if(user == null){
            showAlert("서버 오류","서버가 꺼져있습니다");
            return false;
        }
        if (have == 0) {
            if (user.getCafeList().isEmpty()) {
                showAlert("카페 등록", "카페를 만들어주세요");
               return false;
            }
        }else if (have == 1){
            if (!user.getCafeList().isEmpty()) {
                showAlert("카페 등록", "카페가 이미 존재합니다");
                return false;
            }
        }
        return true;
    }
    public void showAlert(String title,String message){
        customDialog = new CustomDialog(CafeManagerActivity.this,this);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        customDialog.setTxtAlertTitle(title);
        customDialog.setTxtAlertMessage(message);

    }

    @Override
    public void btnAlertClicked(String title) {
        if(title.equals("로그인")){
            Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else if(title.equals("서버 오류")){
            customDialog.cancel();
        }
        else if(title.equals("카페 등록")){
            customDialog.cancel();
        }
    }
}
