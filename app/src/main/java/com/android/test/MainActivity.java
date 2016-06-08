package com.android.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.xdhywj.marqueeview.MarqueeHorizontalView;
import com.github.xdhywj.marqueeview.MarqueeVerticalView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MarqueeVerticalView marqueeView;
    private MarqueeVerticalView marqueeView1;
    private MarqueeVerticalView marqueeView2;
    private MarqueeVerticalView marqueeView3;
    private MarqueeVerticalView marqueeView4;

    private MarqueeHorizontalView marqueeHorizontalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marqueeView = (MarqueeVerticalView) findViewById(R.id.marqueeView);
        marqueeView1 = (MarqueeVerticalView) findViewById(R.id.marqueeView1);
        marqueeView2 = (MarqueeVerticalView) findViewById(R.id.marqueeView2);
        marqueeView3 = (MarqueeVerticalView) findViewById(R.id.marqueeView3);
        marqueeView4 = (MarqueeVerticalView) findViewById(R.id.marqueeView4);

        marqueeHorizontalView = (MarqueeHorizontalView) findViewById(R.id.marqueeView5);

        List<String> info = new ArrayList<>();
        info.add("1. 大家好，欢迎来到这里。");
        info.add("2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：xdhywj");
        info.add("4. 一起学习");
        info.add("5. Github要多逛逛Github要多逛逛Github要多逛逛Github要多逛逛Github要多逛逛Github要多逛逛");
        info.add("6. 微信公众号：xdhywj");
        marqueeView.startEllipsizeWithListText(info);

        marqueeView1.startFixedWithText(getString(R.string.marquee_texts));
        marqueeView2.startEllipsizeWithText(getString(R.string.marquee_texts));
        marqueeView3.startFixedWithText(getString(R.string.marquee_texts));
        marqueeView4.startFixedWithText(getString(R.string.marquee_text));


        marqueeHorizontalView.setTextColor(Color.BLACK);
        marqueeHorizontalView.startScroll();
    }

}
