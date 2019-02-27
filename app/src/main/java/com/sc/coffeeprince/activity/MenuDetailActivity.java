package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.OrderItemAdapter;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.model.Menus;
import com.sc.coffeeprince.model.Order;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sc.coffeeprince.activity.MenuListActivity.orderCartList;

public class MenuDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnBindMenus)
    Button btnBIndMenus;

    @BindView(R.id.recyclerViewMenuDetail)
    RecyclerView recyclerViewMenuDetail;

    private OrderItemAdapter adapterMenuDetail;
    private LinearLayoutManager linearLayoutManager;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        ButterKnife.bind(this);

        setTitle("메뉴 상세보기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initRecyclerView();
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

    @OnClick(R.id.btnBindMenus)
    public void checkOrderList() {
        Order order = orderList.get(0);
        order.setId(0);
        boolean cartState = true;
        for (int i = 0; i < orderCartList.size(); i++) {
            if (orderCartList.get(i).getMenus().getId() == order.getMenus().getId()) {
                Toast.makeText(this, "이미 장바구니에 있습니다.", Toast.LENGTH_SHORT).show();
                cartState = false;
                break;
            }
        }
        if (cartState) {
            Toast.makeText(this, order.getTotal() + "개 장바구니에 담겼습니다", Toast.LENGTH_SHORT).show();
            orderCartList.add(order);
        }
    }

    private void init() {
        Intent intent = getIntent();
        int cafeId = intent.getIntExtra("cafeId", 0);
        Menus menus = intent.getParcelableExtra("menusDetail");

        Cafe cafe = createCafe(cafeId);
        Order order = createOrder(menus, cafe);

        orderList.add(order);
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMenuDetail.setLayoutManager(linearLayoutManager);
        adapterMenuDetail = new OrderItemAdapter(this, orderList);
        recyclerViewMenuDetail.setAdapter(adapterMenuDetail);
    }

    @NonNull
    private Cafe createCafe(int cafeId) {
        Cafe cafe = new Cafe();
        cafe.setId(cafeId);
        return cafe;
    }

    @NonNull
    private Order createOrder(Menus menus, Cafe cafe) {
        Order order = new Order();
        order.setCafe(cafe);
        order.setMenus(menus);
        return order;
    }

}
