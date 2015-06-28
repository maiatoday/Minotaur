package net.maiatoday.minotaur.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.maiatoday.minotaur.provider.MContract;
import net.maiatoday.minotaur.utils.db.FakeData;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class MIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "net.maiatoday.minotaur.service.action.FOO";
    private static final String ACTION_BAZ = "net.maiatoday.minotaur.service.action.BAZ";


    private static final String EXTRA_PARAM1 = "net.maiatoday.minotaur.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "net.maiatoday.minotaur.service.extra.PARAM2";
    private static final String EXTRA_PARAM3 = "net.maiatoday.minotaur.service.extra.PARAM3";
    private static final String LOG_TAG = MIntentService.class.getSimpleName();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, int param1, int param2, int param3) {
        Intent intent = new Intent(context, MIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        intent.putExtra(EXTRA_PARAM3, param3);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MIntentService() {
        super("MIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_PARAM1, 0);
                final int param2 = intent.getIntExtra(EXTRA_PARAM2, 1);
                final int param3 = intent.getIntExtra(EXTRA_PARAM3, 0);
                handleActionFoo(param1, param2, param3);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters. For the example I'll make this one a roll the dice and insert into the db action.
     */
    private void handleActionFoo(int mode, int magicNumber, int currentRoom) {
        if (magicNumber%2 == 0) {
            addLoot(currentRoom);
        } else {
            addHerring(currentRoom);
        }
        if (magicNumber == 2) {
            addCave();
        } else if (magicNumber == 5) {
            addCavern();
        } else if (magicNumber == 4) {
            addPassage();
        }
        Log.d(LOG_TAG, "do some random db changes: magicNumber" +magicNumber);
    }

    private void addLoot(int currentRoom) {

        ContentValues values = new ContentValues();
        values.put(MContract.Loot.COLUMN_NAME, FakeData.getTreasure());
        values.put(MContract.Loot.COLUMN_ROOM_ID, currentRoom);
        values.put(MContract.Loot.COLUMN_IS_HERRING, 0);
        values.put(MContract.Loot.COLUMN_VALUE, 100);
        getContentResolver().insert(MContract.Loot.CONTENT_URI, values);
        Log.d(LOG_TAG, "Add treasure");

    }

    private void addHerring(int currentRoom) {

        ContentValues values = new ContentValues();
        values.put(MContract.Loot.COLUMN_NAME, FakeData.getHerring());
        values.put(MContract.Loot.COLUMN_ROOM_ID, currentRoom);
        values.put(MContract.Loot.COLUMN_IS_HERRING, 1);
        values.put(MContract.Loot.COLUMN_VALUE, 100);
        getContentResolver().insert(MContract.Loot.CONTENT_URI, values);
        Log.d(LOG_TAG, "Add herring");

    }

    private void addCave() {

        ContentValues values = new ContentValues();
        values.put(MContract.Room.COLUMN_NAME, FakeData.getCave());
        values.put(MContract.Room.COLUMN_TYPE, FakeData.TYPE_CAVE);
        getContentResolver().insert(MContract.Room.CONTENT_URI, values);
        Log.d(LOG_TAG, "Add cave");

    }

    private void addCavern() {

        ContentValues values = new ContentValues();
        values.put(MContract.Room.COLUMN_NAME, FakeData.getCave());
        values.put(MContract.Room.COLUMN_TYPE, FakeData.TYPE_CAVERN);
        getContentResolver().insert(MContract.Room.CONTENT_URI, values);
        Log.d(LOG_TAG, "Add cavern");

    }

    private void addPassage() {

        ContentValues values = new ContentValues();
        values.put(MContract.Room.COLUMN_NAME, FakeData.getPassage());
        values.put(MContract.Room.COLUMN_TYPE, FakeData.TYPE_PASSAGE);
        getContentResolver().insert(MContract.Room.CONTENT_URI, values);
        Log.d(LOG_TAG, "Add passage");

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters. For the example I'll make this one, remove everything from the db
     */
    private void handleActionBaz(String param1, String param2) {

        Log.d(LOG_TAG, "whack the db");
        wackDb();
    }

    private void wackDb() {
        getContentResolver().delete(MContract.Loot.CONTENT_URI, null, null);
        getContentResolver().delete(MContract.Room.CONTENT_URI, null, null);
    }
}
