package com.example.helloworld.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    private Bitmap btmBackground, btmView;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private String mShape = "ROUND"; // Defaults to round
    private int mOpacity = 255; // 0-255
    private int brushSize, backgroundColor;
    private float mX, mY;
    private Canvas mCanvas;
    private final int DEFFERENCE_SPACE = 4;
    private ArrayList<Bitmap> actionList = new ArrayList<>();

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        brushSize = 10;
        backgroundColor = Color.WHITE;
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(false); // I hate antialiasing.
        mPaint.setDither(true); // ???
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(toCap(mShape));     // THIS IS THE CURSOR SHAPE!!
        mPaint.setStrokeJoin(toJoin(mShape));   // THIS IS THE CURSOR SHAPE!!
        mPaint.setStrokeWidth(toPx(brushSize));
    }

    private Paint.Cap toCap(String mShape) {
        // 0 = BUTT
        // 1 = ROUND
        // 2 = SQUARE
        Paint.Cap shape;
        if (mShape.equals("BUTT")) {
            shape = Paint.Cap.BUTT;
        }
        else if (mShape.equals("ROUND")) {
            shape = Paint.Cap.ROUND;
        }
        else {
            shape = Paint.Cap.SQUARE;
        }
        return shape;
    }

    private Paint.Join toJoin(String mShape) {
        // 0 = MITER
        // 1 = ROUND
        // 2 = BEVEL
        Paint.Join shape;
        if (mShape.equals("BUTT")) {
            shape = Paint.Join.MITER;
        }
        else if (mShape.equals("ROUND")) {
            shape = Paint.Join.ROUND;
        }
        else {
            shape = Paint.Join.BEVEL;
        }
        return shape;
    }

    private float toPx(int brushSize) {
        return brushSize*(getResources().getDisplayMetrics().density);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        btmBackground = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        btmView = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(btmView);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        canvas.drawBitmap(btmBackground,0,0,null);
        canvas.drawBitmap(btmView,0,0,null);
    }

    public void setBrushSize(int sz) {
        brushSize = sz;
        mPaint.setStrokeWidth(brushSize);
    }

    public void setBrushShape(String shp) {
        mShape = shp;
        mPaint.setStrokeCap(toCap(mShape));
        mPaint.setStrokeJoin(toJoin(mShape));
    }

    public void setBrushColor(int clr) {
        mPaint.setColor(clr);
    }

    public void setBrushOpacity(int alph) {
        mOpacity = alph;
        mPaint.setAlpha(mOpacity);
        invalidate();
    }

    public void addLastAction(Bitmap bitmap) {
        actionList.add(bitmap);
    }

    public void clearActions() {
        actionList = new ArrayList<>();
        btmView = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(btmView);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                swipe(x,y);
                break;
            case MotionEvent.ACTION_UP:
                liftUp(x,y);
                break;
        }
        return true;
    }

    private void liftUp(float x, float y) {
        mPath.reset();
        addLastAction(getBitmap());
    }

    private void swipe(float x, float y) {
        float tempx = Math.abs(x-mX);
        float tempy = Math.abs(y-mY);
        if ((tempx>=DEFFERENCE_SPACE) || (tempy>=DEFFERENCE_SPACE)) {
            mPath.quadTo(x,y,(x+mX)/2, (y+mY)/2);
            mX = x;
            mY = y;
            mCanvas.drawPath(mPath,mPaint);
            invalidate();
        }

    }

    private void touchDown(float x, float y) {
        mPath.moveTo(x,y);
        mX = x;
        mY = y;
    }

    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap b = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return b;
    }
}
