package com.wallace.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Package com.wallace.view
 * Created by Wallace.
 * on 2017/3/15.
 * <br/>
 * 配合{@link PTouchView}使用
 */

public class ProtractorView extends View{

//    private PointF pointer;
//    private Paint cyclePaint;
    private Paint linePaint;
    private Paint textPaint;
    private RectF cycleRectF;

    public ProtractorView(Context context) {
        this(context,null);
    }

    public ProtractorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProtractorView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        cyclePaint = new Paint();
//        cyclePaint.setStrokeWidth(2);
//        cyclePaint.setColor(Color.BLACK);
//        cyclePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setStrokeWidth(4);
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(42);
        textPaint.setColor(0xffff0000);
        cycleRectF = new RectF();
    }

    private float radius;
    private float paddingLeft;
    private int height;
//    private int width;
//    private int top;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = getHeight();
//        width = getWidth();
        paddingLeft = getPaddingLeft();
        int top = height / 8;
        radius = top * 3;
        cycleRectF.top = top;
        cycleRectF.bottom = top * 7;
        cycleRectF.left = -(radius - paddingLeft);
        cycleRectF.right =radius + paddingLeft;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(paddingLeft,height/2,20,linePaint);

        onDrawCycle(canvas);
    }
    private void onDrawCycle(Canvas canvas){
        canvas.save();
        canvas.translate(paddingLeft,height/2);
        canvas.rotate(270);

        for (int i = 0; i <= 180; i++) {
            PointF coordinate = getPointer(radius, i);
            float x = coordinate.x;
            float y = coordinate.y;
            float r = radius - 60;
            if ((i % 5) == 0) {
                if ((i & 0x1) == 0) {
                    r = radius - 120;
                } else {
                    r = radius - 90;
                }
            }
            PointF coordinate1 = getPointer(r, i);
            float x1 = coordinate1.x ;
            float y1 = coordinate1.y ;
            canvas.drawLine(x1, y1, x, y, linePaint);
        }
        canvas.restore();
        canvas.save();
        canvas.translate(paddingLeft,height/2);
        canvas.rotate(90);
        for (int i = 0; i <= 180 ; i++){
            if ((i % 5) == 0){
                if ((i & 0x1) == 0){
                    String text = String.valueOf(i);
                    Path path = getTextPath(text, textPaint, i, radius
                            - 160 - 22 * 5 / 4);
                    canvas.drawTextOnPath(text, path, 0, 0, textPaint);
                }
            }
        }
        canvas.restore();
    }
    private Path getTextPath(String text, Paint paint, double degree, float r) {
        double pathDegree = Math.abs(90 - degree);
        float textWidth = paint.measureText(text);
        float y = Math.abs((float) (textWidth * Math.sin(pathDegree / 180
                * Math.PI)));
        float x = Math.abs((float) (textWidth * Math.cos(pathDegree / 180
                * Math.PI)));
        PointF coordinate = getPointer(r, degree);
        PointF start = new PointF();
        PointF end = new PointF();
        if (degree < 90) {
            end.set(-coordinate.x + x / 2,-coordinate.y - y / 2);
            start.set(-coordinate.x - x / 2,-coordinate.y + y / 2);
        } else {
            end.set(-coordinate.x + x / 2,-coordinate.y + y / 2);
            start.set(-coordinate.x - x / 2,-coordinate.y - y / 2);
        }
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        return path;
    }
//    private static final String TAG = "Protractor";
    private PointF getPointer(float r, double degree) {
        float x = (float) (r * Math.cos(degree / 180 * Math.PI));
        float y = (float) (r * Math.sin(degree / 180 * Math.PI));
        return new PointF(x, y);
    }

}
