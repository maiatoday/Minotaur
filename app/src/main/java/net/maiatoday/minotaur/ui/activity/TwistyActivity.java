package net.maiatoday.minotaur.ui.activity;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.maiatoday.minotaur.R;

public class TwistyActivity extends AppCompatActivity {

    private static final String KEY_MODE = "key_mode";

    @IntDef({MODE_XY, MODE_ZZY})
    private @interface Mode {
    }

    private static final int MODE_XY = 0;
    private static final int MODE_ZZY = 1;

    private
    @Mode
    int mMode = MODE_XY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //noinspection ResourceType
            mMode = savedInstanceState.getInt(KEY_MODE, MODE_XY);
        }
        setContentView(R.layout.activity_twisty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twisty, menu);
        modeAdjustAction(menu.findItem(R.id.action_mode_toggle));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_mode_toggle:
                toggleMode(item);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void toggleMode(MenuItem item) {
        String message;
        if (mMode == MODE_XY) {
            mMode = MODE_ZZY;
            message = "Now in ZZY!";
        } else {
            mMode = MODE_XY;
            message = "Now in XY!";
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        modeAdjustAction(item);
        modeAdjustVisuals();
    }

    private void modeAdjustAction(final MenuItem item) {
        switch (mMode) {
            case MODE_ZZY:
                item.setIcon(R.drawable.ic_favorite_white_24dp);
                item.setTitle(R.string.action_xy);
                break;
            case MODE_XY:
                item.setIcon(R.drawable.ic_grade_white_24dp);
                item.setTitle(R.string.action_zzy);
                break;
        }
    }

    private void modeAdjustVisuals() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_MODE, mMode);
        super.onSaveInstanceState(outState);
    }
}
