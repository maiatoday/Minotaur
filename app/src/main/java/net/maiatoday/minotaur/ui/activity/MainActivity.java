package net.maiatoday.minotaur.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.User;
import net.maiatoday.minotaur.databinding.FragmentMainBinding;
import net.maiatoday.minotaur.red.Red;
import net.maiatoday.minotaur.ui.view.LengthPicker;
import net.maiatoday.minotaur.ui.view.MiniGauge;
import net.maiatoday.minotaur.ui.view.OnSetGauge;


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
            case R.id.action_plugh:
                gotoPlugh();
                break;
            case R.id.action_xyzzy:
                gotoTwisty();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoTwisty() {

        Intent intent = new Intent(this, TwistyActivity.class);
        startActivity(intent);
    }

    private void gotoSettings() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void gotoPlugh() {
        Intent intent = new Intent(this, PlughActivity.class);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private LengthPicker mWidth;
        private LengthPicker mHeight;
        private TextView mArea;

        private OnSetGauge mBigGauge;
        private MiniGauge mMiniCow;
        private MiniGauge mMiniDog;
        private MiniGauge mMiniLamb;
        private MiniGauge mMiniCat;
        private SeekBar mSeekBar;
        private RadioGroup mFarmGroup;

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

            FragmentMainBinding binding = FragmentMainBinding.bind(rootView);
            User user = new User("Blue", "Moon");
            binding.setUser(user);

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

            mFarmGroup = (RadioGroup) rootView.findViewById(R.id.mini_gauges_group);
            mBigGauge = (OnSetGauge) rootView.findViewById(R.id.gauge);
            mMiniCow = (MiniGauge) rootView.findViewById(R.id.mini_cow);
            mMiniCow.setBigGauge(mBigGauge);
            mMiniDog = (MiniGauge) rootView.findViewById(R.id.mini_dog);
            mMiniDog.setBigGauge(mBigGauge);
            mMiniLamb = (MiniGauge) rootView.findViewById(R.id.mini_lamb);
            mMiniLamb.setBigGauge(mBigGauge);
            mMiniCat = (MiniGauge) rootView.findViewById(R.id.mini_cat);
            mMiniCat.setBigGauge(mBigGauge);

            mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
            FloatingActionButton powButton = (FloatingActionButton) rootView.findViewById(R.id.buttonPow);
            powButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newNumber = mSeekBar.getProgress();
                    MiniGauge m = (MiniGauge) mFarmGroup.findViewById(mFarmGroup.getCheckedRadioButtonId());
                    m.setLastValue(newNumber);
                }
            });

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
                case R.id.mini_cow:
                    if (checked) {
                        Log.d("MainFragment", "Moo");
                    }
                        break;
                case R.id.mini_lamb:
                    if (checked) {

                        Log.d("MainFragment", "Baa");
                    }
                        break;
                case R.id.mini_dog:
                    if (checked) {

                        Log.d("MainFragment", "Woof grrr");
                    }
                    break;
                case R.id.mini_cat:
                    if (checked) {

                        Log.d("MainFragment", "Gsss Meauw");
                    }
                    break;
            }

        }
    }

    /**
     * Called when the user clicks the Blank button
     */
    public void gotoBlank(View view) {
        Toast.makeText(this, R.string.powToo, Toast.LENGTH_LONG).show();
    }


    public void onRadioButtonClicked(View view) {
        FragmentManager fm = getSupportFragmentManager();
        PlaceholderFragment fragment = (PlaceholderFragment) fm.findFragmentById(R.id.container);
        fragment.onRadioButtonClicked(view);

        if (((MiniGauge) view).isChecked()) {
            ((MiniGauge) view).updateBigGauge();
        }
    }
}
