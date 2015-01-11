package net.maiatoday.minotaur;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by maia on 2014/11/18.
 */
public class RedRelativeLayout extends RelativeLayout {
    public RedRelativeLayout(Context context) {
        super(context);
        Red.showTrace("");
    }

    public RedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Red.showTrace("");
    }

    public RedRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Red.showTrace("");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Red.showTrace("");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Red.showTrace("");
    }


}
