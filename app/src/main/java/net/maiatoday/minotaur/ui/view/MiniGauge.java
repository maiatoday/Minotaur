package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import net.maiatoday.minotaur.R;

/**
 * Created by maia on 2015/06/21.
 */
public class MiniGauge extends View {
    private Paint mPaint;

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
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(bgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();
        final int cx = width/2 + getPaddingLeft();
        final int cy = height/2 + getPaddingTop();
        final float diameter = Math.min(width, height) - mPaint.getStrokeWidth();
        final float radius = diameter/2;
        canvas.drawCircle(cx, cy, radius, mPaint);
    }
}
