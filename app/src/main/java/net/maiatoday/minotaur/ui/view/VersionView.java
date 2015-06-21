package net.maiatoday.minotaur.ui.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by maia on 2015/06/21.
 */
public class VersionView extends TextView {
    public VersionView(Context context) {
        super(context);
        setVersion();
    }

    public VersionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVersion();
    }

    public VersionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVersion();
    }

    private void setVersion() {
        if (isInEditMode()) {
            setText("3.7_99");
        } else {
            try {
                PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                setText(packageInfo.versionName + "_" + packageInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}
