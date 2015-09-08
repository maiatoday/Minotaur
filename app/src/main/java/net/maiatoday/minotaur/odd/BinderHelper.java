package net.maiatoday.minotaur.odd;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.widget.TextView;

/**
 * Created by maia on 2015/09/08.
 */
public class BinderHelper {

    static LruCache<Integer, Typeface> fontCache = new LruCache<>(5);

    @BindingAdapter({"bind:font"})
    public static void setFont(TextView view, String customFont) {
        Typeface font = fontCache.get(customFont.hashCode());
        if (font == null) {
            Context ctx = view.getContext();
            font = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + customFont);
        }
        view.setTypeface(font);
    }

//    @BindingAdapter({"bind:font"})
//    public static void setFont(TextView textView, String fontName){
//        textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName));
//    }

}
