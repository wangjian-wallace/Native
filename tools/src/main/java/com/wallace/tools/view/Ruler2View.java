package com.wallace.tools.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;

/**
 * Package com.wallace.view
 * Created by Wallace.
 * on 2017/3/14.
 */

public class Ruler2View extends View{

//    private static final String TAG = "Ruler2View";
    private Unit unit;
    private DisplayMetrics dm;

    private Paint scalePaint;
    private Paint labelPaint;
    private Paint linePaint;
    private Paint mPaint;
    private PointF linePointer;

    private float guideScaleTextSize = 40;
//    private float graduatedScaleBaseLength = 100;

//    private float labelTextSize = 60;
//    private boolean isMove = false;

//    private OnRulerClickListener onRulerClickListener;
//
//    public void setOnRulerClickListener(OnRulerClickListener onRulerClickListener) {
//        this.onRulerClickListener = onRulerClickListener;
//    }

    public Ruler2View(Context context) {
        this(context,null);
    }

    public Ruler2View(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Ruler2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        dm = getResources().getDisplayMetrics();
        unit = new Unit(dm.ydpi);
        unit.setType(1);

        scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scalePaint.setStrokeWidth(4);
        scalePaint.setTextSize(guideScaleTextSize);
        scalePaint.setColor(0xFF03070A);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(4);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(60);
        labelPaint.setColor(0xFFff070A);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(0xFFFFFFFF);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        linePointer = new PointF();
        linePointer.set(w/2,h/2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                float x = event.getX();
                float y = event.getY();
                if (linePointer != null) {
                    float y1 = linePointer.y + 120;
                    float y2 = linePointer.y - 120;
                    //                        if (onRulerClickListener != null){
//                            onRulerClickListener.onClick();
//                        }
                    return y > y2 && y < y1;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }


//    private boolean isTo = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
//        int pointerId = event.getPointerId(pointerIndex);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
//                isMove = false;
//                isTo = false;
//                float x = event.getX();
//                float y = event.getY();
//                if (linePointer != null){
//                    float y1 = linePointer.y + 70;
//                    float y2 = linePointer.y - 70;
//                    float x1 = linePointer.x + 70;
//                    float x2 = linePointer.x - 70;
//                    if (y > y2 && y < y1 && x > x2 && x < x1){
//                        isTo = true;
//                    }
//                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
//                isMove = true;
//                isTo = true;
                if (linePointer!= null){
                    linePointer.set(event.getX(pointerIndex), event.getY(pointerIndex));
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (linePointer!= null){
                    linePointer.set(event.getX(pointerIndex), event.getY(pointerIndex));
                }
//                isMove = false;
//                if (!isTo){
//                    if (onRulerClickListener != null){
//                        onRulerClickListener.onClick();
//                    }
//                }
                break;
            }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int paddingTop = getPaddingTop();
//        int paddingLeft = getPaddingLeft();
        unit.setType(1);
        float cm = 0f;
        Iterator<Unit.Graduation> pixelsIterator = unit.getPixelIterator(height - paddingTop);
        while(pixelsIterator.hasNext()) {
            Unit.Graduation graduation = pixelsIterator.next();

            float startX = width - graduation.relativeLength * 100;
            float startY = paddingTop + graduation.pixelOffset;
            cm = startX;
            canvas.drawLine(startX, startY, (float) width, startY, scalePaint);

            if (graduation.value % 1 == 0) {
                int va = (int) graduation.value;
                String text = va + "";

                canvas.save();
                canvas.translate(
                        startX - guideScaleTextSize, startY - scalePaint.measureText(text) / 2);
                canvas.rotate(90);
                canvas.drawText(va == 0 ? "cm" : text, 0, 0, scalePaint);
                canvas.restore();
            }
        }
        float in = 0f;
        unit.setType(0);
        Iterator<Unit.Graduation> pixelsIterator2 = unit.getPixelIterator(height - paddingTop);
        while(pixelsIterator2.hasNext()) {
            Unit.Graduation graduation = pixelsIterator2.next();

            float startX = graduation.relativeLength * 100;
            float startY = paddingTop + graduation.pixelOffset;
            in = startX;
            canvas.drawLine(0, startY, startX, startY, scalePaint);

            if (graduation.value % 1 == 0) {
                int va = (int) graduation.value;
                String text = va + "";

                canvas.save();
                canvas.translate(
                        startX + guideScaleTextSize, startY - scalePaint.measureText(text)/2+paddingTop/2);
                canvas.rotate(270);
                canvas.drawText(va == 0 ? "in" : text, 0, 0, scalePaint);
                canvas.restore();
            }
        }
        canvas.drawLine(0,paddingTop,width,paddingTop,linePaint);

//        if (!isMove){
//            linePointer.set(width/2,(height + paddingTop)/2);
//        }
        canvas.drawLine(0,linePointer.y,width,linePointer.y,linePaint);
        String labelText = "";
        String labelIn = "";
        if (linePointer != null) {
            float distanceInPixels = Math.abs(paddingTop - linePointer.y);
            labelText = getCMRepresentation(distanceInPixels / getPixelsPerUnit());
            labelIn = getINRepresentation(distanceInPixels / dm.ydpi);
        }
        canvas.save();
        canvas.translate(
                cm - guideScaleTextSize*5, linePointer.y/2 - labelPaint.measureText(labelText)/2);
        canvas.rotate(90);
        canvas.drawText(labelText, 0, 0, labelPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(
                in + guideScaleTextSize*5, linePointer.y/2 + labelPaint.measureText(labelIn)/2);
        canvas.rotate(270);
        canvas.drawText(labelIn, 0, 0, labelPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(width/2,linePointer.y);
//        Path path = new Path();
//        path.addCircle(0,0,50, Path.Direction.CW);
        canvas.drawCircle(0,0,50,labelPaint);
//        canvas.drawPath(path,labelPaint);
        canvas.drawLine(-16,0,16,0,mPaint);
        canvas.drawLine(-16,-15,16,-15,mPaint);
        canvas.drawLine(-16,15,16,15,mPaint);
        canvas.restore();

    }

    private float getPixelsPerUnit() {
        return dm.ydpi / 2.54f;
    }
    @SuppressLint("DefaultLocale")
    public String getCMRepresentation(float value) {
        String suffix = "CM";
        return String.format("%.2f %s", value, suffix);
    }
    @SuppressLint("DefaultLocale")
    public String getINRepresentation(float value) {
        String suffix = value > 1 ? "Inches" : "Inch";
        return String.format("%.2f %s", value, suffix);
    }
    @Override
    protected int getSuggestedMinimumWidth() {
        return 200;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 200;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int width = Math.max(minWidth, MeasureSpec.getSize(widthMeasureSpec));

        int minHeight = getPaddingBottom() + getPaddingTop() + getSuggestedMinimumHeight();
        int height = Math.max(minHeight, MeasureSpec.getSize(heightMeasureSpec));

        setMeasuredDimension(width, height);
    }
//    public interface OnRulerClickListener{
//        void onClick();
//    }

    private class Unit {

        class Graduation {
            float value;
            int pixelOffset;
            float relativeLength;
        }

        private static final int INCH = 0;
        private static final int CM = 1;

        private int type = INCH;
        private float dpi;

        Unit(float dpi) {
            this.dpi = dpi;
        }

        private void setType(int type) {
            if (type == INCH || type == CM) {
                this.type = type;
            }
        }

        private Iterator<Graduation> getPixelIterator(final int numberOfPixels) {
            return new Iterator<Graduation>() {
                int graduationIndex = 0;
                Graduation graduation = new Graduation();

                private float getValue() {
                    return graduationIndex * getPrecision();
                }

                private int getPixels() {
                    return (int) (getValue() * getPixelsPerUnit());
                }

                @Override
                public boolean hasNext() {
                    return getPixels() <= numberOfPixels;
                }

                @Override
                public Graduation next() {
                    graduation.value = getValue();
                    graduation.pixelOffset = getPixels();
                    graduation.relativeLength = getGraduatedScaleRelativeLength(graduationIndex);

                    graduationIndex ++;
                    return graduation;
                }

                @Override
                public void remove() {

                }
            };
        }

        private float getPixelsPerUnit() {
            if (type == INCH) {
                return dpi;
            } else if (type == CM) {
                return dpi / 2.54f;
            }
            return 0;
        }

        private float getPrecision() {
            if (type == INCH) {
                return 1 / 4f;
            } else if (type == CM) {
                return 1 / 10f;
            }
            return 0;
        }

        private float getGraduatedScaleRelativeLength(int graduationIndex) {
            if (type == INCH) {
                if (graduationIndex % 4 == 0) {
                    return 1f;
                } else if (graduationIndex% 2 == 0) {
                    return 3 / 4f;
                } else {
                    return 1 / 2f;
                }
            } else if (type == CM) {
                if (graduationIndex % 10 == 0) {
                    return 1;
                } else if (graduationIndex % 5 == 0) {
                    return 3 / 4f;
                } else {
                    return 1 / 2f;
                }
            }
            return 0;
        }

    }
}
