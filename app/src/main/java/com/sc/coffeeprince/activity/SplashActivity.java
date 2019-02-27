package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.sqlite.UserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.imgSplash)
    ImageView imgSplash;

    @BindView(R.id.txtSplash)
    TextView txtSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Typeface tf =  Typeface.createFromAsset(getResources().getAssets(),"BMJUA_ttf.ttf");
        txtSplash.setTypeface(tf);
        moveActivity(getDestinationActivity());
    }

    private Class<?> getDestinationActivity() {
        Class<?> destinationActivity;
        UserHelper userHelper = new UserHelper(this);

        if (userHelper.isLogin()) {
            destinationActivity = HomeActivity.class;
        } else {
            destinationActivity = AccountActivity.class;
        }
        return destinationActivity;
    }

    private void moveActivity(final Class<?> destinationActivity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, destinationActivity);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}