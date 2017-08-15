package com.wallace.tools.ui.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Package com.wallace.tools.ui.game
 * Created by Wallace.
 * on 2017/7/7.
 */

public class Circle {

    private int radius;
    private int c;
    private Paint mPaint;

    private int x;
    private int y;

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    public void draw(Canvas mCanvas)
    {
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.drawCircle(x,y,radius,mPaint);
        mCanvas.restore();
    }

    public boolean isOver(int h){
        return y > h + 10;
    }
}
