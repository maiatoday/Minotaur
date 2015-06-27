package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RadioButton;

import net.maiatoday.minotaur.R;

public class MiniGauge extends RadioButton {
    private Paint mUnCheckedPaint;
    private String mLabel = "";
    private OnSetGauge mBigGauge;
    private String mBigTitle = "";
    private String mBigSubtitle = "";
    private String mBigMessage = "";
    private Paint mCheckedPaint;
    private float mlastValue = 0;
    private float mMax = 100;
    private float mMin = 0;
    private String mLastValueString = "";
    private Rect mBoundsLabel = new Rect();
    private int mLabelValueDivide = 4;

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
        int checkedColour = Color.WHITE;
        int unCheckedColour = Color.BLACK;
        float labelSize = getTextSize(); //super hackery we use the textsize of the base textview
        float valueSize = getTextSize(); //super hackery we use the textsize of the base textview
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MiniGauge);
            checkedColour = array.getColor(R.styleable.MiniGauge_checked_color, checkedColour);
            unCheckedColour = array.getColor(R.styleable.MiniGauge_unchecked_color, unCheckedColour);
            mLabel = array.getString(R.styleable.MiniGauge_mini_label);
            mBigTitle = array.getString(R.styleable.MiniGauge_mini_title);
            mBigSubtitle = array.getString(R.styleable.MiniGauge_mini_subtitle);
            mBigMessage = array.getString(R.styleable.MiniGauge_message);
            mMin = array.getFloat(R.styleable.MiniGauge_min_value, 0);
            mMax = array.getFloat(R.styleable.MiniGauge_max_value, 100);
            mlastValue = array.getFloat(R.styleable.MiniGauge_default_value, 0);
        }
        mUnCheckedPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.LINEAR_TEXT_FLAG);
        mUnCheckedPaint.setColor(unCheckedColour);
        mUnCheckedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mUnCheckedPaint.setTextAlign(Paint.Align.CENTER);
        mUnCheckedPaint.setTextSize(labelSize);
        mUnCheckedPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        mCheckedPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.LINEAR_TEXT_FLAG);
        mCheckedPaint.setColor(checkedColour);
        mCheckedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCheckedPaint.setTextAlign(Paint.Align.CENTER);
        mCheckedPaint.setTextSize(valueSize);
        mCheckedPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        String testString = "0.0";
        mUnCheckedPaint.getTextBounds(testString, 0, testString.length(), mBoundsLabel); // Magic numbers ok because we just use the bounds for spacing


    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        super.onDraw(canvas);
        final int width = getWidth();
        final int height = getHeight();
        final int cx = Math.min(width, height)/2;
        final int cy = Math.min(width, height)/2;
        mLabelValueDivide = mBoundsLabel.height();

        if (isChecked()) {
            canvas.drawText(
                    mLabel,
                    cx,
                    cy + mBoundsLabel.height() / 2 - mLabelValueDivide,
                    mCheckedPaint);

            canvas.drawText(
                    mLastValueString,
                    cx,
                    cy + mBoundsLabel.height() / 2 + mLabelValueDivide,
                    mCheckedPaint);
        } else {

            canvas.drawText(
                    mLabel,
                    cx,
                    cy + mBoundsLabel.height() / 2 - mLabelValueDivide,
                    mUnCheckedPaint);

            canvas.drawText(
                    mLastValueString,
                    cx,
                    cy + mBoundsLabel.height() / 2 + mLabelValueDivide,
                    mUnCheckedPaint);

        }

    }

    /**
     * Keep link to the big gauge that has to be updated by this button.
     * @param bigGauge Interface to set the BigGauge values
     */
    public void setBigGauge(OnSetGauge bigGauge) {
        this.mBigGauge = bigGauge;
    }

    /**
     * Update the face of the associated BigGauge
     */
    public void updateBigGauge() {
        if (mBigGauge != null) {
            mBigGauge.setStaticFields(mBigTitle, mBigSubtitle, mBigMessage, true);
            mBigGauge.setDialValue(mlastValue, mMin, mMax);
        }
    }

    /**
     * Update the data on the associated BigGauge
     * @param lastValue float last data point
     */
    public void setLastValue(float lastValue) {
        this.mlastValue = lastValue;
        mLastValueString = String.valueOf(mlastValue);        updateBigGauge();
        invalidate();
    }
}