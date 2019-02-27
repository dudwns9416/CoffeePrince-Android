package com.sc.coffeeprince.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.OrderItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sc.coffeeprince.activity.MenuListActivity.orderCartList;

public class OrderActivity extends AppCompatActivity implements OrderItemAdapter.OrderItemAdapterLister {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recylerViewOrderList)
    RecyclerView recyclerViewMenuDetail;

    @BindView(R.id.btnOrderCancel)
    Button btnOrderCancel;

    @BindView(R.id.btnOrderCorrect)
    Button btnOrderCorrect;


    private OrderItemAdapter adapterMenuDetail;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);
        setTitle("장바구니");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerVIew();
    }
    @Override
    public void onCancelIconClicked(int position) {
        orderCartList.remove(position);
        adapterMenuDetail.notifyDataSetChanged();
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

    @OnClick(R.id.btnOrderCancel)
    public void doOrderCancel(){
        finish();
    }

    @OnClick(R.id.btnOrderCorrect)
    public void doOrderCorrect(){
        Intent web = new Intent(this, KakaoWebViewActivity.class);
        startActivity(web);
    }

    private void initRecyclerVIew() {
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewMenuDetail.setLayoutManager(linearLayoutManager);

        adapterMenuDetail = new OrderItemAdapter(this, orderCartList, this);
        recyclerViewMenuDetail.setAdapter(adapterMenuDetail);
    }


}
