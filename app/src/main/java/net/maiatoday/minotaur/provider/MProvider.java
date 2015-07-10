/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.maiatoday.minotaur.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import net.maiatoday.minotaur.utils.db.SelectionBuilder;

public class MProvider extends ContentProvider {
    Database mDatabaseHelper;

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = MContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /room
     */
    public static final int ROUTE_ROOM = 1;

    /**
     * URI ID for route: /room/{ID}
     */
    public static final int ROUTE_ROOM_ID = 2;
    /**
     * URI ID for route: /loot
     */
    public static final int ROUTE_LOOT = 3;

    /**
     * URI ID for route: /loot/{ID}
     */
    public static final int ROUTE_LOOT_ID = 4;

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, MContract.Room.PATH, ROUTE_ROOM);
        sUriMatcher.addURI(AUTHORITY, MContract.Room.PATH + "/*", ROUTE_ROOM_ID);
        sUriMatcher.addURI(AUTHORITY, MContract.Loot.PATH, ROUTE_LOOT);
        sUriMatcher.addURI(AUTHORITY, MContract.Loot.PATH + "/*", ROUTE_LOOT_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new Database(getContext());
        return true;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_ROOM:
                return MContract.Room.CONTENT_TYPE;
            case ROUTE_ROOM_ID:
                return MContract.Room.CONTENT_ITEM_TYPE;
            case ROUTE_LOOT:
                return MContract.Loot.CONTENT_TYPE;
            case ROUTE_LOOT_ID:
                return MContract.Loot.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     * <p/>
     * <p>Currently supports returning all entries (/entries) and individual entries by ID
     * (/entries/{ID}).
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_ROOM_ID: {
                // Return a single Room, by ID.
                String id = uri.getLastPathSegment();
                builder.where(MContract.Room._ID + "=?", id);
            }
            case ROUTE_ROOM: {
                // Return all known entries.
                builder.table(MContract.Room.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            }
            case ROUTE_LOOT_ID: {
                // Return a single Room, by ID.
                String id = uri.getLastPathSegment();
                builder.where(MContract.Loot._ID + "=?", id);
            }
            case ROUTE_LOOT: {
                // Return all known entries.
                builder.table(MContract.Loot.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Insert a new Room into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_ROOM: {
                long id = db.insertOrThrow(MContract.Room.TABLE_NAME, null, values);
                result = Uri.parse(MContract.Room.CONTENT_URI + "/" + id);
            }
            break;
            case ROUTE_ROOM_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_LOOT: {
                long id = db.insertOrThrow(MContract.Loot.TABLE_NAME, null, values);
                result = Uri.parse(MContract.Loot.CONTENT_URI + "/" + id);
            }
            break;
            case ROUTE_LOOT_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete an Room by database by URI.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ROOM:
                count = builder.table(MContract.Room.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ROOM_ID: {
                String id = uri.getLastPathSegment();
                count = builder.table(MContract.Room.TABLE_NAME)
                        .where(MContract.Room._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
            }
            break;
            case ROUTE_LOOT:
                count = builder.table(MContract.Loot.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_LOOT_ID: {
                String id = uri.getLastPathSegment();
                count = builder.table(MContract.Loot.TABLE_NAME)
                        .where(MContract.Loot._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * Update an Room in the database by URI.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ROOM:
                count = builder.table(MContract.Room.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ROOM_ID: {
                String id = uri.getLastPathSegment();
                count = builder.table(MContract.Room.TABLE_NAME)
                        .where(MContract.Room._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
            }
            break;

            case ROUTE_LOOT:
                count = builder.table(MContract.Loot.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_LOOT_ID: {
                String id = uri.getLastPathSegment();
                count = builder.table(MContract.Loot.TABLE_NAME)
                        .where(MContract.Loot._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * SQLite backend for @{link MProvider}.
     * <p/>
     * Provides access to an disk-backed, SQLite datastore which is utilized by MProvider. This
     * database should never be accessed by other parts of the application directly.
     */
    static class Database extends SQLiteOpenHelper {
        /**
         * Schema version.
         */
        public static final int DATABASE_VERSION = 1;
        /**
         * Filename for SQLite file.
         */
        public static final String DATABASE_NAME = "minotaur.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String COMMA_SEP = ",";
        private static final String NOT_NULL = " NOT NULL";

        /**
         * SQL statement to create "Room" table.
         */
        private static final String SQL_CREATE_ROOM =
                "CREATE TABLE " + MContract.Room.TABLE_NAME + " (" +
                        MContract.Room._ID + " INTEGER PRIMARY KEY," +
                        MContract.Room.COLUMN_TYPE + TYPE_INTEGER + COMMA_SEP +
                        MContract.Room.COLUMN_NAME + TYPE_TEXT + ")";
        /**
         * SQL statement to drop "Room" table.
         */
        private static final String SQL_DELETE_ROOM =
                "DROP TABLE IF EXISTS " + MContract.Room.TABLE_NAME;

        /**
         * SQL statement to create "Loot" table.
         */
        private static final String SQL_CREATE_LOOT =
                "CREATE TABLE " + MContract.Loot.TABLE_NAME + " (" +
                        MContract.Loot._ID + " INTEGER PRIMARY KEY," +
                        MContract.Loot.COLUMN_ROOM_ID + TYPE_INTEGER + NOT_NULL + COMMA_SEP +
                        MContract.Loot.COLUMN_IS_HERRING + TYPE_INTEGER + COMMA_SEP +
                        MContract.Loot.COLUMN_VALUE + TYPE_INTEGER + COMMA_SEP +
                        MContract.Loot.COLUMN_NAME + TYPE_TEXT + COMMA_SEP +
                        " FOREIGN KEY (" + MContract.Loot.COLUMN_ROOM_ID + ") REFERENCES " +
                        MContract.Room.TABLE_NAME  + ")";
        /**
         * SQL statement to drop "Loot" table.
         */
        private static final String SQL_DELETE_LOOT =
                "DROP TABLE IF EXISTS " + MContract.Loot.TABLE_NAME;

        public Database(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ROOM);
            db.execSQL(SQL_CREATE_LOOT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ROOM);
            db.execSQL(SQL_DELETE_LOOT);
            onCreate(db);
        }
    }
}
