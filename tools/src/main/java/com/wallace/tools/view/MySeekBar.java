package com.wallace.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wallace.tools.R;


/**
 * Created by akmob on 2017/10/10.
 */

public class MySeekBar extends View {
    private static final String TAG = "SeekBar";
    private static final int DEFAULT_COLOR = Color.parseColor("#ff34a6a3");
    private static final int BACKGROUND_COLOR = Color.parseColor("#ffdddddd");
    private Paint mBackgroundPaint;
    private Paint mBackgroundPaint2;
    private Paint mPaint;
    private Paint mTextPaint2;
    private Paint mTextPaint;

    private float mDefaultHeight;
    private float mDefaultLine = 40;
    private float mDefaultWidth;

    private float move = 0;
    private int interval = 40;

    private Bitmap bitmap;
    private float bitmapW;
    private float bitmapH;

    private int smallText = 5000;
    private int bigText = 45000;

    private OnChangeText onChangeText;
    private String text;


    public MySeekBar(Context context) {
        this(context,null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar2);
        interval = attribute.getInt(R.styleable.MySeekBar2_seekInterval, interval);
        move = attribute.getInt(R.styleable.MySeekBar2_seekMove, 0);
        smallText = attribute.getInt(R.styleable.MySeekBar2_seekSmallText, smallText);
        bigText = attribute.getInt(R.styleable.MySeekBar2_seekBigText, bigText);

        attribute.recycle();
        mBackgroundPaint = new Paint();
        mBackgroundPaint2 = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint2.setAntiAlias(true);

        mBackgroundPaint.setColor(BACKGROUND_COLOR);
        mBackgroundPaint2.setColor(Color.parseColor("#c9FFffff"));
        mBackgroundPaint2.setStrokeWidth(50);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setColor(DEFAULT_COLOR);

        mTextPaint = new Paint();
        mTextPaint2 = new Paint();

        mTextPaint.setAntiAlias(true);
        mTextPaint2.setAntiAlias(true);
        mTextPaint.setTextSize(45);
        mTextPaint2.setTextSize(30);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint.setTypeface(font);
        mTextPaint.setColor(Color.parseColor("#000000"));
        mTextPaint2.setColor(Color.parseColor("#000000"));
    }

    public void setInterval(int interval) {
        this.interval = interval;
        invalidate();
    }

    public void setDefaultLine(int mDefaultLine) {
        this.mDefaultLine = mDefaultLine;
        invalidate();
    }

    public void setSmallText(int smallText) {
        this.smallText = smallText;
        invalidate();
    }

    public void setBigText(int bigText) {
        this.bigText = bigText;
        invalidate();
    }

    public void init(int smallText,int bigText,int interval,int move){
        this.interval = interval;
        this.smallText = smallText;
        this.bigText = bigText;
        this.move = move;
        invalidate();
    }

    public void setOnChangeText(OnChangeText onChangeText) {
        this.onChangeText = onChangeText;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDefaultHeight = getHeight();
        mDefaultWidth = getWidth();


        bitmap = Bitmap.createBitmap(
                100,
               100,
                Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);

//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.home_icon_slide);

        bitmapW = bitmap.getWidth();
        bitmapH = bitmap.getHeight();
        mDefaultLine = bitmapH / 3;
        mBackgroundPaint.setStrokeWidth(mDefaultLine);
        mPaint.setStrokeWidth(mDefaultLine);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                move = event.getX();
                invalidate();
//                setChange();
                break;
            case MotionEvent.ACTION_MOVE:
                move = event.getX();
                invalidate();
//                setChange();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float h = bitmapH / 2;
        canvas.drawLine(0,h,mDefaultWidth,h,mBackgroundPaint);

        String b = String.valueOf(bigText);
        canvas.drawText(smallText +"",0,bitmapH * 2,mTextPaint2);
        canvas.drawText(b,mDefaultWidth - b.length()*15 - 20,bitmapH*2,mTextPaint2);

        float singleInterval = mDefaultWidth / interval;
        if (move > 0 && move <= mDefaultWidth){
            for (int i = 0; i <= interval; i++){
                float o = i * singleInterval;
                if (move <= o && move > o - singleInterval){
                    canvas.drawLine(0,h,i * singleInterval,h,mPaint);
                    int t = (bigText - smallText )/ interval * i + smallText;
                    text = String.valueOf(t);
                    float w = o - bitmapW / 2;
                    float tw = o - text.length() * 15;
                    if (i == interval){
                        w = o - bitmapW;
                    }
                    canvas.drawBitmap(bitmap,w ,0,mPaint);
                    if (tw <= 0){
                        tw = 0;
                    }
                    if (o + text.length() * 15 >= mDefaultWidth){
                        tw = mDefaultWidth - text.length() * 30;
                    }
                    canvas.drawLine(tw - 20,bitmapH * 2 -15,o + text.length() * 15 + 5,bitmapH * 2 -15,mBackgroundPaint2);
                    canvas.drawText(text,tw , bitmapH*2,mTextPaint);
                    setChange();
                }
            }
        }else if (move > mDefaultWidth){
            text = String.valueOf(bigText);
            setChange();
//            int t = (bigText - smallText )/ interval * i + smallText;
            String s = String.valueOf(bigText);
            float w = mDefaultWidth - bitmapW;
            float tw = mDefaultWidth - s.length() * 30;
            canvas.drawBitmap(bitmap,w ,0,mPaint);
            canvas.drawLine(0,h,mDefaultWidth,h,mPaint);
            canvas.drawLine(tw - 20,bitmapH * 2 -15,mDefaultWidth + s.length() * 15 + 5,bitmapH * 2 -15,mBackgroundPaint2);
            canvas.drawText(s,tw , bitmapH*2,mTextPaint);
        } else {
            text = String.valueOf(smallText);
            setChange();
            canvas.drawBitmap(bitmap,0,0,mPaint);
        }
    }
    private void setChange(){
        if (onChangeText == null)
            return;
        Log.d(TAG, "setChange: " + text);
        onChangeText.getText(text);
    }

    public String getText() {
        return text;
    }

    public interface OnChangeText{
        void getText(String text);
    }
}
