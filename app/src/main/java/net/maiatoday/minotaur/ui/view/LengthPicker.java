package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.maiatoday.minotaur.R;

/**
 * Created by maia on 2015/06/21.
 */
public class LengthPicker extends LinearLayout {
    private static final String KEY_SUPER_STATE = "superState";
    private static final String KEY_NUM_INCHES = "numInches";
    private View mPlusButton;
    private TextView mTextView;
    private View mMinusButton;

    private int mNumInches = 0;
    private OnChangeListener mListener = null;

    public LengthPicker(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.length_picker, this);
        mPlusButton = findViewById(R.id.plus_btn);
        mMinusButton = findViewById(R.id.minus_btn);
        mTextView = (TextView) findViewById(R.id.text);
        setOrientation(LinearLayout.HORIZONTAL);
        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.plus_btn:
                        mNumInches++;
                        updateControls();
                        if (mListener != null) {
                            mListener.onChange(mNumInches);
                        }
                        break;
                    case R.id.minus_btn:
                        mNumInches--;
                        updateControls();
                        if (mListener != null) {
                            mListener.onChange(mNumInches);
                        }
                        break;
                }

            }
        };
        mPlusButton.setOnClickListener(listener);
        mMinusButton.setOnClickListener(listener);
        updateControls();

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_NUM_INCHES, mNumInches);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mNumInches = bundle.getInt(KEY_NUM_INCHES);
            super.onRestoreInstanceState(bundle.getBundle(KEY_SUPER_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
        updateControls();
    }

    private void updateControls() {
        int feet = mNumInches / 12;
        int inches = mNumInches % 12;
        String text = String.format("%d' %d\"", feet, inches);
        if (feet == 0) {
            text = String.format("%d\"", inches);
        } else if (inches == 0) {
            text = String.format("%d'", feet);
        }

        mTextView.setText(text);
        mMinusButton.setEnabled(mNumInches > 0);

    }

    public LengthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public interface OnChangeListener {
        void onChange(int value);
    }

    public int getmNumInches() {
        return mNumInches;
    }
}
