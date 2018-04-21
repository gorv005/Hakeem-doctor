package com.app.hakeem.util;
/**
 * Created by aditya.singh on 2/16/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.app.hakeem.R;
import com.app.hakeem.interfaces.ITempValue;

public class VerticalSeekBarForBloodPressure extends View {

    private Context context;
    int totalVal = 13;
    int cellHeight = 0;
    private Bitmap pipe;
    private Bitmap slider;
    private int y = 20;
    private int index;
    private int nearVal;
    int minVal = 125;
    int maxVal = 10;
    //    private Bitmap bg;
    private Paint paint;
    ITempValue iTempValue;

    public VerticalSeekBarForBloodPressure(Context context) {
        super(context);
        init(context);
    }


    public VerticalSeekBarForBloodPressure(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public VerticalSeekBarForBloodPressure(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        pipe = ImageCache.getBitmap(R.drawable.off_pipe, context);
        slider = ImageCache.getBitmap(R.drawable.slider, context);
//        bg = ImageCache.getBitmap(R.drawable.seekbaar, context);
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);


    }


    public void SeekBar(ITempValue context) {
        iTempValue = context;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        cellHeight = getHeight() / totalVal;

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellHeight = getHeight() / totalVal;
        paint.setColor(ContextCompat.getColor(context, R.color.blue_dark));
        drawRoundRect(getWidth() / 4, 0, 3 * getWidth() / 4, getHeight(), 20, paint, canvas);
        paint.setColor(ContextCompat.getColor(context, R.color.blue));
        drawRoundRect(getWidth() / 4 + 4, 4, 3 * getWidth() / 4 - 4, getHeight() - 8, 20, paint, canvas);

        pipe = Bitmap.createScaledBitmap(pipe, getWidth() / 2 - 10, pipe.getHeight(), true);
        slider = Bitmap.createScaledBitmap(slider, getWidth() / 2, slider.getHeight(), true);
        for (int i = 1; i < totalVal; i++) {
            canvas.drawBitmap(pipe, getWidth() / 4 + 5, i * cellHeight, null);
            paint.setColor(Color.BLACK);
            paint.setTextSize(18);
            paint.setTypeface(Typeface.SERIF);
            canvas.drawText((minVal - (i * 5)) + "", 3 * getWidth() / 4 + 6, i * cellHeight + 8, paint);
            canvas.drawText((minVal - (i * 5) + 60) + "", 4, i * cellHeight + 8, paint);

        }
//        canvas.drawBitmap(slider, (getWidth() - slider.getWidth()) / 2, i * cellHeight + cellHeight / 2 - 12, null);
        canvas.drawBitmap(slider, getWidth() / 4, y - 12, null);
    }


    private void drawRoundRect(float left, float top, float right, float bottom, int radius, Paint paint, Canvas canvas) {
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right - radius, top);
        path.quadTo(right, top, right, top + radius);
        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);
        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);
        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);
        canvas.drawPath(path, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) event.getY() - slider.getHeight() / 2;

            case MotionEvent.ACTION_MOVE:


                if (event.getY() > cellHeight && event.getY() < getHeight() - cellHeight / 2) {
                    y = (int) event.getY() - slider.getHeight() / 2;
                    invalidate();
                }

                break;

//            case MotionEvent.ACTION_UP:
//                nearVal = y % cellHeight;
//                index = y / cellHeight;
//                if (nearVal > cellHeight / 2) {
//                    index++;
//                }
//                y = index * cellHeight + cellHeight / 2;
//                invalidate();

            default:
                break;
        }

        Log.e("DEBUG", "" + getCurrentVal());

        return true;
    }


    public void initilize(ITempValue iTempValue) {
        this.iTempValue = iTempValue;
    }

    public float getCurrentVal() {
        if(SharedPreference.getInstance(context).getString(C.LANGUAGE)!=null && SharedPreference.getInstance(context).getString(C.LANGUAGE).equals(C.ARABIC)) {
            Util.setAppLocale(C.English, context);
        }
        iTempValue.getValue(Float.parseFloat(String.format(
               "%.1f", (float) (minVal - ((y + slider.getHeight() / 2)*5 / (float) cellHeight))+2.5F)));
        return Float.parseFloat(String.format(
                "%.1f", (float) (minVal - ((y + slider.getHeight() / 2)*5 / (float) cellHeight))+2.5F));
    }
}
