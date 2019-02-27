package com.sc.coffeeprince.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.PayListAdapter;
import com.sc.coffeeprince.model.Order;
import com.sc.coffeeprince.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayCompleteListActivity extends AppCompatActivity implements PayListAdapter.PayListAdapterListener {
    @BindView(R.id.recylerViewPayCompleteList)
    RecyclerView recyclerViewPayCompleteList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PayListAdapter payListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    OrderRepository orderRepository = new OrderRepository();
    List<Order> payList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();

        List<Order> orderList = orderRepository.findByUserId(getIntent().getStringExtra("userId"));
        payList.addAll(orderList);


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

    @Override
    public void onMessageRowClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {
        createCancelDialog(position);

    }

    private void initRecyclerView() {
        payListAdapter = new PayListAdapter(this, payList, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPayCompleteList.setLayoutManager(layoutManager);
        recyclerViewPayCompleteList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPayCompleteList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewPayCompleteList.setAdapter(payListAdapter);
    }

    public void createCancelDialog(final int position) {
        final Order order = payList.get(position);
        if (order.getResultCode() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("결제취소");
            builder.setMessage("해당 결제를 취소 하시겠습니까?");
            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    orderRepository.cancelOrder(order);
                    for (Order o : payList){
                        if(o.getOrderId().equals(order.getOrderId())){
                            deleteOrder(o);
                        }
                    }
                    payListAdapter.notifyDataSetChanged();
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
    }

    private void deleteOrder(Order order) {
        order.setResultCode(5);
        orderRepository.update(order);

    }


}
