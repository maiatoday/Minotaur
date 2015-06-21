package net.maiatoday.minotaur.red;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by maia on 2014/11/18.
 */
public class RedTextView extends TextView {
    public RedTextView(Context context) {
        super(context);
        Red.showTrace("");
    }

    public RedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Red.showTrace("");
    }

    public RedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Red.showTrace("");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Red.showTrace("");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Red.showTrace("");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
