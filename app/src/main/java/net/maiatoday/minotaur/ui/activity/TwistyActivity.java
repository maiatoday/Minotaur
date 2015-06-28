package net.maiatoday.minotaur.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.service.MIntentService;
import net.maiatoday.minotaur.ui.fragment.Xlidey1Fragment;
import net.maiatoday.minotaur.ui.fragment.Xlidey2Fragment;
import net.maiatoday.minotaur.ui.fragment.ZLidey1Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwistyActivity extends AppCompatActivity implements OnTwistyInteractionListener{

    private static final String KEY_MODE = "key_mode";
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    Random dice = new Random();

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int magicNumber = dice.nextInt(6);
                Snackbar.make(view, "You rolled a " + magicNumber, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MIntentService.startActionFoo(TwistyActivity.this, mMode, magicNumber);

            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        TwistyAdapter adapter;
        switch (mMode) {
            case MODE_ZZY:
                adapter = new TwistyAdapter(getSupportFragmentManager());
                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 1", "zz"), "ZZ 1");
                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 2", "zz"), "ZZ 2");
                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 3", "zz"), "ZZ 3");
                viewPager.setAdapter(adapter);
                break;
            case MODE_XY:
                adapter = new TwistyAdapter(getSupportFragmentManager());
                adapter.addFragment(Xlidey1Fragment.newInstance("XX 1", "zz"), "XX1 1");
                adapter.addFragment(Xlidey1Fragment.newInstance("XX 2", "zz"), "XX1 2");
                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
                viewPager.setAdapter(adapter);
                break;
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
            case R.id.action_delete:
                MIntentService.startActionBaz(this, "", "");
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
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_MODE, mMode);
        super.onSaveInstanceState(outState);
    }

    private class TwistyAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public TwistyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

//    private class ZTwistyAdapter extends TwistyAdapter {
//        public ZTwistyAdapter(FragmentManager supportFragmentManager) {
//            super();
//        }
//    }
//
//    private class XTwistyAdapter extends TwistyAdapter {
//        public XTwistyAdapter(FragmentManager supportFragmentManager) {
//            super();
//        }
//    }
}
