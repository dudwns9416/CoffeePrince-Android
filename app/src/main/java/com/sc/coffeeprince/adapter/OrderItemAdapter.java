package com.sc.coffeeprince.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.model.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sc.coffeeprince.util.WsConfig.GET_IMAGE;

/**
 * Created by young on 2017-08-11.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private int amount = 1;
    private OrderItemAdapterLister orderItemAdapterLister;

    public OrderItemAdapter(Context context, List<Order> myDataset) {
        this.context = context;
        this.orderList = myDataset;
    }
    public OrderItemAdapter(Context context, List<Order> myDataset, OrderItemAdapterLister orderItemAdapterLister) {
        this.context  = context;
        this.orderList = myDataset;
        this.orderItemAdapterLister = orderItemAdapterLister;
    }

    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderList.get(position);
        Glide.with(context).load(GET_IMAGE + order.getMenus().getImage())
                .thumbnail(0.5f)
                .into(holder.imageViewBindOrderList);
        holder.btnOrderMinus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_minuss));
        holder.btnOrderMinus.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
        holder.btnOrderPlus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_plus));
        holder.btnOrderPlus.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
        holder.tvOrderMenuName.setText(order.getMenus().getName());
        holder.tvOrderMenuPrice.setText("" + order.getMenus().getPrice());
        holder.tvOrderAmount.setText("" + order.getTotal());
        holder.tvBindOrderTotalPrice.setText("" + order.getMenus().getPrice() * order.getTotal());
        applyClickBtn(holder, position);
        applyCancel(holder, position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewBindOrderList)
        ImageView imageViewBindOrderList;

        @BindView(R.id.tvOrderMenuName)
        TextView tvOrderMenuName;

        @BindView(R.id.tvOrderMenuContent)
        TextView tvOrderMenuContent;

        @BindView(R.id.tvOrderMenuPrice)
        TextView tvOrderMenuPrice;

        @BindView(R.id.tvOrderAmount)
        TextView tvOrderAmount;

        @BindView(R.id.tvBindOrderTotalPrice)
        TextView tvBindOrderTotalPrice;

        @BindView(R.id.btnOrderMinus)
        ImageView btnOrderMinus;

        @BindView(R.id.btnOrderPlus)
        ImageView btnOrderPlus;

        @BindView(R.id.imageViewCancelOrder)
        ImageView imageViewCancelOrder;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    private void applyClickBtn(final ViewHolder holder, final int position) {

        holder.btnOrderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(holder.tvOrderAmount.getText().toString());
                amount++;
                holder.tvOrderAmount.setText("" + amount);
                int price = Integer.parseInt(holder.tvOrderMenuPrice.getText().toString());
                int total = amount * price;
                holder.tvBindOrderTotalPrice.setText("" + total);
                orderList.get(position).setTotal(amount);
            }
        });
        holder.btnOrderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(holder.tvOrderAmount.getText().toString());
                if(amount != 1) {
                    amount--;
                }
                holder.tvOrderAmount.setText("" + amount);
                int price = Integer.parseInt(holder.tvOrderMenuPrice.getText().toString());
                int total = amount * price;
                holder.tvBindOrderTotalPrice.setText("" + total);
                orderList.get(position).setTotal(amount);
            }
        });

        holder.imageViewCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItemAdapterLister.onCancelIconClicked(position);
            }
        });
    }

    private void applyCancel(ViewHolder holder, int position){
        Order order = orderList.get(position);
        if(order.getId()==null){
            holder.imageViewCancelOrder.setVisibility(View.INVISIBLE);
            holder.tvOrderMenuContent.setVisibility(View.VISIBLE);
            holder.tvOrderMenuContent.setText(order.getMenus().getContent().trim());
        }else {
            holder.imageViewCancelOrder.setVisibility(View.VISIBLE);
            holder.imageViewCancelOrder.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_cancel));
            holder.imageViewCancelOrder.setColorFilter(ContextCompat.getColor(context, R.color.dark_grey));
            holder.tvOrderMenuContent.setVisibility(View.INVISIBLE);
        }
    }

    public int getAmount(){
        return amount;
    }

    public interface OrderItemAdapterLister{
        void onCancelIconClicked(int position);
    }

}
