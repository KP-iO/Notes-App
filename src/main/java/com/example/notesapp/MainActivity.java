package com.example.notesapp;
/**
 * Created by khrishawn
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openEditorNewNote();


//        Cursor cursor = getContentResolver().query(NotesProvider.CONTENT_URI,
//        DBOpenHelper.ALL_COLUMNS, null, null, null, null
//        );




        cursorAdapter = new NotesCursorAdaptor(this, null, 0);

        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
        LoaderManager.getInstance(this).initLoader(0,null,this);



    }

    private void openEditorNewNote() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Editor.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }
//Inserts note to app
    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);

        Log.d(TAG, "Inserted note: "+noteUri.getLastPathSegment());
    }
// Gives the app its main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
// Tells app what to do when certain buttons are pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_create_sample:
                insertSampleData();
                break;

            case R.id.action_delete_all:
                deleteAllNotes();
                break;

            case R.id.Help:
                openWebView();

        }



        return super.onOptionsItemSelected(item);
    }

     public void openWebView(){
      Intent intent = new Intent(MainActivity.this, Instructions.class);
        startActivity(intent);

    }

    private void insertSampleData(){
        insertNote("Simple note");
        insertNote("Multi-line\nnote");
        insertNote("Very long text which extends the width of the entire screen");

        restartLoader();

    }

    private void restartLoader() {
        LoaderManager.getInstance(this).restartLoader(0,null,this);
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickList =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int button) {
                        if(button == DialogInterface.BUTTON_POSITIVE) {

                            getContentResolver().delete(
                                    NotesProvider.CONTENT_URI,
                                    null, null
                            );
                            restartLoader();

                            Toast.makeText(MainActivity.this, getString(R.string.all_deleted),Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        Context context;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage("Are you sure?")
                .setPositiveButton(getString(android.R.string.yes), dialogClickList)
                .setNegativeButton(getString(android.R.string.no), dialogClickList)
                .show();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI, null , null , null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }
}
