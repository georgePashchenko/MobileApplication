package com.example.trollapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SampleView extends View {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    private Bitmap mBitmap;
    private final float[] mVerts = new float[COUNT*2];
    private final float[] mOrig = new float[COUNT*2];

    private final Matrix mMatrix = new Matrix();
    private final Matrix mInverse = new Matrix();

    private static void setXY(float[] array, int index, float x, float y) {
        array[index*2 + 0] = x;
        array[index*2 + 1] = y;
    }

    public SampleView(Context context, int ids, int newW, int newH) {
        super(context);
        setFocusable(true);
       
        //Options options = new BitmapFactory.Options();
        //options.inScaled = true;
        //Bitmap source = BitmapFactory.decodeResource(a.getResources(), path, options);
        
        
        int newWidth = newW;
        int newHeight = newH-100;
        
        mBitmap = BitmapFactory.decodeResource(getResources(),ids);
        Bitmap  scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

        float ratioX = newWidth / (float) mBitmap.getWidth();
        float ratioY = newHeight / (float) mBitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(mBitmap, middleX - mBitmap.getWidth() / 2, middleY - mBitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        
        mBitmap = scaledBitmap;
        
        float w = mBitmap.getWidth();
        float h = mBitmap.getHeight();
        // construct our mesh
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = h * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = w * x / WIDTH;
                setXY(mVerts, index, fx, fy);
                setXY(mOrig, index, fx, fy);
                index += 1;
            }
        }

        mMatrix.setTranslate(10, 10);
        mMatrix.invert(mInverse);
    }

    @Override protected void onDraw(Canvas canvas) {
    	//canvas.drawColor(0xFFCCCCCC);

        canvas.concat(mMatrix);
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0,
                              null, 0, null);

    }

    private void warp(float cx, float cy) {
        final float K = 70000;
        float[] src = mOrig;
        float[] dst = mVerts;
        for (int i = 0; i < COUNT*2; i += 2) {
            float x = src[i+0];
            float y = src[i+1];
            float dx = cx - x;
            float dy = cy - y;
            float dd = dx*dx + dy*dy;
            float d = FloatMath.sqrt(dd);
            float pull = K / (dd + 0.000001f);

            pull /= (d + 0.000001f);
         //   android.util.Log.d("skia", "index " + i + " dist=" + d + " pull=" + pull);

            if (pull >= 1) {
                dst[i+0] = cx;
                dst[i+1] = cy;
            } else {
                dst[i+0] = x + dx * pull;
                dst[i+1] = y + dy * pull;
            }
        }
    }

    private int mLastWarpX = -9999; // don't match a touch coordinate
    private int mLastWarpY;

    @Override public boolean onTouchEvent(MotionEvent event) {
        float[] pt = { event.getX(), event.getY() };
        mInverse.mapPoints(pt);

        int x = (int)pt[0];
        int y = (int)pt[1];
        if (mLastWarpX != x || mLastWarpY != y) {
            mLastWarpX = x;
            mLastWarpY = y;
            warp(pt[0], pt[1]);
            invalidate();
        }
        return true;
    }
}