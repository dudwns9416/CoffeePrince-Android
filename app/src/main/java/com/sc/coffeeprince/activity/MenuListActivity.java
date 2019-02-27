package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.GroupMenuItemAdapter;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.Order;
import com.sc.coffeeprince.repository.MenusRepository;
import com.sc.coffeeprince.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recylerViewGroupMenu)
    RecyclerView recyclerViewGroupMenu;

    @BindView(R.id.btnPay)
    Button btnPay;

    private RecyclerView.Adapter adapterGroupMenu;
    private LinearLayoutManager linearLayoutManager;
    private Repository repository = new MenusRepository();
    private List<GroupMenu> groupMenuList = new ArrayList<>();
    public static List<Order> orderCartList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        ButterKnife.bind(this);
        setTitle(getIntent().getStringExtra("cafename"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderCartList = new ArrayList<>();


        initGroupMenus();
        putGroupMenus();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnPay)
    public void doPay() {
        Intent intent = new Intent(this, OrderActivity.class);
        //intent.putExtra("menuNum", "cafelatte");
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }

    private void initGroupMenus() {
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewGroupMenu.setLayoutManager(linearLayoutManager);

        adapterGroupMenu = new GroupMenuItemAdapter(this, groupMenuList);
        recyclerViewGroupMenu.setAdapter(adapterGroupMenu);
    }

    private void putGroupMenus() {
        int cafeId = getIntent().getIntExtra("cafeId", 1);
        List<GroupMenu> groupMenus = repository.finds(cafeId);
        groupMenuList.addAll(groupMenus);
    }

}
