package com.sc.coffeeprince.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.sc.coffeeprince.listener.OnItemClickListener;
import com.sc.coffeeprince.model.Menus;

import java.util.ArrayList;


public class CardViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Menus> listAngel;
    private OnItemClickListener onItemClickListener;

    public CardViewPagerAdapter(Context context, ArrayList<Menus> listAngel, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listAngel = listAngel;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
