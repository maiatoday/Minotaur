package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RadioButton;

import net.maiatoday.minotaur.R;

/**
 * Mini Gauge Radio Button
 * Created by maia on 2015/06/21.
 */
public class MiniGauge extends RadioButton {
    private Paint mPaint;
    private String mLabel;

    public MiniGauge(Context context) {
        super(context);
        init(context, null);
    }

    public MiniGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MiniGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int bgColor = Color.GRAY;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MiniGauge);
            bgColor = array.getColor(R.styleable.MiniGauge_bg_color, bgColor);
            mLabel = array.getString(R.styleable.MiniGauge_text);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.LINEAR_TEXT_FLAG);
        mPaint.setColor(bgColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        // canvas.drawPaint(textPaint);
        mPaint.setTextSize(4);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();
        final int cx = width/2 + getPaddingLeft();
        final int cy = height/2 + getPaddingTop();
//        final float diameter = Math.min(width, height) - mPaint.getStrokeWidth();
//        final float radius = diameter/2;
//        canvas.drawCircle(cx, cy, radius, mPaint);
        super.onDraw(canvas);
        canvas.drawText(mLabel, getPaddingLeft(), cy, mPaint);
    }



}
