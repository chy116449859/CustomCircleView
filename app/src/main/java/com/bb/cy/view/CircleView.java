package com.bb.cy.view;

import com.bb.cy.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
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

    Bitmap mBitmap;

    Path mPath;

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
        mPath = new Path();
        mBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_pointer);
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
            mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf"));
            mSegmentDescTextHeight = getTextHeight(mFirstCircleSegmentBean.descText);

            //获取测量时间文字的高度
            mPaint.setTextSize(mCircleBean.evaluateTimeTextSize);
            int evaluateTimeTextHeight = getTextHeight(mCircleBean.evaluateTimeText);

            if (mCircleBean.outerCircleRadius > 0 && mCircleBean.innerCircleRadius > 0) {
                int width = 2 * (mCircleBean.outerCircleRadius + mCircleBean.outerCircleWidth
                        + mFirstCircleSegmentBean.descTextMarginSegment + mSegmentDescTextHeight);
                int height = width / 2 + evaluateTimeTextHeight + mCircleBean.evaluateTimeTextMarginTop;
                setMeasuredDimension(width, height);
            } else {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                //通过计算得到内外环的半径
                mCircleBean.outerCircleRadius = width / 2 - mSegmentDescTextHeight
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

        /******************绘制内环*********************/
        // 内环位置
        RectF rect = new RectF();
        rect.left =
                (float) (mFirstCircleSegmentBean.descTextMarginSegment
                        + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle
                        + mCircleBean.innerCircleWidth); // 左上角x
        rect.top = (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle
                + mCircleBean.innerCircleWidth); // 左上角y
        rect.right = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle
                + mCircleBean.innerCircleWidth); // 左下角x
        rect.bottom = getWidth() - (float) (mFirstCircleSegmentBean.descTextMarginSegment
                + mCircleBean.outerCircleWidth + mCircleBean.outCircleMarginInnerCircle
                + mCircleBean.innerCircleWidth); // 右下角y
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleBean.innerCircleColor);
        mPaint.setStrokeWidth(mCircleBean.innerCircleWidth);
