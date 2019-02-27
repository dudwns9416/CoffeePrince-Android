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
import com.sc.coffeeprince.helper.FlipAnimator;
import com.sc.coffeeprince.model.Cafe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sc.coffeeprince.util.WsConfig.GET_IMAGE;


public class CafeListAdapter extends RecyclerView.Adapter<CafeListAdapter.ViewHolder> {
    private Context context;
    private List<Cafe> cafeList;
    private CafeListAdapterListener listener;
    private SparseBooleanArray selectedItems;

    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    private static int currentSelectedIndex = -1;

    public CafeListAdapter(Context context, List<Cafe> cafeList, CafeListAdapterListener listener) {
        this.context = context;
        this.cafeList = cafeList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cafe_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Cafe cafes = cafeList.get(position);

        holder.from.setText(cafes.getName());
        holder.subject.setText(cafes.getContent());
        holder.message.setText(cafes.getAddress());
        holder.timestamp.setText(cafes.getBirth());

        holder.iconText.setText(cafes.getAddress().substring(0, 1));
        holder.itemView.setActivated(selectedItems.get(position, false));

        applyReadStatus(holder, cafes);
        applyImportant(holder, cafes);
        applyIconAnimation(holder, position);
        applyProfilePicture(holder, cafes);
        applyClickEvents(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return cafeList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cafeList.size();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

        holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });

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

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }


    private void applyImportant(ViewHolder holder, Cafe cafes) {
        if (cafes.isCafeImportant()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_selected));
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_normal));
        }
    }

    private void applyReadStatus(ViewHolder holder, Cafe cafes) {
        if (cafes.isCafeRead()) {
            holder.from.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.from.setTextColor(ContextCompat.getColor(context, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.message));
        } else {
            holder.from.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            holder.from.setTextColor(ContextCompat.getColor(context, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.subject));
        }
    }


    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        cafeList.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface CafeListAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.txtTitle)
        TextView from;

        @BindView(R.id.txtSubTitle)
        TextView subject;

        @BindView(R.id.txtBottomTitle)
        TextView message;

        @BindView(R.id.icon_text)
        TextView iconText;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.icon_star)
        ImageView iconImp;

        @BindView(R.id.icon_profile)
        ImageView imgProfile;

        @BindView(R.id.message_container)
        LinearLayout messageContainer;

        @BindView(R.id.icon_container)
        RelativeLayout iconContainer;

        @BindView(R.id.icon_back)
        RelativeLayout iconBack;

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