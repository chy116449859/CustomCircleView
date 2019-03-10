package com.bb.cy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * author:  cy
 * date:    2019/3/9-16:35
 * Desc:
 */
public class CircleView extends View {

    private CircleBean mCircleBean;

    private CircleSegmentBean mFirstCircleSegmentBean;

    RectF mRectF;

    Paint mPaint;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //初始化circle的各个属性
    private void init() {
        mRectF = new RectF();
        mPaint = new Paint();
    }

    public CircleBean getCircleBean() {
        return mCircleBean;
    }

    /**
     * 设置一个circle的各种属性
     */
    public void setCircleBean(CircleBean circleBean) {
        mCircleBean = circleBean;
        requestLayout();
    }

    int mSegmentDescTextHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCircleBean == null || mCircleBean.mCircleSegmentBeanList == null
                || mCircleBean.mCircleSegmentBeanList.isEmpty()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            mFirstCircleSegmentBean = mCircleBean.mCircleSegmentBeanList.get(0);
            //获取圆环段描述文字的高度
            mPaint.setTextSize(mFirstCircleSegmentBean.descTextSize);
            Rect rect = new Rect();
            mPaint.getTextBounds(mFirstCircleSegmentBean.descText, 0, mFirstCircleSegmentBean.descText.length(), rect);
            mSegmentDescTextHeight = rect.height();

            //获取测量时间文字的高度
            mPaint.setTextSize(mCircleBean.evaluateTimeTextSize);
            mPaint.getTextBounds(mCircleBean.evaluateTimeText, 0, mCircleBean.evaluateTimeText.length(), rect);
            int evaluateTimeTextHeight = rect.height();

            if (mCircleBean.outerCircleRadius > 0 && mCircleBean.innerCircleRadius > 0) {
                int width = 2 * (mCircleBean.outerCircleRadius + mCircleBean.outerCircleWidth
                        + mFirstCircleSegmentBean.descTextMarginSegment + mSegmentDescTextHeight);
                int height = width / 2 + evaluateTimeTextHeight + mCircleBean.evaluateTimeTextMarginTop;
                setMeasuredDimension(width, height);
            } else {
                //通过计算得到内外环的半径
                mCircleBean.outerCircleRadius = getWidth() / 2 - mSegmentDescTextHeight
                        - mFirstCircleSegmentBean.descTextMarginSegment - mCircleBean.outerCircleWidth;
                mCircleBean.innerCircleRadius = mCircleBean.outerCircleRadius - mCircleBean.outCircleMarginInnerCircle
                        - mCircleBean.innerCircleWidth;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCircleBean == null || mFirstCircleSegmentBean == null) {
            return;
        }