//        Shader shader = new Shader();
//        mPaint.setShader(shader);
//        mPaint.setShadowLayer(2, 2, 2, Color.RED);
        canvas.drawArc(rect, -180, 180, false, mPaint);

        //绘制中心的文字
        float[] textWidthHeight = getTextWidthAndHeight(mCircleBean.levelDescText, mCircleBean.levelDescTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.levelDescTextColor);
        mPaint.setTextSize(mCircleBean.levelDescTextSize);
        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/din_bold.otf"));
        mPaint.setStrokeWidth(2f);
        mPaint.setShadowLayer(0, 0, 0, 0);
        canvas.drawText(mCircleBean.levelDescText, (getWidth() - textWidthHeight[0]) / 2, getWidth() / 2, mPaint);

        float[] textWidthHeight1 = getTextWidthAndHeight(mCircleBean.levelText, mCircleBean.levelTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.levelTextColor);
        mPaint.setTextSize(mCircleBean.levelTextSize);
        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/din_bold.otf"));
        mPaint.setStrokeWidth(1f);
        canvas.drawText(mCircleBean.levelText, (getWidth() - textWidthHeight1[0]) / 2,
                getWidth() / 2 - textWidthHeight[1] - dp2px(getContext(), 7), mPaint);

        //绘制下面的文案
        float[] textWidthHeight2 = getTextWidthAndHeight(mCircleBean.evaluateTimeText,
                mCircleBean.evaluateTimeTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.evaluateTimeTextColor);
        mPaint.setTextSize(mCircleBean.evaluateTimeTextSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setStrokeWidth(3f);
        canvas.drawText(mCircleBean.evaluateTimeText, (getWidth() - textWidthHeight2[0]) / 2,
                getWidth() / 2 + textWidthHeight[1] + mCircleBean.evaluateTimeTextMarginTop, mPaint);

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
        float outerCircleDescRadius = getWidth() / 2 - mSegmentDescTextHeight / 2
                - mFirstCircleSegmentBean.descTextMarginSegment;

        for (int i = 0, size = mCircleBean.mCircleSegmentBeanList.size(); i < size; i++) {
            CircleSegmentBean bean = mCircleBean.mCircleSegmentBeanList.get(i);
            /****************绘颜色环******************/
            mPaint.setStrokeWidth(mCircleBean.outerCircleWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(bean.color);
            float startAngle = -180 + i * bean.spaceAngle + bean.angle * i;
            canvas.drawArc(mRectF, startAngle, bean.angle, false, mPaint);

            /*****************绘外圈文字*****************/
            if (!TextUtils.isEmpty(bean.descText)) {
                //设置路径，以圆作为我们文本显示的路线；路径的绘制方式 CW 表示正序绘制，CCW表示倒序绘制
                mPath.addCircle((float) getWidth() / 2, (float) getWidth() / 2, outerCircleDescRadius,
                        Path.Direction.CW);
                if (bean.descText.contains(":")) {
                    String[] textArray = bean.descText.split(":");
                    float lastTextWidth = 0;
                    for (int j = 0, num = textArray.length; j < num; j++) {
                        mPaint.setColor(bean.descTextColor);
                        mPaint.setTextSize(bean.descTextSize);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setTypeface(
                                Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf"));
                        mPaint.setStrokeWidth(0f);
                        float padding = dp2px(getContext(), 7);
                        float splitTextAngle = getSplitTextAngle(bean.descText, outerCircleDescRadius, ":", bean.angle,
                                padding);
                        if (j > 0) {
                            lastTextWidth = lastTextWidth + getTextWidth(textArray[j - 1]);
                        }
                        float textAngle = getTextAngle(outerCircleDescRadius, bean.angle, bean.spaceAngle,
                                splitTextAngle, i, j, padding + lastTextWidth);
                        canvas.drawTextOnPath(textArray[j], mPath, getHOffset4Split(outerCircleDescRadius, textAngle),
                                0, mPaint);

                        /******************绘制小水滴图标******************/
                        if (mCircleBean.levelText.equalsIgnoreCase(textArray[j])) {
                            //图片偏移的角度
                            float offsetAngle = textAngle
                                    + getTextWidth(textArray[j]) / getPerimeter(outerCircleDescRadius) * 360 / 2;
                            float[] offset = getBitmapOffset(
                                    mCircleBean.innerCircleRadius + (float) mCircleBean.innerCircleWidth,
                                    offsetAngle);
                            float dripRadius = dp2px(getContext(), 7);
                            //设置画笔颜色和样式
                            mPaint.setColor(bean.color);
                            mPaint.setStyle(Paint.Style.FILL);

                            //水滴圆心坐标
                            float dripCenterX = offset[0];
                            float dripCenterY = offset[1];

                            //绘制水滴外圆
                            canvas.drawCircle(dripCenterX, dripCenterY, dripRadius, mPaint);

                            float dripAngle4Cal = (180 - mCircleBean.dripAngle) / 2;
                            //最远定点离圆心的距离
                            float triangle2CircleCenter = (float) (dripRadius / Math
                                    .cos(Math.toRadians(dripAngle4Cal)));

                            //水滴小三角顶点坐标
                            Path path = new Path();
                            float mTriangleX1 = (float) (dripCenterX - triangle2CircleCenter * Math
                                    .cos(Math.toRadians(offsetAngle)));
                            float mTriangleY1 = (float) (dripCenterY - triangle2CircleCenter * Math
                                    .sin(Math.toRadians(offsetAngle)));
                            float mTriangleX2 = (float) (dripCenterX - dripRadius * Math
                                    .cos(Math.toRadians(dripAngle4Cal - offsetAngle)));
                            float mTriangleY2 = (float) (dripCenterY + dripRadius * Math
                                    .sin(Math.toRadians(dripAngle4Cal - offsetAngle)));
                            float mTriangleX3 = (float) (dripCenterX - dripRadius * Math
                                    .cos(Math.toRadians(dripAngle4Cal + offsetAngle)));
                            float mTriangleY3 = (float) (dripCenterY - dripRadius * Math
                                    .sin(Math.toRadians(dripAngle4Cal + offsetAngle)));
                            //绘制顶部三角形
                            path.moveTo(mTriangleX1, mTriangleY1);
                            path.lineTo(mTriangleX2, mTriangleY2);
                            path.lineTo(mTriangleX3, mTriangleY3);
                            //lineto起点
                            path.close();
                            canvas.drawPath(path, mPaint);

                            //绘制水滴白色小内圆ff
                            mPaint.setColor(Color.rgb(0xff, 0xff, 0xff));
                            canvas.drawCircle(dripCenterX, dripCenterY,
                                    dripRadius / 2,
                                    mPaint);

                        }
                    }
                } else {
                    canvas.drawTextOnPath(bean.descText, mPath,
                            getHOffset(bean.descText, outerCircleDescRadius, bean.angle, bean.spaceAngle, i), 0,
                            mPaint);
                }
            }
        }
    }

    /**
     * 根据半径和偏移角度获取偏移的xy坐标
     *
     * @param radius      半径
     * @param offsetAngle 偏移角度
     */
    private float[] getBitmapOffset(float radius, float offsetAngle) {
        float tempX = (float) (radius * Math.cos(Math.toRadians(offsetAngle)));
        float tempY = (float) (radius * Math.sin(Math.toRadians(offsetAngle)));
        float offsetX = getWidth() / 2 - tempX;
        float offsetY = getWidth() / 2 - tempY;
        return new float[]{offsetX, offsetY};
    }

    /**
     * 获取小图标的偏移角度
     *
     * @param segmentAngle 颜色环每段的角度
     * @param spaceAngle   颜色环每段的间距角度
     * @param index        第几个颜色环
     */
    private float getBitmapOffsetAngle(float segmentAngle, float spaceAngle, int index) {
        return index * (segmentAngle + spaceAngle) + segmentAngle / 2;
    }

    /**
     * 获取文案的宽度
     */
    private int getTextHeight(String text) {
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 获取文案的宽度
     */
    private int getTextWidth(String text) {
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 获取文案被截断后间隔弧长度
     *
     * @param text         被截断的text
     * @param splitString  截取的符号
     * @param segmentAngle 每段颜色弧长
     * @param padding      左右留距
     */
    private float getSplitTextAngle(String text, float radius, String splitString, float segmentAngle, float padding) {
        float width = getTextWidth(text.replaceAll(splitString, ""));
        return (segmentAngle - (width + padding * 2) / getPerimeter(radius) * 360) / 2;
    }

    /**
     * 计算出每段环文案对应的偏移角度
     *
     * @param segmentAngle  颜色环每段的角度
     * @param spaceAngle    颜色环每段的间距角度
     * @param intervalAngle 颜色环内文案之间的角度
     * @param index         第几个颜色环
     * @param splitIndex    颜色环内的第几个文案
     * @param padding       颜色环文案左右的padding
     */
    private float getTextAngle(float radius, float segmentAngle, float spaceAngle, float intervalAngle,
            int index, int splitIndex, float padding) {
        float offsetAnage = index * (segmentAngle + spaceAngle) + padding / getPerimeter(radius) * 360
                + splitIndex * intervalAngle;
        return offsetAnage;
    }

    /**
     * 获取画圆环文字时的纵向偏移量
     *
     * @param radius            文案环的半径
     * @param textPositionAngle 文案位置的弧度
     */
    private float getHOffset4Split(float radius, float textPositionAngle) {
        System.out.println("111111 offsetAnage = " + textPositionAngle);
        //计算出每段颜色环的偏移
        float offset = textPositionAngle / 360 * getPerimeter(radius);
        float hOffset = getPerimeter(radius) / 2 + offset;
        return hOffset;
    }

    /**
     * 获取画圆环文字时的纵向偏移量
     *
     * @param text         文案，主要是为了计算文案的长度
     * @param radius       文案环的半径
     * @param segmentAngle 颜色环每段的角度
     * @param spaceAngle   颜色环每段的间距角度
     * @param index        第几个颜色环
     */
    private float getHOffset(String text, float radius, float segmentAngle, float spaceAngle, int index) {
        float hOffset = 0;
        float width = getTextWidth(text);
        //计算出这个文案占整个圆环的角度
        float textAngle = width / getPerimeter(radius) * 360;
        //计算出每段环文案对应的偏移角度；最后这个0.5主要是为了精度的问题，没有实际意义
        float offsetAnage = (segmentAngle - textAngle) / 2 + index * (segmentAngle + spaceAngle);
        //计算出每段颜色环的偏移
        float offset = offsetAnage / 360 * getPerimeter(radius);
        hOffset = getPerimeter(radius) / 2 + offset;
        return hOffset;
    }

    /**
     * 获取圆环周长
     */
    private float getPerimeter(float radius) {
        return (float) (2 * Math.PI * radius);
    }

    /**
     * 根据文案内容和字体大小获取字体宽度
     */
    private float[] getTextWidthAndHeight(String text, int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return new float[]{Float.valueOf(rect.width()), Float.valueOf(rect.height())};
    }

    public int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
