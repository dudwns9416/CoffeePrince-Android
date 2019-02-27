package com.sc.coffeeprince.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sc.coffeeprince.R;
import com.sc.coffeeprince.helper.CircleTransform;
import com.sc.coffeeprince.model.Cafe;
import com.sc.coffeeprince.model.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sc.coffeeprince.util.WsConfig.GET_IMAGE;


public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ViewHolder> {
    private Context context;
    private List<Order> payOrderList;
    private PayListAdapterListener listener;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animationItemsIndex;

    public PayListAdapter(Context context, List<Order> payOrderList, PayListAdapterListener listener) {
        this.context = context;
        this.payOrderList = payOrderList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pay_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Order order = payOrderList.get(position);

        Cafe cafes = order.getCafe();

        holder.message.setText(order.getDate());
        holder.subject.setText(cafes.getName());
        holder.txtTitle.setText("주문번호 " + order.getOrderId());


        holder.iconText.setText("서");
        holder.itemView.setActivated(selectedItems.get(position, false));

        applyResultCode(holder, order);
        applyReadStatus(holder, cafes);
        applyProfilePicture(holder, cafes);
        applyClickEvents(holder, position);
    }

    public void applyResultCode(ViewHolder holder, Order order) {
        String resultCode = "";
        switch (order.getResultCode()){
            case 1:
                resultCode = "주문 완료";
                break;
            case 2:
                resultCode = "매장 확인";
                break;
            case 3:
                resultCode = "음료 제조";
                break;
            case 4:
                resultCode = "음료 취득";
                break;
            case 5:
                resultCode = "결제 취소";
                break;
        }
        holder.timestamp.setText(resultCode);
    }

    @Override
    public long getItemId(int position) {
        return payOrderList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return payOrderList.size();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    private void applyProfilePicture(ViewHolder holder, Cafe cafes) {
        if (!TextUtils.isEmpty(cafes.getImage())) {
            Glide.with(context).load(GET_IMAGE + cafes.getImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(Color.DKGRAY);
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    private void applyReadStatus(ViewHolder holder, Cafe cafes) {
        if (cafes.isCafeRead()) {
            holder.txtTitle.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.message));
        } else {
            holder.txtTitle.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.subject));
        }
    }
    public interface PayListAdapterListener {

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.txtSubTitle)
        TextView subject;

        @BindView(R.id.txtBottomTitle)
        TextView message;

        @BindView(R.id.icon_text)
        TextView iconText;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.icon_profile)
        ImageView imgProfile;

        @BindView(R.id.message_container)
        LinearLayout messageContainer;

        @BindView(R.id.icon_container)
        RelativeLayout iconContainer;

        @BindView(R.id.icon_front)
        RelativeLayout iconFront;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

}