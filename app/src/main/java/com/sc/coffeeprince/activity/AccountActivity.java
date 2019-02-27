package com.sc.coffeeprince.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.ViewPagerAdapter;
import com.sc.coffeeprince.fragment.account.LoginFragment;
import com.sc.coffeeprince.fragment.account.RegisterFragment;
import com.sc.coffeeprince.listener.OnRegisterListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {
    private static final int FLAG_LOGIN = 0;
    private static final int FLAG_REGISTER = 1;

    @BindView(R.id.imgAccountMain)
    ImageView imgAccountMain;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.bg_account_mini)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgAccountMain);

        initViewPager();
    }

    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getFragmentList());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(FLAG_LOGIN);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(FLAG_LOGIN).setText("로그인");
        tabLayout.getTabAt(FLAG_REGISTER).setText("회원가입");
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> listFragment = new ArrayList<>();
        final LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setOnRegisterListener(new OnRegisterListener() {
            @Override
            public void onSuccess(String email) {
                viewPager.setCurrentItem(FLAG_LOGIN);
                loginFragment.setInputEmail(email);
            }
        });

        listFragment.add(loginFragment);
        listFragment.add(registerFragment);

        return listFragment;
    }
}
