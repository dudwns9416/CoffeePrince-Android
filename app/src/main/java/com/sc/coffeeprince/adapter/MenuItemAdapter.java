package com.sc.coffeeprince.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.activity.MenuDetailActivity;
import com.sc.coffeeprince.model.Menus;

import java.util.List;

import butterknife.BindView;

import static com.sc.coffeeprince.util.WsConfig.GET_IMAGE;

/**
 * Created by young on 2017-08-11.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private Context context;
    private List<Menus> menusList;
    private int cafeId;

    public MenuItemAdapter(Context context, List<Menus> myDataset, int cafeId) {
        this.context = context;
        this.menusList = myDataset;
        this.cafeId = cafeId;
    }

    @Override
    public MenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Menus menus = menusList.get(position);
        Glide.with(context).load(GET_IMAGE + menus.getImage())
                .thumbnail(0.5f)
                .into(holder.mImageView);
        holder.tvMenuTitle.setText(menus.getName());
        holder.tvMenuPrice.setText(" " + menus.getPrice());

        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return menusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView mImageView;

        @BindView(R.id.tvMenuName)
        TextView tvMenuTitle;

        @BindView(R.id.tvMenuPrice)
        TextView tvMenuPrice;

        @BindView(R.id.tvGroupMenu)
        TextView tvGroupMenu;

        @BindView(R.id.LinearLayoutMenuList)
        LinearLayout LinearLayoutMenuList;

        public ViewHolder(View view) {
            super(view);
            LinearLayoutMenuList = (LinearLayout) view.findViewById(R.id.LinearLayoutMenuList);
            mImageView = (ImageView) view.findViewById(R.id.img);
            tvMenuTitle = (TextView) view.findViewById(R.id.tvMenuName);
            tvMenuPrice = (TextView) view.findViewById(R.id.tvMenuPrice);
            tvGroupMenu = (TextView) view.findViewById(R.id.tvGroupMenu);
        }
    }

    private void applyClickEvents(ViewHolder holder, final int position) {
        holder.LinearLayoutMenuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menus menus = menusList.get(position);
                Intent intent = new Intent(context, MenuDetailActivity.class);
                intent.putExtra("menusDetail",menus);
                intent.putExtra("cafeId",cafeId);
                context.startActivity(intent);
            }
        });
    }
}
