package net.maiatoday.minotaur.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    private static final String LOG_TAG = MIntentService.class.getSimpleName();

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFoo(Context context, int param1, int param2) {
        Intent intent = new Intent(context, MIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
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
                handleActionFoo(param1, param2);
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
    private void handleActionFoo(int mode, int magicNumber) {
        if (magicNumber%2 == 0) {
            // add valuable object
        } else {
            // add herring
        }
        if (magicNumber == 2) {
            // add a cave room
        } else if (magicNumber == 5) {
            // add a cavern room
        } else if (magicNumber == 4) {
            // add a passage
        }
        Log.d(LOG_TAG, "do some random db changes: magicNumber" +magicNumber);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters. For the example I'll make this one, remove everything from the db
     */
    private void handleActionBaz(String param1, String param2) {

        Log.d(LOG_TAG, "whack the db");
    }
}
