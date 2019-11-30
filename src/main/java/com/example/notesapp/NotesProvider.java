package com.example.notesapp;
/**
 * Created by khrishawn
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NotesProvider extends ContentProvider {
    // globally unique string that identifies content provider
    private static final String AUTHORITY = "com.example.notesapp.notesprovider";

    // represents entire data set
    private static final String BASE_PATH = "notes";

    //CONTNENT_URI is a uniform resource identifier that identifies content provider
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //constants to identify operation
    private static final int  NOTES = 1;
    private static final int  NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(android.content.UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Note";

    static{
uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);

}
    SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String selection, String[] strings1, String s1) {

        if (uriMatcher.match(uri) == NOTES_ID) {

        }

        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.NOTE_CREATED + " DESC");


    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String table;
        long id = database.insert(DBOpenHelper.TABLE_NOTES, null, contentValues);


        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES, contentValues, selection, selectionArgs);
    }
}
