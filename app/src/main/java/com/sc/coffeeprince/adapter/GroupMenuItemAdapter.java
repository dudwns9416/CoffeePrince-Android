package com.sc.coffeeprince.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.GroupMenu;
import com.sc.coffeeprince.model.Menus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by young on 2017-08-11.
 */

public class GroupMenuItemAdapter extends RecyclerView.Adapter<GroupMenuItemAdapter.ViewHolder> {
    private Context context;
    private List<GroupMenu> groupMenus;

    public GroupMenuItemAdapter(Context context, List<GroupMenu> groupMenus) {
        this.context = context;
        this.groupMenus = groupMenus;
    }

    @Override
    public GroupMenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupmenu_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupMenu groupMenu = groupMenus.get(position);
        List<Menus> menusList = groupMenu.getMenusList();

        holder.tvGroupMenu.setText(groupMenu.getName());
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(context, menusList, groupMenu.getCafeId());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        holder.recyclerView.setAdapter(menuItemAdapter);

        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return groupMenus.size();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {
        holder.linearLayoutGroupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGroupMenu)
        TextView tvGroupMenu;

        @BindView(R.id.linearLayoutGroupMenu)
        LinearLayout linearLayoutGroupMenu;

        @BindView(R.id.recylerView)
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
