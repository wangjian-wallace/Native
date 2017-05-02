package com.wallace.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Package com.wallace.view
 * Created by Wallace.
 * on 2017/3/15.
 * <br/>触摸的线还有角度文字配合{@link ProtractorView}使用
 */

public class PTouchView extends View{

    private Paint linePaint;
    private Paint arcPaint;
    private Path mPath;
    private Paint textPaint;
    private SparseArray<PointF> activePointers;

    public PTouchView(Context context) {
        this(context,null);
    }

    public PTouchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        activePointers = new SparseArray<>();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(4);
        linePaint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(85);
        textPaint.setColor(Color.RED);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(4);
        arcPaint.setColor(Color.RED);

        mPath = new Path();

    }

    private int paddingLeft;
    private int width;
    private int height;
    private int radius;
    private int top;
    private RectF rect;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = getHeight();
        width = getWidth();
        paddingLeft = getPaddingLeft();
        top = height / 8;
        radius = top * 3;
        PointF pointF = new PointF();
        pointF.set(paddingLeft,radius - 160);
        activePointers.put(0,pointF);
        float r = height / 8 * 2 ;
        float left = r - paddingLeft;
        rect = new RectF(-left, r, r + paddingLeft,height - r);
    }

    /**
     * 规定触摸的范围，再决定是否交给父布局
     * @param event 触摸
     * @return 是否在触摸的范围
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (x > radius || y < top || y > top + radius * 2){
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        int pointerIndex = event.getActionIndex();
//        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int numberOfPointers = activePointers.size();
                for (int i = 0; i < numberOfPointers; i++) {
                    PointF point = activePointers.get(event.getPointerId(i));
                    if (point == null) {
                        continue;
                    }
                    point.x = event.getX(i);
                    point.y = event.getY(i);

                }
                PointF pointF = new PointF(event.getX(),event.getY());
                calculatePoint(pointF);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int numberOfPointers = activePointers.size();
                for (int i = 0; i < numberOfPointers; i++) {
                    PointF point = activePointers.get(event.getPointerId(i));
                    if (point == null) {
                        continue;
                    }
                    point.x = event.getX(i);
                    point.y = event.getY(i);

                }
                PointF pointF = new PointF(event.getX(),event.getY());
                calculatePoint(pointF);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                PointF pointF = new PointF(event.getX(),event.getY());
                calculatePoint(pointF);
                break;
            }
        }

        invalidate();
        return true;
    }
    private void calculatePoint(PointF pointF) {
        float dy = height / 2 - pointF.y;
        float dx = pointF.x - paddingLeft;

        kedu = (int) Math.round(Math.atan(dy /dx) / Math.PI * 180);
        if (pointF.y < height/2){
            kedu = 90 - kedu;
        }else if (pointF.y == height / 2){
            kedu = 90;
        }else if (pointF.y > height/2){
            kedu = 90 +  Math.abs(kedu);
        }
        if (pointF.y > height/2 && pointF.x < paddingLeft){
            kedu = 180;
        }
//        invalidate();
    }
    private float kedu = 0f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPath.addArc(rect,270,kedu);
        int numberOfPointers = activePointers.size();
        if (kedu == 180){
            int h = height /2;
            mPath.moveTo(paddingLeft,h);
            mPath.lineTo(paddingLeft,height);
        }else {
            for (int i = 0; i < numberOfPointers; i++){
                PointF pointF = activePointers.get(i);
//            canvas.drawLine(paddingLeft,height/2,pointF.x ,pointF.y,linePaint);
                float h = height /2;
                float w = pointF.x + (width - pointF.x );
                mPath.moveTo(paddingLeft,h);
                if (pointF.x < paddingLeft * 3){
                    w = paddingLeft + 300;
                }
                if (pointF.x < paddingLeft * 2){
                    w = paddingLeft + 100;
                }

//                if (pointF.x < rect.right && pointF.y > rect.top && pointF.y < h){
//                    w = width;
//                }
                mPath.lineTo(w, getYs(w,paddingLeft,pointF.x,h,pointF.y));
            }
        }

        canvas.drawPath(mPath, arcPaint);
        canvas.drawLine(paddingLeft,height/2,paddingLeft,0,linePaint);
        canvas.drawText(String.valueOf(kedu)+" °",radius + radius / 4,radius / 4,textPaint);
        mPath.reset();
    }
    private float getYs(float x,float x1,float x2,float y1,float y2){
        return (x - x1) / (x2 - x1) * (y2 - y1) + y1;
    }
}
