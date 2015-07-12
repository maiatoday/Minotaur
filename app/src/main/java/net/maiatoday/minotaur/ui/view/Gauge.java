package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.maiatoday.minotaur.R;

import java.security.InvalidParameterException;


/**
 * A Gauge that displays a value as perimeter on a circle.
 * It can have one or more child views that define the labels.
 * Update labels and value of the gauge with the OnSetGauge interface calls.
 *
 * Children need to have these Resource Ids:
 * R.id.gauge_title
 * R.id.gauge_subtitle
 * R.id.gauge_message
 * R.id.gauge_value
 */
public class Gauge extends LinearLayout implements OnSetGauge {
    private static final int STROKE_WIDTH = 8;
    private static final float TOP = 270;
    /// Duration in 20ms ticks e.g. 50*20 = 1000ms
    private static final float DURATION = 50f;
    private Paint mInactivePaint;
    private Paint mActivePaint;
    private RectF mArcRectangle = new RectF();

    private TextView mTitleText;
    private TextView mSubTitleText;
    private TextView mMessageText;
    private TextView mValueText;
    private float mValue;
    private float mArcAngle = TOP;
    private boolean mUpdateValueText = true;
    private float mMin;
    private float mMax;
    private float mCurrentArcAngle = 0;
    private boolean mAnimate = false;
    private int time = 0;


    public Gauge(Context context) {
        super(context);
        init(context, null);
    }

    public Gauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Gauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Initialise the component from the attributes if there are any
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void init(Context context, AttributeSet attrs) {

        int bgColor = Color.GRAY;
        int activeColor = Color.BLUE;
        if (!isInEditMode()) {
            activeColor = context.getResources().getColor(R.color.primary);
        }
        int strokeWidth = STROKE_WIDTH;
        int strokeWidthInactive = STROKE_WIDTH;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Gauge);
            activeColor = array.getColor(R.styleable.Gauge_gauge_active_color, activeColor);
            bgColor = array.getColor(R.styleable.Gauge_gauge_inactive_color, bgColor);
            strokeWidth = array.getInteger(R.styleable.Gauge_gauge_stroke_width_active, STROKE_WIDTH);
            strokeWidthInactive = array.getInteger(R.styleable.Gauge_gauge_stroke_width_inactive, STROKE_WIDTH);
            mAnimate = array.getBoolean(R.styleable.Gauge_gauge_animate_set, false);
        }
        mInactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInactivePaint.setColor(bgColor);
        mInactivePaint.setStrokeWidth(strokeWidthInactive);
        mInactivePaint.setStyle(Paint.Style.STROKE);
        mActivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mActivePaint.setColor(activeColor);
        mActivePaint.setStrokeWidth(strokeWidth);
        mActivePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = Math.max(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        drawDial(canvas);
        super.dispatchDraw(canvas);
    }

    /**
     * Draw the dial on the canvas with the correct values depending on the value of the gauge
     *
     * @param canvas Canvas
     */
    private void drawDial(@NonNull Canvas canvas) {

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int diameter = Math.min(width, height) - STROKE_WIDTH;
        int cx = getPaddingLeft() + width / 2;
        int cy = getPaddingTop() + height / 2;
        int radius = diameter / 2;
        mArcRectangle.set(getPaddingLeft(), getPaddingTop(),
                width + getPaddingLeft(), height + getPaddingTop());
        canvas.drawCircle(cx, cy, radius, mInactivePaint);
        canvas.drawArc(mArcRectangle, TOP, mCurrentArcAngle, false, mActivePaint);
    }

    /**
     * Set the static label fields for the dial
     *
     * @param title           String
     * @param subtitle        String
     * @param message         String
     * @param updateValueText boolean to indicate if the value that is set must be displayed
     */
    @Override
    public void setStaticFields(final String title, final String subtitle, final String message, final boolean updateValueText) {
        findFields();
        setGaugeTitle(title);
        setGaugeSubtitle(subtitle);
        setMessage(message);
        mUpdateValueText = updateValueText;
    }

    /**
     * Find the child views for the linear layout to update later
     */
    private void findFields() {
        mTitleText = (TextView) findViewById(R.id.gauge_title);
        mSubTitleText = (TextView) findViewById(R.id.gauge_subtitle);
        mMessageText = (TextView) findViewById(R.id.gauge_message);
        mValueText = (TextView) findViewById(R.id.gauge_value);
    }

    /**
     * Set the title text
     *
     * @param text String
     */
    @Override
    public void setGaugeTitle(String text) {
        if (mTitleText != null) {
            mTitleText.setText(text);
        }
    }

    /**
     * Set the subtitle test.
     *
     * @param text String
     */
    @Override
    public void setGaugeSubtitle(final String text) {
        if (mSubTitleText != null) {
            mSubTitleText.setText(text);
        }
    }

    /**
     * Set the value on the dial.
     * if value <= min it will not show an arc.
     * if value >= max it will fill the entire circle.
     *
     * @param value int between min and max
     * @param min   int to indictate an empty circle
     * @param max   int to indicate a full circle
     */
    @Override
    public void setDialValue(final float value, final float min, final float max) {
        if (min < max) {
            //Clamp value between max and min
            mValue = (value < min) ? min : ((value > max) ? max : value);
            mMin = min;
            mMax = max;
            mArcAngle = ((mValue - mMin) / (mMax - mMin) * 360);
            if (mUpdateValueText) {
                setValueText(String.valueOf(value));
            }

            if (mAnimate) {
                mCurrentArcAngle = 0;
                time = 0;
                removeCallbacks(animator);
                post(animator);
            } else {
                mCurrentArcAngle = mArcAngle;
            }
        } else {
            throw (new InvalidParameterException("max is smaller than or equal to  min"));
        }
    }

    /**
     * Set the value text.
     * Use this if the value text is different to the string of value.
     *
     * @param text String
     */
    @Override
    public void setValueText(final String text) {
        if (mValueText != null) {
            mValueText.setText(text);
        }
    }

    /**
     * Set the message text.
     *
     * @param text String
     */
    @Override
    public void setMessage(final String text) {
        if (mMessageText != null) {
            mMessageText.setText(text);
        }
    }
    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            if (updateArcPosition()) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };

    /**
     * Update to the next arc position.
     * @return true another frame is needed else false
     */
    private boolean updateArcPosition() {
        mCurrentArcAngle = easeInOut(time, 0, mArcAngle, DURATION);
        if (time < DURATION) {
            time++;
        }
        if (mCurrentArcAngle >= mArcAngle) {
            mCurrentArcAngle = mArcAngle;
            return false;
        }
        return true;
    }

    /**
     * Simple sine wave ease in and out
     * @param time ticks that has passed
     * @param beginning value at start
     * @param change value at end
     * @param duration total number of ticks
     * @return current value
     */
    public static float  easeInOut(float time, float beginning , float change, float duration) {
        return -change/2 * ((float)Math.cos(Math.PI*time/duration) - 1) + beginning;
    }


    @NonNull
    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState ss = new SavedState(superState);
        ss.mCurrentArcAngle = mCurrentArcAngle;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mCurrentArcAngle = ss.mCurrentArcAngle;
    }

    static class SavedState extends BaseSavedState {
        float mCurrentArcAngle;

        SavedState(final Parcelable superState) {
            super(superState);
        }

        private SavedState(final Parcel in) {
            super(in);
            mCurrentArcAngle = in.readFloat();
        }

        @Override
        public void writeToParcel(@NonNull final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(mCurrentArcAngle);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(final Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(final int size) {
                return new SavedState[size];
            }
        };
    }
}
