package com.bb.cy;

import com.bb.cy.view.CircleBean;
import com.bb.cy.view.CircleSegmentBean;
import com.bb.cy.view.CircleView;

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

        circleBean.dripAngle = 70;

        circleBean.evaluateTimeTextMarginTop = 16;
        circleBean.evaluateTimeText = "Evaluation time:" + getCurrentDate(System.currentTimeMillis());
        circleBean.evaluateTimeTextColor = Color.rgb(0x99, 0x99, 0x99);
        circleBean.evaluateTimeTextSize = 24;

        List<CircleSegmentBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CircleSegmentBean bean = new CircleSegmentBean();
            bean.descTextColor = Color.rgb(0x66, 0x66, 0x66);
            bean.descTextSize = 14;
            bean.descTextMarginSegment = 12;
            if (i == 0) {
                bean.descText = "BELOW AVERAGE";
                bean.color = Color.rgb(0xf8, 0x60, 0x30);
            } else if (i == 1) {
                bean.descText = "FAIR";
                bean.color = Color.rgb(0xf4, 0xa4, 0x60);
            } else if (i == 2) {
                bean.descText = "GOOD";
                bean.color = Color.rgb(0xff, 0xff, 0x00);
            } else if (i == 3) {
                bean.descText = "VERY GOOD";
                bean.color = Color.rgb(0x00, 0x00, 0xff);
            } else if (i == 4) {
                bean.descText = "EXCELLENT";
                bean.color = Color.rgb(0x00, 0xfa, 0x9a);
            }

            bean.spaceAngle = 10f;
            bean.angle = (180f - bean.spaceAngle * 4) / 5;
            list.add(bean);
        }
        circleBean.mCircleSegmentBeanList = list;

        circleBean.outerCircleWidth = 36;
        circleBean.outCircleMarginInnerCircle = 28;

        circleBean.innerCircleWidth = 18;
        circleBean.innerCircleColor = Color.rgb(0xef, 0xef, 0xef);

        circleBean.creditLevel = 1;

        circleBean.levelText = "B";
        circleBean.levelTextColor = Color.rgb(0x33, 0x33, 0x33);
        circleBean.levelTextSize = 86;

        circleBean.levelDescText = "Low Risk";
        circleBean.levelDescTextColor = Color.rgb(0x33, 0x33, 0x33);
        circleBean.levelDescTextSize = 36;
        return circleBean;
    }

    private String getCurrentDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String dateString = dateFormat.format(date);
        return dateString;
    }
}
