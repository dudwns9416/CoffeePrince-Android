package com.sc.coffeeprince.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.CafeListAdapter;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.repository.CafeRepository;
import com.sc.coffeeprince.repository.Repository;
import com.sc.coffeeprince.sqlite.CafeHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteCafeActivity extends AppCompatActivity implements CafeListAdapter.CafeListAdapterListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CafeListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Cafe> cafes = new ArrayList<>();
    private Repository repository = new CafeRepository();

    private CafeHelper cafeHelper = new CafeHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_cafe);
        ButterKnife.bind(this);
        setTitle("즐겨 찾는 카페");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();
        refreshCafeList();
        swipeRefreshLayout.setOnRefreshListener(this);
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
    public void onIconClicked(int position) {
    }

    @Override
    public void onIconImportantClicked(int position) {
        createLogoutDialog(position);
    }

    @Override
    public void onMessageRowClicked(int position) {
        Cafe cafe = cafes.get(position);
        cafe.setCafeRead(true);
        cafes.set(position, cafe);
        mAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, MenuListActivity.class);
        intent.putExtra("cafeId", cafe.getId());
        intent.putExtra("cafename", cafe.getName());
        this.startActivity(intent);
    }

    @Override
    public void onRowLongClicked(int position) {

    }

    @Override
    public void onRefresh() {
        refreshCafeList();
    }

    private void refreshCafeList() {
        swipeRefreshLayout.setRefreshing(true);
        cafes.clear();

        try {
            cafes.addAll(cafeHelper.selectCafe());
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "서버가 꺼져있습니다.", Toast.LENGTH_SHORT).show();
        }

        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initRecyclerView() {
        mAdapter = new CafeListAdapter(this, cafes, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    public void createLogoutDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("즐겨찾기");
        builder.setMessage("해당 즐겨찾기를 삭제 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteFavoriteCafe(position);
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

    public void deleteFavoriteCafe(int position) {
        Cafe cafe = cafes.get(position);
        cafe.setCafeImportant(!cafe.isCafeImportant());
        cafes.set(position, cafe);
        Log.d("iconImportant","" + cafes.get(position).isCafeImportant());
        cafeHelper.deleteCafeWhereId(cafe.getId());
        refreshCafeList();
    }

}
