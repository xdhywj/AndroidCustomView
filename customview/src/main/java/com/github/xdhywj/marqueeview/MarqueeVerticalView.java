package com.github.xdhywj.marqueeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.github.marqueenview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xdhywj on 16/6/7.
 */
public class MarqueeVerticalView extends ViewFlipper {

    private Context context;
    // 定义显示内容的list
    private List<String> noticeList = null;
    private boolean isSetAnimation = false;
    private boolean isEllipsizeStyle = false;

    // 定义默认参数：一行文字动画执行时间/两行文字翻页时间间隔/文字大小/文字颜色
    private int interval = 2000;
    private int animationDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;


    public MarqueeVerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        if (noticeList == null) {
            noticeList = new ArrayList<>();
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeVerticalViewStyle, defStyleAttr, 0);
        interval = typedArray.getInt(R.styleable.MarqueeVerticalViewStyle_vInterval, interval);
        isSetAnimation = typedArray.hasValue(R.styleable.MarqueeVerticalViewStyle_vAnimDuration);
        animationDuration = typedArray.getInt(R.styleable.MarqueeVerticalViewStyle_vAnimDuration, animationDuration);
        if (typedArray.hasValue(R.styleable.MarqueeVerticalViewStyle_vTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeVerticalViewStyle_vTextSize, textSize);
            textSize = px2sp(context, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeVerticalViewStyle_vTextColor, textColor);
        typedArray.recycle();

        // How long to wait before flipping to the next view
        setFlipInterval(interval);

        // set in/out animation
        Animation animIn = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_in);
        if (isSetAnimation) animIn.setDuration(animationDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_out);
        if (isSetAnimation) animOut.setDuration(animationDuration);
        setOutAnimation(animOut);
    }

    // ------------提供外部使用的方法--------------

    /**
     * 固定长度
     *
     * @param notice 传入的单个字符串
     */
    public void startFixedWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        // 获取view的长度
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setWithFixedWidth(notice, getWidth());
                startFilpper();
            }
        });
    }

    /**
     * 固定长度
     *
     * @param list 传入的list显示
     */
    public void startFixedWithListText(final List<String> list) {
        if (isEmpty(list)) {
            return;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setWithFixedWidth(list, getWidth());
                startFilpper();
            }
        });
    }

    /**
     * 长度大于view，则用省略号显示
     *
     * @param notice 传入的notice
     */
    public void startEllipsizeWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        noticeList.add(notice);
        isEllipsizeStyle = true;
        startFilpper();
    }

    /**
     * 长度大于view，则用省略号显示
     *
     * @param list 传入list列表
     */
    public void startEllipsizeWithListText(final List<String> list) {
        if (isEmpty(list)) {
            return;
        }
        noticeList.addAll(list);
        isEllipsizeStyle = true;
        startFilpper();
    }

    private void setWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = px2dp(context, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width");
        }
        if (noticeLength <= limit) {
            noticeList.add(notice);
        } else {
            int size = noticeLength / limit + ((noticeLength % limit) != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = (i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit;
                noticeList.add(notice.substring(startIndex, endIndex));
            }
        }
    }

    private void setWithFixedWidth(List<String> list, int width) {
        int noticeLength;
        int dpW = px2dp(context, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width");
        }
        for (String notice : list) {
            if (notice != null) {
                noticeLength = notice.length();
                if (noticeLength < limit) {
                    noticeList.add(notice);
                } else {
                    int size = noticeLength / limit + (noticeLength % limit) != 0 ? 1 : 0;
                    for (int i = 0; i < size; i++) {
                        int startIndex = i * limit;
                        int endIndex = (i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit;
                        noticeList.add(notice.substring(startIndex, endIndex));
                    }
                }
            }
        }
    }

    private void startFilpper() {
        if (isEmpty(noticeList)) {
            return;
        }
        removeAllViews();
        for (String notice : noticeList) {
            addView(createFixedItemView(notice));
        }
        if (noticeList.size() > 0) {
            startFlipping();
        }
    }

    private TextView createFixedItemView(String notice) {
        TextView item = new TextView(context);
        item.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        item.setText(notice);
        item.setTextSize(textSize);
        item.setTextColor(textColor);
        if (isEllipsizeStyle) {
            item.setSingleLine();
            item.setEllipsize(TextUtils.TruncateAt.END);
        }
        return item;
    }


    //-----------------工具类---------------
    private boolean isEmpty(List<String> list) {
        return list == null || list.size() == 0;
    }

    private int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
