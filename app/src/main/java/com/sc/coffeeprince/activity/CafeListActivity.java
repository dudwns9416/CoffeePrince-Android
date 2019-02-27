package com.sc.coffeeprince.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.adapter.CafeListAdapter;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.repository.CafeRepository;
import com.sc.coffeeprince.repository.Repository;
import com.sc.coffeeprince.sqlite.CafeHelper;
import com.sc.coffeeprince.util.SoundSearcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CafeListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CafeListAdapter.CafeListAdapterListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.inputSearchBar)
    EditText inputSearchBar;


    private CafeListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    private List<Cafe> cafes = new ArrayList<>();
    private List<Cafe> cafeResultList = new ArrayList<>();
    private Repository repository = new CafeRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_list);
        ButterKnife.bind(this);


        initRecyclerView();

        actionModeCallback = new ActionModeCallback();
        refreshCafeList();

        setTitle("카페 목록");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                inputSearchBar.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputSearchBar.setVisibility(View.VISIBLE);
                setTitle("");
                inputSearchBar.setImeOptions(EditorInfo.IME_ACTION_DONE);
                inputSearchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.toString().equals("")){
                            cafes.clear();
                            cafes.addAll(cafeResultList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                inputSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if(actionId == EditorInfo.IME_ACTION_DONE){
                            setTitle("카페 목록");
                            inputSearchBar.setVisibility(View.INVISIBLE);
                            searchItem(textView.getText().toString());
                            inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(),0);
                        }
                        return false;
                    }
                });
        }
        return false;
    }
    @Override
    public void onRefresh() {
        refreshCafeList();
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {

        Cafe cafe = cafes.get(position);
        cafe.setCafeImportant(!cafe.isCafeImportant());
        if(cafe.isCafeImportant()){
            CafeHelper cafeHelper = new CafeHelper(this);
            cafeHelper.insertCafe(cafe);
            cafeHelper.close();
        }
        cafes.set(position, cafe);
        Log.d("iconImportant","" + cafes.get(position).isCafeImportant());

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            Cafe cafe = cafes.get(position);
            cafe.setCafeRead(true);
            cafes.set(position, cafe);
            mAdapter.notifyDataSetChanged();

            Intent intent = new Intent(this, MenuListActivity.class);
            intent.putExtra("cafeId", cafe.getId());
            intent.putExtra("cafename", cafe.getName());
            this.startActivity(intent);
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }

    private void initRecyclerView() {
        mAdapter = new CafeListAdapter(this, cafes, this);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void refreshCafeList() {
        swipeRefreshLayout.setRefreshing(true);
        cafes.clear();


        try {
            cafeResultList.addAll(repository.findAll());
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "서버가 꺼져있습니다.", Toast.LENGTH_SHORT).show();
        }
        ArrayList<Cafe> cafeList = new CafeHelper(this).selectCafe();
        for (int i = 0; i < cafeList.size(); i++ ){
            int cafeId = cafeList.get(i).getId();
            for (int j = 0; j < cafeResultList.size(); j++){
                if(cafeId == cafeResultList.get(j).getId()){
                    cafeResultList.get(j).setCafeImportant(true);
                }
                if(cafeResultList.get(j).isHide()){
                    cafeResultList.remove(j);
                }
            }
        }
        cafes.addAll(cafeResultList);

        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void searchItem(String s){
        cafes.clear();
        cafes.addAll(cafeResultList);
        List<Cafe> matchCafe = new ArrayList<>(cafes);
        for(Cafe cafe : matchCafe){
            if(!SoundSearcher.matchString(new String(cafe.getName().trim()),s)){
                cafes.remove(cafe);
            }
        }

        mAdapter.notifyDataSetChanged();
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                }
            });
        }
    }
}
