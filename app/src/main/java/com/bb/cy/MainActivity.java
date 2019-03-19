package com.bb.cy;

import com.bb.cy.view.CircleBean;
import com.bb.cy.view.CircleSegmentBean;
import com.bb.cy.view.CircleView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleView circleView = findViewById(R.id.circleView);
        circleView.setCircleBean(initCircleBean());
    }

    private CircleBean initCircleBean() {
        CircleBean circleBean = new CircleBean();

        circleBean.dripAngle = 72.5f;

        circleBean.evaluateTimeTextMarginTop = dp2px(this, 10);
        circleBean.evaluateTimeText = "Evaluation time:" + getCurrentDate(System.currentTimeMillis());
        circleBean.evaluateTimeTextColor = Color.rgb(0x99, 0x99, 0x99);
        circleBean.evaluateTimeTextSize = sp2px(this, 10);

        List<CircleSegmentBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CircleSegmentBean bean = new CircleSegmentBean();
            bean.descTextColor = Color.rgb(0x66, 0x66, 0x66);
            bean.descTextSize = sp2px(this, 7);
            bean.descTextMarginSegment = dp2px(this, 5);
            if (i == 0) {
                bean.descText = "E-:E:E+";
                bean.color = Color.rgb(0xff, 0x6c, 0x6c);
            } else if (i == 1) {
                bean.descText = "D-:D:D+";
                bean.color = Color.rgb(0xff, 0x99, 0x33);
            } else if (i == 2) {
                bean.descText = "C-:C:C+";
                bean.color = Color.rgb(0xfe, 0xc4, 0x14);
            } else if (i == 3) {
                bean.descText = "B-:B:B+";
                bean.color = Color.rgb(0x48, 0x88, 0xf7);
            } else if (i == 4) {
                bean.descText = "A-:A:A+";
                bean.color = Color.rgb(0x45, 0xe0, 0x9d);
            }

            bean.spaceAngle = 7f;
            bean.angle = (180f - bean.spaceAngle * 4) / 5;
            list.add(bean);
        }
        circleBean.mCircleSegmentBeanList = list;

        circleBean.outerCircleWidth = dp2px(this, 11);
        ;
        circleBean.outCircleMarginInnerCircle = dp2px(this, 15);

        circleBean.innerCircleWidth = dp2px(this, 5);
        circleBean.innerCircleColor = Color.rgb(0xef, 0xef, 0xef);

        circleBean.creditLevel = 1;

        circleBean.levelText = "E-";
        circleBean.levelTextColor = Color.rgb(0x33, 0x33, 0x33);
        circleBean.levelTextSize = sp2px(this, 49);

        circleBean.levelDescText = "Low Risk";
        circleBean.levelDescTextColor = Color.rgb(0x33, 0x33, 0x33);
        circleBean.levelDescTextSize = sp2px(this, 16);
        return circleBean;
    }

    private String getCurrentDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
