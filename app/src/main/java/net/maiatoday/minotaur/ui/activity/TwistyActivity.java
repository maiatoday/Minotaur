package net.maiatoday.minotaur.ui.activity;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.maiatoday.minotaur.R;

public class TwistyActivity extends AppCompatActivity {

    @IntDef({MODE_XY, MODE_ZZY})
    private @interface Mode {
    }

    private static final int MODE_XY = 0;
    private static final int MODE_ZZY = 1;

    private
    @Mode
    int mMode = MODE_XY;

    MenuItem mItemXY;
    MenuItem mItemZZY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twisty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twisty, menu);
        mItemXY = menu.findItem(R.id.action_xy);
        mItemZZY = menu.findItem(R.id.action_zzy);
        modeAdjustActions();
        return true;
    }

    private void modeAdjustActions() {
        mItemXY.setVisible(mMode == MODE_ZZY);
        mItemZZY.setVisible(mMode == MODE_XY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_xy:
                toggleMode();
                break;
            case R.id.action_zzy:
                toggleMode();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void toggleMode() {
        String message;
        if (mMode == MODE_XY) {
            mMode = MODE_ZZY;
            message = "ZZY!";
        } else {
            mMode = MODE_XY;
            message = "XY!";
        }
        modeAdjustActions();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
