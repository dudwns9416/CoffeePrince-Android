package com.sc.coffeeprince.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.helper.CircleTransform;
import com.sc.coffeeprince.model.User;
import com.sc.coffeeprince.sqlite.UserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sc.coffeeprince.util.WsConfig.PROFILE_IMAGE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final long FINISH_INTERVAL_TIME = 2000;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_home)
    DrawerLayout layoutMain;

    @BindView(R.id.layoutCafeList)
    LinearLayout layoutCafeList;

    @BindView(R.id.layoutFavoriteCafe)
    LinearLayout layoutFavoriteCafe;

    @BindView(R.id.layoutFAQ)
    LinearLayout layoutFAQ;

    @BindView(R.id.navigationViewMain)
    NavigationView navigationViewMain;

    @BindView(R.id.navigationHeader)
    NavigationView navigationHeader;

    @BindView(R.id.navigationBottom)
    NavigationView navigationBottom;

    @BindView(R.id.linearLayoutHomeMenu)
    LinearLayout linearLayoutHomeMenu;

    @BindView(R.id.imageViewBtnCafeList)
    ImageView imageViewBtnCafeList;

    @BindView(R.id.imageViewBtnFavorite)
    ImageView imageViewBtnFavorite;

    @BindView(R.id.imageViewBtnFAQ)
    ImageView imageViewBtnFAQ;

    private long backPressedTime = 0;
    private Typeface typefaceBinggrae;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("커피프린스 홈");

        ButterKnife.bind(this);

        imageViewSet();

        initMenuNavigation();
        showCafeManager();
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Snackbar.make(layoutMain, "한번 더 누르시면 앱이 종료됩니다.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        layoutMain.closeDrawer(navigationViewMain);

        int id = item.getItemId();
        switch (id) {
            case R.id.navHome:
                break;

            case R.id.navPayCompleteList:
                goPayCompletList();
                break;

            case R.id.navCafeManager:
                doCafeManager();
                break;

            case R.id.navLogOut:
                createLogoutDialog();
                return false;

            default:
                return false;
        }
        return true;
    }
    private void goPayCompletList() {
        Intent intent = new Intent(this,PayCompleteListActivity.class);
        intent.putExtra("userId",user.getId());
        startActivity(intent);
    }
    public void doCafeManager() {
        Intent intent = new Intent(this, CafeManagerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layoutCafeList)
    public void showCafeList() {
        Intent intent = new Intent(this, CafeListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layoutFavoriteCafe)
    public void doFavoriteCafe() {
        Intent intent = new Intent(this, FavoriteCafeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layoutFAQ)
    public void doEventList() {
        Intent intent = new Intent(this, CoffeeCenterActivity.class);
        startActivity(intent);
    }

    private void imageViewSet() {
        View headerView = navigationHeader.getHeaderView(0);
        FloatingActionButton btnCallCenter = (FloatingActionButton) headerView.findViewById(R.id.btnCallCenter);
        ImageView imgProfile = (ImageView) headerView.findViewById(R.id.imgProfile);
        TextView txtLogin = (TextView) headerView.findViewById(R.id.txtLogin);

        UserHelper userHelper = new UserHelper(this);
        User user = userHelper.selectUser();
        if(user != null) {
            txtLogin.setText(user.getId());
            if (user.getFacebook() != null) {
                imgProfile.setVisibility(headerView.VISIBLE);
                btnCallCenter.setVisibility(headerView.INVISIBLE);
                Glide.with(this).load(PROFILE_IMAGE + user.getFacebook() + "/picture?type=normal")
                        .crossFade()
                        .transform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile);
            } else {
                imgProfile.setVisibility(headerView.INVISIBLE);
            }
        }
        imageViewBtnFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
        imageViewBtnFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        imageViewBtnCafeList.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_coffee));
        imageViewBtnCafeList.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        imageViewBtnFAQ.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_question));
        imageViewBtnFAQ.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    public void createLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutUser();
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        UserHelper userHelper = new UserHelper(this);
        userHelper.deleteUser();

        Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    private void initMenuNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layoutMain, toolbar, R.string.openDrawer, R.string.closeDrawer);
        layoutMain.addDrawerListener(toggle);
        toggle.syncState();

        navigationHeader.setCheckedItem(R.id.navHome);
        navigationHeader.setNavigationItemSelectedListener(this);
    }

    private void showCafeManager() {
        UserHelper userHelper = new UserHelper(this);
        user = userHelper.selectUser();
        if (user != null) {
            if (user.getAuthority() == 1) {

            }
        }
    }
}
