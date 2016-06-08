package com.github.xdhywj.commonview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.github.marqueenview.R;

/**
 * 带清除按钮的EditText
 * Created by xdhywj on 16/6/8.
 * </p>这个view继承自EditText
 * 使用了setCompoundDrawables、setOnTouchListener、addTextChangedListener
 * 、setOnFocusChangeListener这几个方法，如使用中遇到问题请查看具体实现
 * 另外，可以通过setClearButton来设置清除按钮的图标，不调用将使用默认图标
 */
public class EditTextWithClearButton extends EditText {


    // 清除按钮的图片
    private Drawable mDrawableClearButton = getResources().getDrawable(R.drawable.ic_edittext_clear);

    // 在没有清除按钮的情况下，放一个大小相等的空图片，主要是解决清除按钮状态切换时EditText的background闪烁
    private Drawable mDrawableEmpty;

    private OnTouchListener mOnTouchListener;
    private OnFocusChangeListener mOnFocusChangeListener;

    // 标记是否需要处理点击事件
    private boolean isClearButtonClickable = false;

    public EditTextWithClearButton(Context context) {
        super(context);
        init();
    }

    public EditTextWithClearButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClearButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 设置touchListener
    public void setETOnTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }

    public void setETFocusChangeListener(OnFocusChangeListener listener) {
        mOnFocusChangeListener = listener;
    }

    private void init() {
        mDrawableClearButton.setBounds(0, 0, mDrawableClearButton.getIntrinsicWidth(), mDrawableClearButton.getIntrinsicHeight());
        mDrawableEmpty = new Drawable() {
            @Override
            public void draw(Canvas canvas) {

            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        // 空按钮的设置bounds，占位用
        mDrawableEmpty.setBounds(mDrawableClearButton.getBounds());

        handleClearButton();

        handleTouchListener();

        handleTextChangeListener();

        handleFocusChangeListener();

    }


    private void handleTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnTouchListener != null) {
                    mOnTouchListener.onTouch(v, event);
                }
                EditTextWithClearButton editText = EditTextWithClearButton.this;

                // 如果清除按钮为空，不处理点击事件
                if (editText.getCompoundDrawables()[2] == null) {
                    return false;
                }

                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }

                // 如果清除按钮处在不显示状态，不处理点击事件
                if (!isClearButtonClickable) {
                    return false;
                }

                // 按点击位置处理点击事件
                if (event.getX() > editText.getWidth()
                        - editText.getPaddingRight()
                        - mDrawableClearButton.getIntrinsicWidth()) {
                    editText.setText("");
                    EditTextWithClearButton.this.handleClearButton();
                }
                return false;
            }
        });
    }

    // 添加输入变化的监听
    private void handleTextChangeListener() {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditTextWithClearButton.this.handleClearButton();
            }
        });
    }

    // 设置点击事件
    private void handleFocusChangeListener() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnFocusChangeListener != null) {
                    mOnFocusChangeListener.onFocusChange(v, hasFocus);
                }
                EditTextWithClearButton.this.handleClearButton();

            }
        });
    }

    private void handleClearButton() {
        // 如果当前EditText获取了焦点并且内容不为空，则显示清除按钮；其他情况都不显示清空按钮
        if (isFocused() && !TextUtils.isEmpty(getText().toString())) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mDrawableClearButton, getCompoundDrawables()[3]);
            isClearButtonClickable = true;
        } else {
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], mDrawableEmpty,
                    getCompoundDrawables()[3]);
            isClearButtonClickable = false;
        }
    }

    /**
     * 此函数只用来替换清除按钮的大小样式
     *
     * @param id 图片的id
     */
    public void setEditTextClearButton(int id) {
        try {
            mDrawableClearButton = getResources().getDrawable(id);
        } catch (Exception e) {
            mDrawableClearButton = getResources().getDrawable(
                    R.drawable.ic_edittext_clear);
        } finally {
            init();
        }
    }

    /**
     * 设置不要这个站位
     */
    public void removeDrawableEmpty() {
        mDrawableEmpty.setBounds(0, 0, 0, 0);
    }


}
