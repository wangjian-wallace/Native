package com.wallace.tools.ui.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Package com.wallace.tools.ui.game
 * Created by Wallace.
 * on 2017/7/7.板
 */

public class TopPlate {

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 1;

    private int wight;
    private int height;

    private int type = LEVEL_1;

    private Paint mPaint;

    public TopPlate(int wight, int height) {
        this.wight = wight;
        this.height = height;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void draw(Canvas mCanvas, RectF rect)
    {
        mPaint.setColor(type == LEVEL_1 ? Color.BLUE:Color.RED);
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.drawRect(rect,mPaint);
        mCanvas.restore();
    }

    public int getWight() {
        return wight;
    }

    public int getHeight() {
        return height;
    }
}
