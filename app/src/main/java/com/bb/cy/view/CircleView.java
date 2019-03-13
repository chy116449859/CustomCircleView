package com.bb.cy.view;

import com.bb.cy.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
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
        mPaint.setStrokeWidth(9f);
        canvas.drawText(mCircleBean.levelText, (getWidth() - textWidthHeight1[0]) / 2,
                getWidth() / 2 - textWidthHeight[1] - 21, mPaint);

        //绘制下面的文案
        float[] textWidthHeight2 = getTextWidthAndHeight(mCircleBean.evaluateTimeText,
                mCircleBean.evaluateTimeTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBean.evaluateTimeTextColor);
        mPaint.setTextSize(mCircleBean.evaluateTimeTextSize);
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
        float outerCircleDescRadius = getWidth() / 2 - mSegmentDescTextHeight
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
            mPaint.setColor(bean.descTextColor);
            mPaint.setTextSize(18);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(0f);
            if (!TextUtils.isEmpty(bean.descText)) {
                //设置路径，以圆作为我们文本显示的路线；路径的绘制方式 CW 表示正序绘制，CCW表示倒序绘制
                mPath.addCircle((float) getWidth() / 2, (float) getWidth() / 2, outerCircleDescRadius,
                        Path.Direction.CW);
                canvas.drawTextOnPath(bean.descText, mPath,
                        getHOffset(bean.descText, outerCircleDescRadius, bean.angle, bean.spaceAngle, i), 0, mPaint);
            }

            /******************绘制小水滴图标******************/
            if (mCircleBean.creditLevel == i) {
                //图片偏移的角度
                float offsetAngle = getBitmapOffsetAngle(bean.angle, bean.spaceAngle, i);
                float[] offset = getBitmapOffset(
                        mCircleBean.innerCircleRadius + (float) mCircleBean.innerCircleWidth / 2,
                        offsetAngle);
                float dripRadius = (float) mCircleBean.innerCircleWidth * 0.75f;
                //设置画笔颜色和样式
                mPaint.setColor(bean.color);
                mPaint.setStyle(Paint.Style.FILL);

                //水滴圆心坐标
                float dripCenterX = offset[0];
                float dripCenterY = offset[1];
                if (offsetAngle > 90) {
                    dripCenterX = offset[0];
                    dripCenterY = offset[1];
                }

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
                canvas.drawCircle(dripCenterX, dripCenterY, dripRadius - mCircleBean.innerCircleWidth * 0.36f,
                        mPaint);

            } else {
//                mBitmap = tintBitmap(mBitmap, bean.color);
//                float offsetAngle = getBitmapOffsetAngle(bean.angle, bean.spaceAngle, i);
//                float[] offset = getBitmapOffset(mCircleBean.innerCircleRadius + mCircleBean.innerCircleWidth / 2,
//                        offsetAngle);
//                Matrix matrix = new Matrix();
//                matrix.postTranslate(-(float) mBitmap.getWidth() / 2, -(float) mBitmap.getHeight() / 2);//步骤1
//                matrix.postRotate(offsetAngle + 270);//
//                matrix.postTranslate(offset[0], offset[1]);//步骤3  
//                canvas.drawBitmap(mBitmap, matrix, null);//步骤4
//                matrix.reset();
            }
        }
    }

    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
//        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
//        Canvas canvas = new Canvas(outBitmap);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setDither(true);// 图像过滤
//        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.MULTIPLY));
//        canvas.drawBitmap(inBitmap, 0, 0, paint);
//        return outBitmap;

        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);// 图像过滤
        ColorMatrix matrix = new ColorMatrix();

        matrix.setRotate(0, Color.green(tintColor)); //控制让红**在色轮上旋转hueColor葛角度
        matrix.setRotate(1, Color.blue(tintColor)); //控制让绿红**在色轮上旋转hueColor葛角度
        matrix.setRotate(2, Color.red(tintColor)); //控制让蓝**在色轮上旋转hueColor葛角度
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;

//        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
//        Canvas canvas = new Canvas(outBitmap);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setDither(true);// 图像过滤
//        paint.setColorFilter(new LightingColorFilter(tintColor, tintColor));
//        canvas.drawBitmap(inBitmap, 0, 0, paint);
//        return outBitmap;

//        Bitmap resultBitmap = Bitmap.createBitmap(inBitmap, 0, 0,
//                inBitmap.getWidth() - 1, inBitmap.getHeight() - 1);
//        Paint p = new Paint();
//        p.setAntiAlias(true);
//        p.setDither(true);// 图像过滤
//        ColorFilter filter = new LightingColorFilter(tintColor, 1);
//        p.setColorFilter(filter);
//        Canvas canvas = new Canvas(resultBitmap);
//        canvas.drawBitmap(resultBitmap, 0, 0, p);
//        return resultBitmap;

        // start with a Bitmap bmp
//        Bitmap newBmp = inBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas c = new Canvas(newBmp);
//
//        // get the int for the colour which needs to be removed
//        Paint paint = new Paint();// 去锯齿
//        paint.setAntiAlias(true);// 防抖动
//        paint.setDither(true);// 图像过滤
//        paint.setFilterBitmap(true);
//        paint.setARGB(255, 0, 0, 0); // ARGB for the color to replace,black replace gray
//        paint.setXfermode(new AvoidXfermode(0x616161, 50, AvoidXfermode.Mode.TARGET));
//        c.drawPaint(paint);
//        return newBmp;
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int[] mArrayColor = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = mBitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
//                if (color == oldColor) {
                if (Color.argb(0x00, 0x00, 0x00, 0x00) != color) {
                    mBitmap.setPixel(j, i, newColor);  //将白色替换成透明色
                }
//                }

            }
        }
        return mBitmap;
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
     * 获取小图标的偏移角度，为什么-90？因为左上区是负数
     *
     * @param segmentAngle 颜色环每段的角度
     * @param spaceAngle   颜色环每段的间距角度
     * @param index        第几个颜色环
     */
    private float getBitmapOffsetAngle(float segmentAngle, float spaceAngle, int index) {
        return index * (segmentAngle + spaceAngle) + segmentAngle / 2;
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
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();
        //计算出这个文案占整个圆环的角度
        float textAngle = width / getPerimeter(radius) * 360;
        //计算出每段环文案对应的偏移角度；最后这个0.5主要是为了精度的问题，没有实际意义
        float offsetAnage = (segmentAngle - textAngle) / 2 + index * (segmentAngle + spaceAngle) + 0.5f;
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
}
