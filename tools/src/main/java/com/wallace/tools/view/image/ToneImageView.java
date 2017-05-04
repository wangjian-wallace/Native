package com.wallace.tools.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wallace.tools.R;

import java.util.ArrayList;

/**
 * Package com.wallace.tools.view.image
 * Created by Wallace.
 * on 2017/5/4.
 */

public class ToneImageView extends BaseImage implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "ToneImageView";

    /**
     * 饱和度标识
     */
    public static final int FLAG_SATURATION = 0x0;

    /**
     * 亮度标识
     */
    public static final int FLAG_LUM = 0x1;

    /**
     * 色相标识
     */
    public static final int FLAG_HUE = 0x2;

    /**
     * 饱和度
     */
    private TextView mSaturation;
    private SeekBar mSaturationBar;

    /**
     * 色相
     */
    private TextView mHue;
    private SeekBar mHueBar;

    /**
     * 亮度
     */
    private TextView mLum;
    private SeekBar mLumBar;

    private float mDensity;
    private static final int TEXT_WIDTH = 50;

    private LinearLayout mParent;

    private ColorMatrix mLightnessMatrix;
    private ColorMatrix mSaturationMatrix;
    private ColorMatrix mHueMatrix;
    private ColorMatrix mAllMatrix;

    /**
     * 亮度
     */
    private float mLumValue = 1F;

    /**
     * 饱和度
     */
    private float mSaturationValue = 0F;

    /**
     * 色相
     */
    private float mHueValue = 0F;

    /**
     * SeekBar的中间值
     */
    private static final int MIDDLE_VALUE = 127;

    /**
     * SeekBar的最大值
     */
    private static final int MAX_VALUE = 255;

    private int flag = FLAG_HUE;

    private ArrayList<SeekBar> mSeekBars = new ArrayList<>();

    public ToneImageView(Context context) {
        this(context,null);
    }

    public ToneImageView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ToneImageView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mDensity = getResources().getDisplayMetrics().density;

        mSaturation = new TextView(getContext());
        mSaturation.setText("饱和度");
        mHue = new TextView(getContext());
        mHue.setText("色相");
        mLum = new TextView(getContext());
        mLum.setText("亮度");

        mSaturationBar = new SeekBar(getContext());
        mHueBar = new SeekBar(getContext());
        mLumBar = new SeekBar(getContext());

        mSeekBars.add(mSaturationBar);
        mSeekBars.add(mHueBar);
        mSeekBars.add(mLumBar);

        for (int i = 0, size = mSeekBars.size(); i < size; i++) {
            SeekBar seekBar = mSeekBars.get(i);
            seekBar.setMax(MAX_VALUE);
            seekBar.setProgress(MIDDLE_VALUE);
            seekBar.setTag(i);
        }

        LinearLayout saturation = new LinearLayout(getContext());
        saturation.setOrientation(LinearLayout.HORIZONTAL);
        saturation.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams txtLayoutparams = new LinearLayout.LayoutParams((int) (TEXT_WIDTH * mDensity), LinearLayout.LayoutParams.MATCH_PARENT);
        mSaturation.setGravity(Gravity.CENTER);
        saturation.addView(mSaturation, txtLayoutparams);

        LinearLayout.LayoutParams seekLayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        saturation.addView(mSaturationBar, seekLayoutparams);

        LinearLayout hue = new LinearLayout(getContext());
        hue.setOrientation(LinearLayout.HORIZONTAL);
        hue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        mHue.setGravity(Gravity.CENTER);
        hue.addView(mHue, txtLayoutparams);
        hue.addView(mHueBar, seekLayoutparams);

        LinearLayout lum = new LinearLayout(getContext());
        lum.setOrientation(LinearLayout.HORIZONTAL);
        lum.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        mLum.setGravity(Gravity.CENTER);
        lum.addView(mLum, txtLayoutparams);
        lum.addView(mLumBar, seekLayoutparams);

        mParent = new LinearLayout(getContext());
        mParent.setOrientation(LinearLayout.VERTICAL);
        mParent.setBackgroundColor(Color.WHITE);
        mParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mParent.addView(saturation);
        mParent.addView(hue);
        mParent.addView(lum);

    }
    public View getParentView() {
        return mParent;
    }
    /**
     * 设置饱和度值
     * @param saturation
     */
    public void setSaturation(int saturation) {
        mSaturationValue = saturation * 1.0F / MIDDLE_VALUE;
    }

    /**
     * 设置色相值
     * @param hue
     */
    public void setHue(int hue) {
        mHueValue = hue * 1.0F / MIDDLE_VALUE;
    }

    /**
     * 设置亮度值
     * @param lum
     */
    public void setLum(int lum) {
        mLumValue = (lum - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
    }

    public ArrayList<SeekBar> getSeekBars()
    {
        return mSeekBars;
    }
    public void setFlag(int flag){
        this.flag = flag;
    }

    private Bitmap bmp;

    public Bitmap getBmp() {
        return bmp;
    }

    @Override
    public Bitmap getImage() {
        Log.d(TAG, "getImage: ");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sa);
        bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
        if (null == mAllMatrix) {
            mAllMatrix = new ColorMatrix();
        }

        if (null == mLightnessMatrix) {
            mLightnessMatrix = new ColorMatrix(); // 用于颜色变换的矩阵，android位图颜色变化处理主要是靠该对象完成
        }

        if (null == mSaturationMatrix) {
            mSaturationMatrix = new ColorMatrix();
        }

        if (null == mHueMatrix) {
            mHueMatrix = new ColorMatrix();
        }

        switch (flag) {
            case FLAG_HUE: // 需要改变色相
                mHueMatrix.reset();
                mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考
                // // android
                // doc
                break;
            case FLAG_SATURATION: // 需要改变饱和度
                // saturation 饱和度值，最小可设为0，此时对应的是灰度图(也就是俗话的“黑白图”)，
                // 为1表示饱和度不变，设置大于1，就显示过饱和
                mSaturationMatrix.reset();
                mSaturationMatrix.setSaturation(mSaturationValue);
                break;
            case FLAG_LUM: // 亮度
                // hueColor就是色轮旋转的角度,正值表示顺时针旋转，负值表示逆时针旋转
                mLightnessMatrix.reset(); // 设为默认值
                mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
                mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
                mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度
                // 这里相当于改变的是全图的色相
                break;
        }
        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
        mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
        canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        // 返回新的位图，也即调色处理后的图片
        return bmp;
    }

    private OnSeekListener onSeekListener;
    @Override
    public void setOnSeekListener(OnSeekListener onSeekListener) {
        this.onSeekListener = onSeekListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        int flag = (Integer) seekBar.getTag();
        switch (flag)
        {
            case FLAG_SATURATION:
                setSaturation(progress);
                break;
            case FLAG_LUM:
                setLum(progress);
                break;
            case FLAG_HUE:
                setHue(progress);
                break;
        }
        if (onSeekListener != null){
            onSeekListener.onSeek(getImage());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width, heightMeasureSpec);
    }
}