        mPaint.setAntiAlias(true);
        canvas.drawColor(Color.TRANSPARENT);
        // 颜色环位置
        mRectF.left =
                (float) (mFirstCircleSegmentBean.descTextMarginSegment
                        + mCircleBean.outerCircleWidth); // 左上角x
        mRectF.top = (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth); // 左上角y
        mRectF.right = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth); // 左下角x
        mRectF.bottom = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth); // 右下角y

        //外圈文字半径
        float outerCircleDescRadius = getWidth() / 2 - mSegmentDescTextHeight - mFirstCircleSegmentBean.descTextMarginSegment;

        for (int i = 0, size = mCircleBean.mCircleSegmentBeanList.size(); i < size; i++) {
            CircleSegmentBean bean = mCircleBean.mCircleSegmentBeanList.get(i);
            /****************绘颜色环******************/
            mPaint.setStrokeWidth(mCircleBean.outerCircleWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(bean.color);
            float startAngle = -180 + i * bean.spaceAngle + bean.angle * i;
            canvas.drawArc(mRectF, startAngle, bean.angle, false, mPaint);

            /*****************绘外圈文字*****************/
            mPaint.setColor(bean.descTextColor);
            mPaint.setTextSize(18);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(0f);
            if (!TextUtils.isEmpty(bean.descText)) {
                Path path = new Path();
                //设置路径，以圆作为我们文本显示的路线；路径的绘制方式 CW 表示正序绘制，CCW表示倒序绘制
                path.addCircle(getWidth() / 2, getWidth() / 2, outerCircleDescRadius, Path.Direction.CW);
                canvas.drawTextOnPath(bean.descText, path,
                        getHOffset(bean.descText, outerCircleDescRadius, bean.angle, bean.spaceAngle, i), 0, mPaint);
            }
        }

        /******************绘制内环*********************/
        // 内环位置
        RectF rect = new RectF();
        rect.left =
                (float) (mFirstCircleSegmentBean.descTextMarginSegment
                        + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle + mCircleBean.innerCircleWidth); // 左上角x
        rect.top = (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle + mCircleBean.innerCircleWidth); // 左上角y
        rect.right = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle + mCircleBean.innerCircleWidth); // 左下角x
        rect.bottom = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle + mCircleBean.innerCircleWidth); // 右下角y
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleBean.innerCircleColor);
        mPaint.setStrokeWidth(mCircleBean.innerCircleWidth);
        canvas.drawArc(rect, -180, 180, false, mPaint);

        //绘制中心的文字
        float[] textWidthHeight = getTextWidthAndHeight(mCircleBean.levelDescText, mCircleBean.levelDescTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.levelDescTextColor);
        mPaint.setTextSize(mCircleBean.levelDescTextSize);
        mPaint.setStrokeWidth(2f);
        canvas.drawText(mCircleBean.levelDescText, (getWidth() - textWidthHeight[0]) / 2, getWidth() / 2, mPaint);

        float[] textWidthHeight1 = getTextWidthAndHeight(mCircleBean.levelText, mCircleBean.levelTextSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleBean.levelTextColor);
        mPaint.setTextSize(mCircleBean.levelTextSize);
        mPaint.setStrokeWidth(5f);
        canvas.drawText(mCircleBean.levelText, (getWidth() - textWidthHeight1[0]) / 2, getWidth() / 2 - textWidthHeight[1] - 12, mPaint);

        //绘制下面的文案
        float[] textWidthHeight2 = getTextWidthAndHeight(mCircleBean.evaluateTimeText, mCircleBean.evaluateTimeTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.evaluateTimeTextColor);
        mPaint.setTextSize(mCircleBean.evaluateTimeTextSize);
        mPaint.setStrokeWidth(3f);
        canvas.drawText(mCircleBean.evaluateTimeText, (getWidth() - textWidthHeight2[0]) / 2,
                getWidth() / 2 + textWidthHeight[1] + mCircleBean.evaluateTimeTextMarginTop, mPaint);
    }

    /**
     * 获取画圆环文字时的纵向偏移量
     *
     * @param text         文案，主要是为了计算文案的长度
     * @param radius       文案环的半径
     * @param segmentAngle 颜色环每段的角度
     * @param spaceAngle   颜色环每段的间距角度
     * @param index        第几个颜色环
     * @return
     */
    private float getHOffset(String text, float radius, float segmentAngle, float spaceAngle, int index) {
        float hOffset = 0;
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();
        //计算出这个文案占整个圆环的角度
        float textAngle = width / getPerimeter(radius) * 360;
        //计算出每段环文案对应的便宜角度；最后这个0.5主要是为了精度的问题，没有实际意义
        float offsetAnage = (segmentAngle - textAngle) / 2 + index * (segmentAngle + spaceAngle) + 0.5f;
        //计算出每段颜色环的偏移
        float offset = offsetAnage / 360 * getPerimeter(radius);
        hOffset = getPerimeter(radius) / 2 + offset;
        return hOffset;
    }

    /**
     * 获取圆环周长
     *
     * @param radius
     * @return
     */
    private float getPerimeter(float radius) {
        return (float) (2 * 3.14 * radius);
    }

    /**
     * 根据文案内容和字体大小获取字体宽度
     *
     * @param text
     * @param textSize
     * @return
     */
    private float[] getTextWidthAndHeight(String text, int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return new float[]{Float.valueOf(rect.width()), Float.valueOf(rect.height())};
    }
}
