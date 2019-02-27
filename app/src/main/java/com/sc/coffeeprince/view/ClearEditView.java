package com.sc.coffeeprince.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.sc.coffeeprince.R;


/**
 * Created by CoreJin on 2016-12-08.
 */

public class ClearEditView extends AppCompatEditText implements View.OnTouchListener, OnFocusChangeListener, TextWatcher {
    private Drawable clearTextIcon;
    private OnTouchListener onTouchListener;

    public ClearEditView(final Context context) {
        super(context);
        init(context);
    }

    public ClearEditView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - clearTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_cancel);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.textColorPrimary));
        clearTextIcon = wrappedDrawable;
        clearTextIcon.setBounds(0, 0, 40, 40);
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private void setClearIconVisible(final boolean visible) {
        clearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? clearTextIcon : null,
                compoundDrawables[3]);
    }
}
