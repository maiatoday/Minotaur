package net.maiatoday.minotaur.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.red.Red;
import net.maiatoday.minotaur.ui.view.Gauge;
import net.maiatoday.minotaur.ui.view.LengthPicker;
import net.maiatoday.minotaur.ui.view.MiniGauge;
import net.maiatoday.minotaur.ui.view.SetGauge;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Red.showTrace("onCreate");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Red.showTrace("onCreateOptionMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                gotoSettings();
                break;
            case R.id.action_blank:
                gotoBlank(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoSettings() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private LengthPicker mWidth;
        private LengthPicker mHeight;
        private TextView mArea;

        private SetGauge mBigGauge;

        public PlaceholderFragment() {
        }

        @Override
        public void onResume() {
            super.onResume();
            updateArea();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mWidth = (LengthPicker) rootView.findViewById(R.id.length_picker_width);
            mHeight = (LengthPicker) rootView.findViewById(R.id.length_picker_height);
            mArea = (TextView) rootView.findViewById(R.id.area_text);
            LengthPicker.OnChangeListener listener = new LengthPicker.OnChangeListener() {
                @Override
                public void onChange(int value) {
                    updateArea();
                }
            };
            mWidth.setOnChangeListener(listener);
            mHeight.setOnChangeListener(listener);

            mBigGauge = (Gauge) rootView.findViewById(R.id.gauge);

            return rootView;
        }

        private void updateArea() {
            int area = mWidth.getmNumInches()*mHeight.getmNumInches();
            mArea.setText(area + " sq inches");
        }


        public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            boolean checked = ((MiniGauge) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.mini_gauge1:
                    if (checked) {
                        Log.d("MainFragment", "one clicked");
                        mBigGauge.setGaugeTitle("Moo");
                    }
                        break;
                case R.id.mini_gauge2:
                    if (checked) {
                        mBigGauge.setGaugeTitle("Baa");

                        Log.d("MainFragment", "other clicked");
                    }
                        break;
                case R.id.mini_gauge3:
                    if (checked) {
                        mBigGauge.setGaugeTitle("Woof");

                        Log.d("MainFragment", "other clicked");
                    }
                    break;
                case R.id.mini_gauge4:
                    if (checked) {
                        mBigGauge.setGaugeTitle("Meouw");

                        Log.d("MainFragment", "other clicked");
                    }
                    break;
            }

        }
    }

    /**
     * Called when the user clicks the Blank button
     */
    public void gotoBlank(View view) {
        Intent intent = new Intent(this, BlankActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        FragmentManager fm = getSupportFragmentManager();
        PlaceholderFragment fragment = (PlaceholderFragment) fm.findFragmentById(R.id.container);
        fragment.onRadioButtonClicked(view);
    }
}
