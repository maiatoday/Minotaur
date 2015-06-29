package net.maiatoday.minotaur.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.odd.CursorFragmentStatePagerAdapter;
import net.maiatoday.minotaur.provider.MContract;
import net.maiatoday.minotaur.service.MIntentService;
import net.maiatoday.minotaur.ui.fragment.Xlidey1Fragment;
import net.maiatoday.minotaur.ui.fragment.Xlidey2Fragment;
import net.maiatoday.minotaur.ui.fragment.ZLidey1Fragment;
import net.maiatoday.minotaur.utils.db.FakeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwistyActivity extends AppCompatActivity implements OnTwistyInteractionListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = TwistyActivity.class.getSimpleName();

    private static final String KEY_MODE = "key_mode";
    private static final int LOADER_ROOM_XY = 0;
    private static final int LOADER_ROOM_ZZY = 1;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CursorFragmentStatePagerAdapter mAdapter;

    Random dice = new Random();
    private int mCurrentRoom;

    /**
     * Projection for querying the content provider.
     */
    private static final String[] PROJECTION = new String[]{
            MContract.Room._ID,
            MContract.Room.COLUMN_NAME,
            MContract.Room.COLUMN_TYPE
    };

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
                MIntentService.startActionFoo(TwistyActivity.this, mMode, magicNumber, mCurrentRoom);

            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(LOG_TAG, "Page selected position " + position);
                //TODO set the current room here
            }
        });
        initLoader();
    }

    private void initLoader() {
        switch (mMode) {
            case MODE_XY:
                getSupportLoaderManager().destroyLoader(LOADER_ROOM_ZZY);
                getSupportLoaderManager().initLoader(LOADER_ROOM_XY, null, this);
                break;
            case MODE_ZZY:
                getSupportLoaderManager().destroyLoader(LOADER_ROOM_XY);
                getSupportLoaderManager().initLoader(LOADER_ROOM_ZZY, null, this);
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        mAdapter = new TwistyCursorAdapter(this, getSupportFragmentManager(), null);
        viewPager.setAdapter(mAdapter);

//        TwistyAdapter adapter;
//        switch (mMode) {
//            case MODE_ZZY:
//                adapter = new TwistyAdapter(getSupportFragmentManager());
//                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 1", "zz"), "ZZ 1");
//                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 2", "zz"), "ZZ 2");
//                adapter.addFragment(ZLidey1Fragment.newInstance("ZZ 3", "zz"), "ZZ 3");
//                viewPager.setAdapter(adapter);
//                break;
//            case MODE_XY:
//                adapter = new TwistyAdapter(getSupportFragmentManager());
//                adapter.addFragment(Xlidey1Fragment.newInstance("XX 1", "zz"), "XX1 1");
//                adapter.addFragment(Xlidey1Fragment.newInstance("XX 2", "zz"), "XX1 2");
//                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
//                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
//                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
//                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
//                adapter.addFragment(Xlidey2Fragment.newInstance("XX 3", "zz"), "XX2 3");
//                viewPager.setAdapter(adapter);
//                break;
//        }
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
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        initLoader();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_MODE, mMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ROOM_XY: {
                return new CursorLoader(
                        this,   // Parent activity context
                        MContract.Room.CONTENT_URI,        // Table to query
                        PROJECTION,     // Projection to return
                        MContract.Room.COLUMN_TYPE + " =? OR " + MContract.Room.COLUMN_TYPE +  " =?",
                        new String[] {String.valueOf(FakeData.TYPE_CAVE), String.valueOf(FakeData.TYPE_CAVERN)},            // No selection arguments
                        null             // Default sort order
                );
            }

            case LOADER_ROOM_ZZY: {
                return new CursorLoader(
                        this,   // Parent activity context
                        MContract.Room.CONTENT_URI,        // Table to query
                        PROJECTION,     // Projection to return
                        MContract.Room.COLUMN_TYPE + " =? OR " + MContract.Room.COLUMN_TYPE +  " =?",
                        new String[] {String.valueOf(FakeData.TYPE_PASSAGE)},            // No selection arguments
                        null             // Default sort order
                );
            }

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // swapCursor does not close the cursor and this is right because the loader will do this
        mAdapter.swapCursor(data);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }

    private class TwistyCursorAdapter extends CursorFragmentStatePagerAdapter {

        public TwistyCursorAdapter(Context context, FragmentManager fm, Cursor cursor) {
            super(context, fm, cursor);
        }

        @Override
        public Fragment getItem(Context context, Cursor cursor) {
            int roomType = cursor.getInt(cursor.getColumnIndex(MContract.Room.COLUMN_TYPE));
            int id = cursor.getInt(cursor.getColumnIndex(MContract.Room._ID));
            String name = cursor.getString(cursor.getColumnIndex(MContract.Room.COLUMN_NAME));
            switch(roomType) {
                case FakeData.TYPE_CAVE:
                    return Xlidey1Fragment.newInstance(id, name);
                case FakeData.TYPE_CAVERN:
                    return Xlidey2Fragment.newInstance(id, name);
                case FakeData.TYPE_PASSAGE:
                    return ZLidey1Fragment.newInstance(id, name);
            }
            return null;
        }
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
