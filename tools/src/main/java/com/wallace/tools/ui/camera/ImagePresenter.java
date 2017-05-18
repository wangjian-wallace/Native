package com.wallace.tools.ui.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.SeekBar;

import com.wallace.tools.R;
import com.wallace.tools.view.image.ImageProcessHelper;
import com.wallace.tools.view.image.ToneLayer;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Package com.wallace.tools.ui.camera
 * Created by Wallace.
 * on 2017/5/4.
 */

class ImagePresenter implements ImageContract.Presenter, SeekBar.OnSeekBarChangeListener {
    private ImageContract.ImageView view;

    ImagePresenter(ImageContract.ImageView view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getData() {
        mBitmap = BitmapFactory.decodeResource(view.getFragmentView().getResources(), R.drawable.sb);

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("text", "色调");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "素描");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "灰度化");
        list.add(map);

        map = new HashMap<>();
        map.put("text", "线性灰度");
        list.add(map);

        map = new HashMap<>();
        map.put("text", "二值化");
        list.add(map);

        map = new HashMap<>();
        map.put("text", "高斯模糊");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "锐化");
        list.add(map);

        map = new HashMap<>();
        map.put("text", "复古");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "浮雕");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "冰冻效果");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "黑白照片");
        list.add(map);
        map = new HashMap<>();
        map.put("text", "底片效果");
        list.add(map);
        map = new HashMap<>();
        map.put("text", " 油画效果");
        list.add(map);
        map = new HashMap<>();
        map.put("text", " 复原");
        list.add(map);


        view.toNext(list);
    }

    private ToneLayer toneLayer;
    private Bitmap mBitmap;

    @Override
    public void onClick(final int position) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                switch (position) {
                    case 0:
                        e.onNext(null);
                        break;
                    case 1:
                        e.onNext(ImageProcessHelper.getInstance().convertToSketch(mBitmap));
                        break;
                    case 2:
                        e.onNext(ImageProcessHelper.getInstance().bitmap2Gray(mBitmap));
                        break;
                    case 3:
                        e.onNext(ImageProcessHelper.getInstance().bitmap2LineGrey(mBitmap));
                        break;
                    case 4:
                        e.onNext(ImageProcessHelper.getInstance().gray2Binary(mBitmap));
                        break;
                    case 5:
                        e.onNext(BoxBlurFilter(mBitmap));
                        break;
                    case 6:
                        e.onNext(ImageProcessHelper.getInstance().sharpenImageAmeliorate(mBitmap));
                        break;
                    case 7:
                        e.onNext(ImageProcessHelper.getInstance().oldRemeberImage(mBitmap));
                        break;
                    case 8:
                        e.onNext(ImageProcessHelper.getInstance().reliefImage(mBitmap));
                        break;
                    case 9:
                        e.onNext(ImageProcessHelper.getInstance().iceImage(mBitmap));
                        break;
                    case 10:
                        e.onNext(ImageProcessHelper.getInstance().toBlackAndWhite(mBitmap));
                        break;

                    case 11:
                        e.onNext(ImageProcessHelper.getInstance().negativeFilm(mBitmap));
                        break;
                    case 12:
                        e.onNext(ImageProcessHelper.getInstance().oilPainting(mBitmap));
                        break;


                    default:
                        e.onNext(mBitmap);
                        break;
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.d("TAG", "onNext: ");
                        if (position != 0) {
                            view.setImageBitmap(bitmap);
                        }else {
                            toneLayer = new ToneLayer(view.getFragmentView().getActivity());
                            view.toChangeView(0, toneLayer.getParentView());
                            ArrayList<SeekBar> seekBars = toneLayer.getSeekBars();
                            for (int i = 0, size = seekBars.size(); i < size; i++) {
                                seekBars.get(i).setOnSeekBarChangeListener(ImagePresenter.this);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: ");
                    }


                });


    }

    @Override
    public void toStart() {

    }

    @Override
    public void toStop() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        int flag = (Integer) seekBar.getTag();
        switch (flag) {
            case ToneLayer.FLAG_SATURATION:
                toneLayer.setSaturation(progress);
                break;
            case ToneLayer.FLAG_LUM:
                toneLayer.setLum(progress);
                break;
            case ToneLayer.FLAG_HUE:
                toneLayer.setHue(progress);
                break;
        }

        view.setImageBitmap(toneLayer.handleImage(mBitmap, flag));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 水平方向模糊度
     */
    private static float hRadius = 10;
    /**
     * 竖直方向模糊度
     */
    private static float vRadius = 10;
    /**
     * 模糊迭代度
     */
    private static int iterations = 7;
    private static String TAG = "Image";

    public static Bitmap BoxBlurFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.getPixels(inPixels,
                0,
                width, 0,
                0,
                width, height);
        Log.d(TAG, "start: " + inPixels[0] + " " + outPixels[0]);
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, hRadius);
            blur(outPixels, inPixels, height, width, vRadius);
        }
        Log.d(TAG, "blur: " + inPixels[0] + " " + outPixels[0]);
        blurFractional(inPixels, outPixels, width, height, hRadius);
        blurFractional(outPixels, inPixels, height, width, vRadius);
        bitmap.setPixels(inPixels,
                0,
                width, 0,
                0,
                width, height);
        return bitmap;
    }
    public static void blur(int[] in, int[] out, int width, int height, float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];
        for (int i = 0; i < 256 * tableSize; i++) divide[i] = i / tableSize;

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) |
                        (divide[tr] << 16) |
                        (divide[tg] << 8) |
                        divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - r;
                if (i2 < 0) i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }
}
