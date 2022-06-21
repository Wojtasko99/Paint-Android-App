package com.example.aplikacja4_dw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class PowierzchniaRysunku extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private static Canvas mCanvas;
    private static Canvas poj;
    private static SurfaceHolder mPojemnik;
    private  Paint mFarba = new Paint();
    private Thread mWatekRysujacy;
    private boolean mWatekPracuje = false;
    private Object mBlokada = new Object();
    private static Bitmap mBitmapa = null;
    private float xPos,yPos;
    private static int mFarba_ST = Color.BLUE;
    private int clear = 0;
    private Path mPath;

    public PowierzchniaRysunku(Context context, AttributeSet attrs) {
        super(context,attrs);

        mPojemnik = getHolder();
        mPojemnik.addCallback(this);
    }
    public void restart(){
        mWatekRysujacy = new Thread(String.valueOf(this));
        mWatekPracuje = true;
        mWatekRysujacy.start();
    }

    public void pause(){
        mWatekPracuje = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        performClick();
        mFarba.setColor(mFarba_ST);
        synchronized (mBlokada){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    xPos = event.getX();
                    yPos = event.getY();
                    poj = mPojemnik.lockCanvas();
                    poj.drawBitmap(mBitmapa,0,0,mFarba);
                    mFarba.setStyle(Paint.Style.FILL);
                    mCanvas.drawCircle(event.getX(),event.getY(),10,mFarba);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath = new Path();
                    mPath.moveTo(xPos,yPos);
                    mPath.lineTo(event.getX(),event.getY());
                    xPos = event.getX();
                    yPos = event.getY();
                    mPath.close();
                    mFarba.setStrokeWidth(5);
                    mFarba.setStyle(Paint.Style.FILL);
                    mFarba.setStyle(Paint.Style.STROKE);
                    mCanvas.drawPath(mPath,mFarba);
                    invalidate();
                    poj.drawBitmap(mBitmapa,0,0,mFarba);
                    break;
                case MotionEvent.ACTION_UP:
                    mFarba.setStyle(Paint.Style.FILL);
                    mCanvas.drawCircle(event.getX(),event.getY(),10,mFarba);
                    poj.drawBitmap(mBitmapa,0,0,mFarba);
                    mPojemnik.unlockCanvasAndPost(poj);
                    break;
            }
        }
        return true;
    }

    public boolean performClick(){
        return super.performClick();
    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        mBitmapa = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmapa);
        mCanvas.drawARGB(255,255,255,255);
        Canvas poj = mPojemnik.lockCanvas();
        poj.drawBitmap(mBitmapa,0,0,null);
        mPojemnik.unlockCanvasAndPost(poj);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mWatekPracuje = false;
    }

    @Override
    public void run() {
        while(mWatekPracuje){
            Canvas canvas = null;
            try {
                synchronized (mPojemnik) {
                    if (!mPojemnik.getSurface().isValid()) continue;
                    canvas = mPojemnik.lockCanvas(null);
                    synchronized (mBlokada) {
                        if (mWatekPracuje) {
                            canvas.drawBitmap(mBitmapa,0,0,null);
                        }
                    }
                }
            } finally {
                if(canvas != null){
                    mPojemnik.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(1000/25);
            }catch (InterruptedException e){ }
        }
    }
    public static void setColor(int color){
        mFarba_ST = color;
    }
    public static void clear(){
        poj = mPojemnik.lockCanvas();
        mCanvas.drawColor(Color.WHITE);
        poj.drawBitmap(mBitmapa,0,0,null);
        mPojemnik.unlockCanvasAndPost(poj);
    }
}
