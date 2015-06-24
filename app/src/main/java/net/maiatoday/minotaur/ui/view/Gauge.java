package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.maiatoday.minotaur.R;

public class Gauge extends LinearLayout implements SetGauge {
    private static final int STROKE_WIDTH = 8;
    private Paint mPaint;
    private Paint mArcPaint;
    private RectF mArcRectangle = new RectF();
    private float mArcRadius;

    private TextView mTitleText;
    private TextView mSubTitleText;
    private TextView mMessageText;
    private TextView mValueText;
    private int mValue;


    public Gauge(Context context) {
        super(context);
        init();
    }

    public Gauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Gauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(Color.BLUE);
        mArcPaint.setStrokeWidth(STROKE_WIDTH);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mTitleText = (TextView) findViewById(R.id.gauge_title);
        mSubTitleText = (TextView) findViewById(R.id.gauge_subtitle);
        mMessageText = (TextView) findViewById(R.id.gauge_message);
        mValueText = (TextView) findViewById(R.id.gauge_value);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = Math.max(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
//        mArcRadius = size * 0.5f;
//        mArcRectangle.set(-mArcRadius, -mArcRadius,
//                mArcRadius, mArcRadius);
    }

//    @Override
//    public void onDraw(Canvas canvas) {
//        int width = getWidth() - getPaddingLeft() - getPaddingRight();
//        int height = getHeight() - getPaddingTop() - getPaddingBottom();
//        int diameter = Math.min(width, height) - STROKE_WIDTH;
//        int cx = getPaddingLeft() + width / 2;
//        int cy = getPaddingTop() + height / 2;
//        int radius = diameter / 2;
//        canvas.drawCircle(cx, cy, radius, mPaint);
//        drawValue(canvas, cx, cy, radius);
//    }
//
//    private void drawValue(Canvas canvas, int cx, int cy, int radius) {
//
//        canvas.save();
//        final float degrees = 360f / NUM_WEDGES;
//        for (int i = 0; i < NUM_WEDGES; ++i) {
//            canvas.rotate(degrees, cx, cy);
//            canvas.drawLine(cx, cy, cx, cy - radius, mPaint);
//        }
//        canvas.restore();
//    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        drawDial(canvas);
//        canvas.save();
//        canvas.translate(0, getHeight());
//        canvas.rotate(-90, 0, 0);
        super.dispatchDraw(canvas);
//        canvas.restore();
    }

    private void drawDial(@NonNull Canvas canvas) {

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int diameter = Math.min(width, height) - STROKE_WIDTH;
        int cx = getPaddingLeft() + width / 2;
        int cy = getPaddingTop() + height / 2;
        int radius = diameter / 2;
        mArcRectangle.set(getPaddingLeft(), getPaddingTop(),
                width+getPaddingLeft(), height+getPaddingTop());
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.drawArc(mArcRectangle, 270, 180, false, mArcPaint);

    }

    @Override
    public void setGaugeTitle(String text) {
        if (mTitleText == null) {
            mTitleText = (TextView) findViewById(R.id.gauge_title);
        }
        if(mTitleText != null) {
            mTitleText.setText(text);
        }
    }

    @Override
    public void setGaugeSubtitle(String text) {
        if(mSubTitleText != null) {
            mSubTitleText.setText(text);
        }
    }

    @Override
    public void setValue(int value) {
        mValue = value;
    }

    @Override
    public void setValueText(String text) {
        if(mValueText != null) {
            mValueText.setText(text);
        }
    }

    @Override
    public void setMessage(String text) {
        if(mMessageText != null) {
            mMessageText.setText(text);
        }
    }
}
