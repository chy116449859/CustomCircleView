package com.bb.cy.view;

import java.util.List;

/**
 * author:  cy
 * date:    2019/3/9-16:03
 * Desc:    圆环各个属性bean
 */
public class CircleBean {

    public List<CircleSegmentBean> mCircleSegmentBeanList;

    /**
     * 圆环水滴的角度
     */
    public float dripAngle;

    /**
     * 圆环水滴的颜色
     */
    public int dripColor;

    /**
     * 内环半径，一般不设置，可通过计算获取
     */
    public int innerCircleRadius;

    /**
     * 内环宽度
     */
    public int innerCircleWidth;

    /**
     * 内环颜色
     */
    public int innerCircleColor;

    /**
     * 外环半径，一般不设置，可通过计算获取
     */
    public int outerCircleRadius;

    /**
     * 外环宽度
     */
    public int outerCircleWidth;

    /**
     * 内环和外环间的间距
     */
    public int outCircleMarginInnerCircle;

    /**
     * 信用等级
     */
    public String levelText;

    public int levelTextSize;

    public int levelTextColor;

    /**
     * 等级描述
     */
    public String levelDescText;

    public int levelDescTextSize;

    public int levelDescTextColor;

    /**
     * 评测时间
     */
    public String evaluateTimeText;

    public int evaluateTimeTextSize;

    public int evaluateTimeTextColor;

    /**
     * 测量时间文案距离上面的高度
     */
    public int evaluateTimeTextMarginTop;

}
